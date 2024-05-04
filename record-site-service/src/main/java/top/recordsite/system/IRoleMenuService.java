package top.recordsite.system;

import com.baomidou.mybatisplus.extension.service.IService;
import top.recordsite.entity.system.RoleMenu;

import java.util.List;

/**
 * <p>
 * 角色和菜单关联表 服务类
 * </p>
 *
 * @author lpl
 * @since 2023-09-25
 */
public interface IRoleMenuService extends IService<RoleMenu> {

    public void updateCache(List<Integer> menuIds);
}
