package top.recordsite.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import top.recordsite.dto.system.RoleSearchDto;
import top.recordsite.dto.system.RoleUpdateDto;
import top.recordsite.entity.system.Role;
import top.recordsite.entity.system.RoleMenu;
import top.recordsite.enums.system.RoleDictionary;
import top.recordsite.exception.BusinessException;
import top.recordsite.mapper.system.RoleMapper;
import top.recordsite.system.IMenuService;
import top.recordsite.system.IRoleMenuService;
import top.recordsite.system.IRoleService;
import top.recordsite.system.IUserRoleService;
import top.recordsite.utils.BeanCopyUtils;
import top.recordsite.vo.ListVo;
import top.recordsite.vo.system.RoleSelectVo;
import top.recordsite.vo.system.RoleVo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色信息表 服务实现类
 * </p>
 *
 * @author lpl
 * @since 2023-09-25
 */
@Service
@EnableAspectJAutoProxy(exposeProxy = true)
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private IMenuService menuService;

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private IRoleMenuService roleMenuService;

    @Override
    public List<RoleSelectVo> getRoleSelect() {
        //组装查询条件
        LambdaQueryWrapper<Role> roleQueryWrapper = new LambdaQueryWrapper<>();
        roleQueryWrapper.select(Role::getId, Role::getRoleName);
        roleQueryWrapper.eq(Role::getDelFlag, RoleDictionary.del_enabled);
//        roleQueryWrapper.eq(Role::getStatus, RoleDictionary.status_enabled);
        //查询
        List<Role> roles = roleMapper.selectList(roleQueryWrapper);
        List<RoleSelectVo> selectVos = BeanCopyUtils.copyBeanList(roles, RoleSelectVo.class);

        return selectVos;


//        //2 查询 角色菜单关联表的情况
//        LambdaQueryWrapper<RoleMenu> roleMenuQueryWrapper = new LambdaQueryWrapper<>();
//        roleMenuQueryWrapper.eq(RoleMenu::getRoleId,1);
//        roleMenuMapper.selectCount()
//        //2.1 查不到关联信息，设置角色状态为1
//        //2.2 查询得到，角色关联权限正常

    }

    @Override
    public ListVo<RoleVo> getAdminRoleList(RoleSearchDto dto) {
        Page<Role> page = new Page<>(dto.getCurrentPage(), dto.getPageSize());

        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(dto.getRoleName()), Role::getRoleName, dto.getRoleName());
        queryWrapper.like(StringUtils.hasText(dto.getRoleKey()), Role::getRoleKey, dto.getRoleKey());
        queryWrapper.eq(!ObjectUtils.isEmpty(dto.getStatus()), Role::getStatus, dto.getStatus());
        queryWrapper.eq(Role::getDelFlag, RoleDictionary.del_enabled);
        queryWrapper.select(
                Role::getId,
                Role::getRoleName,
                Role::getRoleKey,
                Role::getStatus,
                Role::getRemark
        );

        List<RoleVo> list = roleMapper.getAdminRoleList(queryWrapper, page);
        list = list.stream().map(roleVo -> {
            return roleVo.setMenuList(menuService.selectRouterMenuTreeByRoleId(roleVo.getId()));
        }).toList();

        ListVo<RoleVo> listVo = new ListVo<>();
        listVo.setList(list);
        listVo.setTotal(page.getTotal());

        return listVo;
    }

    @Override
    public RoleVo getRoleInfoById(Integer id) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(!ObjectUtils.isEmpty(id), Role::getId, id);
        queryWrapper.eq(Role::getDelFlag, RoleDictionary.del_enabled);
        queryWrapper.select(
                Role::getId,
                Role::getRoleName,
                Role::getRoleKey,
                Role::getStatus,
                Role::getRemark
        );


        RoleVo roleVo = roleMapper.getRoleInfoById(queryWrapper);
        roleVo.setMenuList(menuService.selectAllRouterMenuTree());


        return roleVo;
    }

    @Transactional
    @Override
    synchronized public void updateRole(RoleUpdateDto dto) {
        List<Integer> roleIdList = roleMapper.getRoleIdList(null);
        if (!roleIdList.contains(dto.getId())) {
            throw new BusinessException("角色不存在，无法更新");
        }

        //更新角色表
        Role role = BeanCopyUtils.copyBean(dto, Role.class);
        LambdaUpdateWrapper<Role> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Role::getId, dto.getId());
        roleMapper.update(role, updateWrapper);


        //更新role_menu关联表
        List<RoleMenu> list = roleMenuService.list(
                new LambdaQueryWrapper<RoleMenu>()
                        .eq(RoleMenu::getRoleId, dto.getId())
                        .select(RoleMenu::getMenuId)
        );
        //menuIds
        List<Integer> ids = list.stream().map(RoleMenu::getMenuId).collect(Collectors.toList());
        if (ids.equals(dto.getMenuList())) {
            //没有变 role 与 menu 的关联 ，只改了 role本身
//            IRoleService proxy = (IRoleService) AopContext.currentProxy();
//            proxy.updateCache(role);
            return;
        }
        //合集，不用删
        List<Integer> inter = dto.getMenuList().stream().filter(ids::contains).collect(Collectors.toList());
        //找数据库的差集
        List<Integer> deleteIds = ids.stream().filter(item -> !inter.contains(item)).collect(Collectors.toList());
        //找添加的差集
        List<Integer> insertIds = dto.getMenuList().stream().filter(item -> !inter.contains(item)).collect(Collectors.toList());

        //先删除提交中id数组中没有的id
        if (!ObjectUtils.isEmpty(deleteIds)) {
            roleMenuService.remove(
                    new LambdaQueryWrapper<RoleMenu>()
                            .in(RoleMenu::getMenuId, deleteIds)
                            .eq(RoleMenu::getRoleId, dto.getId())
            );
        }
        //后更新
        insertIds.forEach((id) -> {
            roleMenuService.save(
                    new RoleMenu()
                            .setRoleId(dto.getId())
                            .setMenuId(id)
            );
        });


        userRoleService.updateCache(List.of(role.getId()));
//        //更新索引
//        roleMenuMapper.update(
//                null,
//                new LambdaUpdateWrapper<RoleMenu>()
//                        .setSql(" SET @i = 0;" +
//                                "        UPDATE sys_role_menu" +
//                                "        SET `id`=(@i := @i + 1)" +
//                                "        where id <= ANY_VALUE(id);" +
//                                "        ALTER TABLE sys_user" +
//                                "        AUTO_INCREMENT = 0;")
//
//                );

    }


//    @Override
//    public void deleteUserByIds(List<Integer> ids) {
//        //逻辑删除
//        LambdaUpdateWrapper<Role> updateWrapper = new LambdaUpdateWrapper<>();
//        updateWrapper.in(!ObjectUtils.isEmpty(ids), Role::getId, ids);
//        updateWrapper.set(Role::getDelFlag, 1);
//
//        int update = roleMapper.update(null, updateWrapper);
//        if (update == 0) {
//            throw new BusinessException("删除用户失败");
//        }
//
//        //更新用户角色表
//        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.in(!ObjectUtils.isEmpty(ids), UserRole::getUserId, ids);
//
//        int delete = userRoleMapper.delete(queryWrapper);
//        if (delete == 0) {
//            throw new BusinessException("删除用户失败");
//        }
//    }
}
