package top.recordsite.system;

import com.info.annotation.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.recordsite.dto.system.RoleSearchDto;
import top.recordsite.dto.system.RoleUpdateDto;
import top.recordsite.exception.BusinessException;
import top.recordsite.security.SecurityUtils;
import top.recordsite.vo.ListVo;
import top.recordsite.vo.Result;
import top.recordsite.vo.system.RoleSelectVo;
import top.recordsite.vo.system.RoleVo;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @SystemLog(businessName = "获取角色select框选项")
    @PreAuthorize("hasAnyAuthority('system:role','system:user')")
    @GetMapping("/select")
    public Result getRoleSelect() {
        List<RoleSelectVo> selectVo = roleService.getRoleSelect();
        return Result.success(selectVo);
    }

    @SystemLog(businessName = "获取后台角色列表")
    @PreAuthorize("hasAnyAuthority('system:role')")
    @PostMapping("/list")
    public Result getAdminRoleList(@RequestBody @Validated RoleSearchDto dto) {

        ListVo<RoleVo> listVo = roleService.getAdminRoleList(dto);

        return Result.success(listVo);
    }

    @SystemLog(businessName = "获取角色详情")
    @PreAuthorize("hasAnyAuthority('system:role')")
    @GetMapping("/{id}")
    public Result getRoleInfoById(@PathVariable("id") Integer id) {
        RoleVo roleVo = roleService.getRoleInfoById(id);

        return Result.success(roleVo);
    }

    @SystemLog(businessName = "修改角色")
    @PreAuthorize("hasAnyAuthority('system:role')")
    @PutMapping
    public Result updateRole(@RequestBody @Validated RoleUpdateDto dto) {
        if (SecurityUtils.getUserId()!=1) {
            throw new BusinessException("不能操作管理员角色");
        }
        roleService.updateRole(dto);

        return Result.success();
    }

    @SystemLog(businessName = "删除角色")
    @PreAuthorize("hasAnyAuthority('system:role')")
    @DeleteMapping("/{id}")
    public Result deleteUser(@PathVariable("id") @NotEmpty(message = "删除信息不符合要求") List<Integer> ids) {
//        if (ids.contains(SecurityUtils.getUserId())) {
//            return Result.error(AppHttpCodeEnum.NO_OPERATOR_AUTH, "管理员数据无法删除");
//        }
//
//        roleService.deleteUserByIds(ids);
        return Result.error(404,"暂时不开发");
    }

}
