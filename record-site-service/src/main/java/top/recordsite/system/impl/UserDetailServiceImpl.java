package top.recordsite.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import top.recordsite.entity.User;
import top.recordsite.enums.system.UserDictionary;
import top.recordsite.exception.BusinessException;
import top.recordsite.mapper.system.MenuMapper;
import top.recordsite.mapper.system.UserMapper;
import top.recordsite.security.LoginUserDetail;

import java.util.List;
import java.util.Objects;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        if (account.contains("@")) {
            //account为邮箱
            queryWrapper.eq(User::getEmail, account);
        } else {
            //account为用户名
            queryWrapper.eq(User::getUserName, account);
        }
        queryWrapper.eq(User::getDelFlag, UserDictionary.del_enabled);
        //获取用户
        User user = userMapper.selectOne(queryWrapper);
        //用户不存在
        if (Objects.isNull(user)) {
            //用户名不存在异常
            throw new UsernameNotFoundException("用户不存在或被删除");
        }
        //用户已封禁
        if (user.getStatus() == 1) {
            //用户封禁则不能登录
            throw new BusinessException("用户封禁");
        }
        //创建UserDetail
        LoginUserDetail loginUser = new LoginUserDetail();

        // TODO: 2023/12/6 根据user的Id,查询该用户权限(menus的perms)
//        List<String> perms=roleMapper.getUserRolePermsById(user.getId());
        List<String> perms = menuMapper.selectMenuPermsByUserId(user.getId());
        loginUser.setPermissions(perms);

        loginUser.setUser(user);

        return loginUser;
    }
}
