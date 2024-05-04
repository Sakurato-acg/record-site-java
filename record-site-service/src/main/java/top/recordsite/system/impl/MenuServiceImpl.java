package top.recordsite.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import top.recordsite.dto.system.MenuAddDto;
import top.recordsite.dto.system.MenuSearchDto;
import top.recordsite.dto.system.MenuUpdateDto;
import top.recordsite.entity.system.Menu;
import top.recordsite.entity.system.RoleMenu;
import top.recordsite.enums.system.MenuDictionary;
import top.recordsite.enums.system.UserDictionary;
import top.recordsite.exception.BusinessException;
import top.recordsite.mapper.system.MenuMapper;
import top.recordsite.security.LoginUserDetail;
import top.recordsite.system.IMenuService;
import top.recordsite.system.IRoleMenuService;
import top.recordsite.system.IUserRoleService;
import top.recordsite.utils.BeanCopyUtils;
import top.recordsite.utils.RedisUtlis.RedisString;
import top.recordsite.utils.RedisUtlis.RedisUtils;
import top.recordsite.vo.ListVo;
import top.recordsite.vo.system.MenuVo;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @author lpl
 * @since 2023-09-25
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RedisUtils<LoginUserDetail> redisUtils;

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private IRoleMenuService roleMenuService;


    @Override
    public List<MenuVo> selectRouterMenuTreeByUserId(Integer userId) {
        List<MenuVo> menuVoList = null;

        //判断是否是管理员
//        if (userId == 1 || SecurityUtils.isAdmin()) {
//            是管理员，则获取全部菜单
//            menuVoList = menuMapper.selectAllRouterMenu();
//        } else {
        // 获取当前用户的角色所拥有的菜单
        menuVoList = menuMapper.selectRouterMenuTreeByUserId(userId);
        //如果路由查询不到，可能角色删除了或封禁了，使得获取不到权限
        if (ObjectUtils.isEmpty(menuVoList)) {
            throw new BusinessException("无任何后台权限");
        }
//        }
        List<MenuVo> menuTree = builderMenuTree(menuVoList, 0, "");

        return menuTree;
    }

    @Override
    public List<MenuVo> selectRouterMenuTreeByRoleId(Integer roleId) {
        List<MenuVo> menuVoList = null;

//        if (roleId == 1) {
//            menuVoList = menuMapper.selectAllRouterMenu();
//        } else {
        menuVoList = menuMapper.selectRouterMenuTreeByRole(roleId);
//        }

        if (ObjectUtils.isEmpty(menuVoList)) {
            return menuVoList;
        }
        return builderMenuTree(menuVoList, 0, "");
    }

    @Override
    public List<MenuVo> selectAllRouterMenuTree() {
        List<MenuVo> menuVos = menuMapper.selectAllRouterMenu();
        return builderMenuTree(menuVos, 0, "");
    }

    @Override
    public ListVo<MenuVo> getMenuList(MenuSearchDto dto) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getDelFlag, MenuDictionary.del_enabled);
        queryWrapper.like(StringUtils.hasText(dto.getPath()), Menu::getPath, dto.getPath());
        queryWrapper.like(StringUtils.hasText(dto.getName()), Menu::getName, dto.getName());
        queryWrapper.like(StringUtils.hasText(dto.getComponent()), Menu::getComponent, dto.getComponent());
        queryWrapper.eq(StringUtils.hasText(dto.getMenuType()), Menu::getMenuType, dto.getMenuType());
        queryWrapper.eq(!ObjectUtils.isEmpty(dto.getVisible()), Menu::getVisible, dto.getVisible());
        queryWrapper.like(StringUtils.hasText(dto.getPerms()), Menu::getPerms, dto.getPerms());
        queryWrapper.like(StringUtils.hasText(dto.getRemark()), Menu::getRemark, dto.getRemark());

        IPage<Menu> page = new Page<>(dto.getCurrentPage(), dto.getPageSize());

        menuMapper.selectPage(page, queryWrapper);
        ListVo<MenuVo> listVo = new ListVo<>();

        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(page.getRecords(), MenuVo.class);
//        List<MenuVo> menuVoList = new ArrayList<>();
//        menuVos.forEach(parent -> {
//            if (parent.getParentId() != 0) {
//                //二级目录
//                menuVoList.forEach(item -> {
//                    if (Objects.equals(item.getId(), parent.getParentId())) {
//                        parent.setMeta(item.getName());
//                    }
//                });
//                parent.setMeta(parent.getMeta()+"/" + parent.getName());
//                menuVoList.add(parent);
//
//            } else {
//                parent.setMeta(parent.getName());
//                menuVoList.add(
//                        parent.setChildren(
//                                builderMenuTree(menuVos,parent.getId(),"/")
//                        ));
//            }
//        });
        listVo.setList(builderMenuTree(menuVos, 0, ""));
        listVo.setTotal(page.getTotal());

        return listVo;


    }

    @Override
    public MenuVo getMenuInfo(Integer id) {
        Menu menu = menuMapper.selectById(id);
        return BeanCopyUtils.copyBean(menu, MenuVo.class);
    }

    @Transactional
    @Override
    synchronized public void updateMenu(MenuUpdateDto dto) {

        //更新数据库
        menuMapper.updateById(BeanCopyUtils.copyBean(dto, Menu.class));
        if (dto.getVisible().equals(MenuDictionary.visible_disabled)) {

            LambdaUpdateWrapper<Menu> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Menu::getParentId, dto.getId());
            updateWrapper.set(Menu::getVisible, MenuDictionary.visible_disabled);
            menuMapper.update(null, updateWrapper);
        }
        //更新缓存
        //查找权限绑定的user
        List<Integer> ids = userRoleService.getUserIdsByMenuId(dto.getId());
        RedisString<LoginUserDetail> redis = redisUtils.getRedisString();
        ids.forEach(item -> {
            String key = UserDictionary.user_prefix + item;

            LoginUserDetail cache = redis.get(key);
            if (ObjectUtils.isEmpty(cache)) {
                return;
            }

            cache.setPermissions(menuMapper.selectMenuPermsByUserId(item));
            //缓存ttl>10秒
            if (redis.ttl(key) > 10) {
                redisUtils.getRedisString().set(
                        key,
                        cache,
                        redis.ttl(key),
                        TimeUnit.SECONDS
                );
            }
        });


    }

    @Transactional
    @Override
    synchronized public void addMenu(MenuAddDto dto) {
        //添加到数据库
        menuMapper.insert(BeanCopyUtils.copyBean(dto, Menu.class));


        // TODO: 2023/12/18 更新缓存



    }

    @Transactional
    @Override
    synchronized public void deleteMenuByIds(List<Integer> ids) {
        //删除数据库,设置delFlag
        menuMapper.update(null,
                new LambdaUpdateWrapper<Menu>()
                        .in(Menu::getId,ids)
                        .set(Menu::getDelFlag,MenuDictionary.del_disabled)
        );
        //更新role 与 menu 的关联表
        roleMenuService.remove(
                new LambdaQueryWrapper<RoleMenu>()
                        .in(RoleMenu::getMenuId,ids)
        );
        //受影响的Role
        List<RoleMenu> list = roleMenuService.list(
                new LambdaQueryWrapper<RoleMenu>()
                        .in(RoleMenu::getMenuId, ids)
        );

        //更新缓存
        if (ObjectUtils.isEmpty(list)){
            return;
        }
        userRoleService.updateCache(list.stream().map(RoleMenu::getRoleId).collect(Collectors.toList()));
    }

    private List<MenuVo> builderMenuTree(List<MenuVo> menuVoList, Integer parentId, String meta) {
        List<MenuVo> collect = menuVoList.stream().
                filter(item -> Objects.equals(item.getParentId(), parentId))
                .map(item -> {
                    item.setMeta(meta + item.getName());
                    return item.setChildren(builderMenuTree(menuVoList, item.getId(), item.getName() + "/"));
                })
                .sorted(Comparator.comparingInt(MenuVo::getOrderNum))
                .collect(Collectors.toList());
        if (collect.size() == 0) {
            return null;
        }
        return collect;
    }
}
