package top.recordsite.system;


import com.info.annotation.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.recordsite.dto.system.*;
import top.recordsite.entity.User;
import top.recordsite.exception.BusinessException;
import top.recordsite.security.SecurityUtils;
import top.recordsite.utils.BeanCopyUtils;
import top.recordsite.vo.AppHttpCodeEnum;
import top.recordsite.vo.ListVo;
import top.recordsite.vo.Result;
import top.recordsite.vo.system.AdminUserVo;
import top.recordsite.vo.system.LoginUserVo;
import top.recordsite.vo.system.MenuVo;
import top.recordsite.vo.system.UserSimpleVo;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lpl
 * @since 2023-09-25
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/login")
    @SystemLog(businessName = "登录")
    public Result login(@RequestBody @Validated LoginUserDto userDto) {

        LoginUserVo loginUserVo = userService.login(userDto);
        return Result.success("登录成功", loginUserVo);
    }


    @PutMapping("/register")
    @SystemLog(businessName = "注册")
    public Result register(@RequestBody @Validated RegisterUserDto userDto) {
        boolean success = userService.register(BeanCopyUtils.copyBean(userDto, User.class));

        return success ? Result.success("注册成功") : Result.error(AppHttpCodeEnum.BUSINESS).setMsg("用户名或邮箱已存在");
    }

    @DeleteMapping("/logout")
    @SystemLog(businessName = "退出登录")
    public Result logout() {
        userService.logout();
        return Result.success("退出登录成功");
    }

    @GetMapping("/router")
    @SystemLog(businessName = "获取路由")
    public Result getRouter() {
        //获取路由时，不需要鉴权，只要认证过了都能访问
        List<MenuVo> router = userService.getRouter();
        return Result.success(router);
    }

    @SystemLog(businessName = "获取用户信息")
    @GetMapping("/admin")
    public Result getAdminUserInfo(@RequestParam(required = false) Integer id) {
        AdminUserVo adminUserVo = null;
        //修改时id存在
        if (id == null) {
            adminUserVo = userService.getAdminUserInfo(SecurityUtils.getUserId());
        } else {
            adminUserVo = userService.getAdminUserInfo(id);
        }
        return Result.success(adminUserVo);
    }

    @SystemLog(businessName = "获取header用户信息")
    @GetMapping("/header")
    public Result getUserHeaderInfo() {
        UserSimpleVo simpleVo = userService.getUserSimpleInfo();
        return Result.success(simpleVo);
    }

    @SystemLog(businessName = "获取后台用户列表")
    @PreAuthorize("hasAnyAuthority('system:user')")
    @PostMapping("/admin/list")
    public Result getAdminUserList(@RequestBody @Validated UserListDto userListDto) {

        ListVo<AdminUserVo> adminUserList = userService.getAdminUserList(userListDto);
        if (adminUserList.getTotal().equals(0L)) {
            return Result.error(AppHttpCodeEnum.BUSINESS, "用户列表查询为空", adminUserList);
        }

        return Result.success("用户列表加载成功", adminUserList);
    }

    @SystemLog(businessName = "删除用户")
    @PreAuthorize("hasAnyAuthority('system:user')")
    @DeleteMapping("/{id}")
    public Result deleteUser(@PathVariable("id") @NotEmpty(message = "删除信息不符合要求") List<Integer> ids) {
        if (ids.contains(SecurityUtils.getUserId())) {
            return Result.error(AppHttpCodeEnum.NO_OPERATOR_AUTH, "管理员数据无法删除");
        }

        userService.deleteUserByIds(ids);
        return Result.success();
    }

    @SystemLog(businessName = "更新用户")
    @PreAuthorize("hasAnyAuthority('system:user')")
    @PutMapping
    public Result updateUser(@RequestBody @Validated UserUpdateDto dto) {
        if (dto.getId() == 1 && (dto.getRoleId() != 1 || dto.getStatus() == 1)) {
            throw new BusinessException("不能操作管理员角色和状态");
        }
        userService.updateUser(dto);

        return Result.success("更新用户" + dto.getNickName() + "成功");

    }

    @SystemLog(businessName = "更新用户,用户信息维护页面")
    @PreAuthorize("hasAnyAuthority('user:edit')")
    @PutMapping("/edit")
    public Result updateUserSelf(@RequestBody @Validated UserEditDto dto) {
        userService.updateUser(dto);
        return Result.success("更新用户" + dto.getNickName() + "成功");
    }

}
// TODO: 2024/3/13 前端路由改成computed

