package top.recordsite.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.recordsite.entity.system.RoleMenu;
import top.recordsite.mapper.system.RoleMenuMapper;
import top.recordsite.system.IRoleMenuService;
import top.recordsite.system.IUserRoleService;

import java.util.List;

/**
 * <p>
 * 角色和菜单关联表 服务实现类
 * </p>
 *
 * @author lpl
 * @since 2023-09-25
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements IRoleMenuService {

    @Autowired
    private IUserRoleService userRoleService;

    @Override
    public void updateCache(List<Integer> menuIds) {

//        userRoleService.updateCach();
    }
}
