package top.recordsite.system;

import com.baomidou.mybatisplus.extension.service.IService;
import top.recordsite.dto.system.RoleSearchDto;
import top.recordsite.dto.system.RoleUpdateDto;
import top.recordsite.entity.system.Role;
import top.recordsite.vo.ListVo;
import top.recordsite.vo.system.RoleSelectVo;
import top.recordsite.vo.system.RoleVo;

import java.util.List;

/**
 * <p>
 * 角色信息表 服务类
 * </p>
 *
 * @author lpl
 * @since 2023-09-25
 */

public interface IRoleService extends IService<Role> {

    List<RoleSelectVo> getRoleSelect();

    ListVo<RoleVo> getAdminRoleList(RoleSearchDto dto);

    RoleVo getRoleInfoById(Integer id);

    void updateRole(RoleUpdateDto dto);

}
