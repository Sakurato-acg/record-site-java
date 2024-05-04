package top.recordsite.system;

import com.baomidou.mybatisplus.extension.service.IService;
import top.recordsite.dto.system.MenuAddDto;
import top.recordsite.dto.system.MenuSearchDto;
import top.recordsite.dto.system.MenuUpdateDto;
import top.recordsite.entity.system.Menu;
import top.recordsite.vo.ListVo;
import top.recordsite.vo.system.MenuVo;

import java.util.List;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *
 * @author lpl
 * @since 2023-09-25
 */
public interface IMenuService extends IService<Menu> {

    List<MenuVo> selectRouterMenuTreeByUserId(Integer userId);

    List<MenuVo> selectRouterMenuTreeByRoleId(Integer roleId);

    List<MenuVo> selectAllRouterMenuTree();

    ListVo<MenuVo> getMenuList(MenuSearchDto dto);

    MenuVo getMenuInfo(Integer id);

    void updateMenu(MenuUpdateDto dto);

    void addMenu(MenuAddDto dto);

    void deleteMenuByIds(List<Integer> ids);
}
