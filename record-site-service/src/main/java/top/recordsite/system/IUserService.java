package top.recordsite.system;

import top.recordsite.dto.system.LoginUserDto;
import top.recordsite.dto.system.UserEditDto;
import top.recordsite.dto.system.UserListDto;
import top.recordsite.dto.system.UserUpdateDto;
import top.recordsite.entity.User;
import top.recordsite.vo.ListVo;
import top.recordsite.vo.system.AdminUserVo;
import top.recordsite.vo.system.LoginUserVo;
import top.recordsite.vo.system.MenuVo;
import top.recordsite.vo.system.UserSimpleVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lpl
 * @since 2023-09-25
 */
public interface IUserService  {

    public LoginUserVo login(LoginUserDto userDto);

    public boolean register(User user);

    public void logout();

    public List<MenuVo> getRouter();

    AdminUserVo getAdminUserInfo(Integer userId);

    ListVo<AdminUserVo> getAdminUserList(UserListDto userListDto);

    void deleteUserByIds(List<Integer> ids);

    void updateUser(UserUpdateDto dto);

    void updateUser(UserEditDto dto);

    UserSimpleVo getUserSimpleInfo();
}
