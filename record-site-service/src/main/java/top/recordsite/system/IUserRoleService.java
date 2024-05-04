package top.recordsite.system;

import com.baomidou.mybatisplus.extension.service.IService;
import top.recordsite.entity.system.UserRole;

import java.util.List;

/**
 * <p>
 * 用户和角色关联表 服务类
 * </p>
 *
 * @author lpl
 * @since 2023-09-25
 */
public interface IUserRoleService extends IService<UserRole> {

    public void updateCache(List<Integer> roleIds);

    public List<Integer> getUserIdsByMenuId(Integer menuId);
}
