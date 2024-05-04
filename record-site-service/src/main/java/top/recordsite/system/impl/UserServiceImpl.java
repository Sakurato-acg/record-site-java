package top.recordsite.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import top.recordsite.dto.system.LoginUserDto;
import top.recordsite.dto.system.UserEditDto;
import top.recordsite.dto.system.UserListDto;
import top.recordsite.dto.system.UserUpdateDto;
import top.recordsite.entity.User;
import top.recordsite.entity.blog.BlogArticle;
import top.recordsite.entity.blog.Comment;
import top.recordsite.entity.system.UserRole;
import top.recordsite.enums.system.CommonDictionary;
import top.recordsite.enums.system.UserDictionary;
import top.recordsite.exception.BusinessException;
import top.recordsite.exception.SystemException;
import top.recordsite.mapper.blog.BlogArticleMapper;
import top.recordsite.mapper.blog.CommentMapper;
import top.recordsite.mapper.system.MenuMapper;
import top.recordsite.mapper.system.RoleMapper;
import top.recordsite.mapper.system.UserMapper;
import top.recordsite.mapper.system.UserRoleMapper;
import top.recordsite.security.LoginUserDetail;
import top.recordsite.security.SecurityUtils;
import top.recordsite.system.IMenuService;
import top.recordsite.system.IUserService;
import top.recordsite.utils.BeanCopyUtils;
import top.recordsite.utils.JwtUtils;
import top.recordsite.utils.RedisUtlis.RedisString;
import top.recordsite.utils.RedisUtlis.RedisUtils;
import top.recordsite.vo.AppHttpCodeEnum;
import top.recordsite.vo.ListVo;
import top.recordsite.vo.system.AdminUserVo;
import top.recordsite.vo.system.LoginUserVo;
import top.recordsite.vo.system.MenuVo;
import top.recordsite.vo.system.UserSimpleVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lpl
 * @since 2023-09-25
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisUtils<LoginUserDetail> redisUtils;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IMenuService menuService;

    @Autowired
    private BlogArticleMapper articleMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private MenuMapper menuMapper;


    public static void setUserAvatar(User user) {
        String email = user.getEmail();
        if (email.substring(email.indexOf('@') + 1, email.indexOf('@') + 3).equals("qq")) {
            user.setAvatar("https://q2.qlogo.cn/headimg_dl?dst_uin=" + email + "&spec=160");
        }
    }

    @Override
    public LoginUserVo login(LoginUserDto userDto) {
        //把用户信息封装成Authentication
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDto.getAccount(), userDto.getPassword());

        //从数据里查信息,校验
        Authentication userDetail = authenticationManager.authenticate(authenticationToken);

        //判断认证是否通过
        if (Objects.isNull(userDetail)) {
            throw new BusinessException("认证失败");
        }

        //认证通过,生成JWT,存入redis
        LoginUserDetail loginUserDetail = (LoginUserDetail) userDetail.getPrincipal();
        String userId = loginUserDetail.getUser().getId().toString();
        redisUtils.getRedisString().set(UserDictionary.user_prefix + userId, loginUserDetail, 7, TimeUnit.DAYS);

        String jwt = JwtUtils.createJwt(userId);

        return new LoginUserVo().setToken(jwt);

    }

    @Override
    public boolean register(User user) {
        // TODO: 2024/1/30 insert可优化
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();

        //用户注册时，（昵称、邮箱）不能与现有的重复
        queryWrapper.eq(User::getEmail, user.getEmail());
        queryWrapper.or().eq(User::getUserName, user.getUserName());
        queryWrapper.or().eq(StringUtils.hasText(user.getNickName()), User::getNickName, user.getNickName());

        List<User> selectList = userMapper.selectList(queryWrapper);

        //用户存在
        if (!ObjectUtils.isEmpty(selectList)) {
            //1.用户已注册，系统中已有信息
            return false;
        }
        //用户不存在，则可以创建
        if (!StringUtils.hasText(user.getNickName())) {
            user.setNickName(user.getUserName());
        }

        setUserAvatar(user);
        userMapper.insert(user.setPassword(passwordEncoder.encode(user.getPassword())));
        //配置权限--普通用户
        UserRole userRole = new UserRole().setUserId(user.getId()).setRoleId(UserDictionary.Role_Common);
        userRoleMapper.insert(userRole);
        return true;
    }

    @Override
    public void logout() {
        Integer userId = SecurityUtils.getUserId();
        redisUtils.del(UserDictionary.user_prefix + userId);
    }

    @Override
    public List<MenuVo> getRouter() {
        //Security保证了这里是登录之后的用户
        Integer userId = SecurityUtils.getUserId();
        List<MenuVo> menuVoList = menuService.selectRouterMenuTreeByUserId(userId);
        return menuVoList;
    }

    @Override
    public AdminUserVo getAdminUserInfo(Integer userId) {
        AdminUserVo selectOne = userMapper.getAdminUserInfo(userId);
        if (ObjectUtils.isEmpty(selectOne)) {
            //用户不存在（已删除、已封禁）
            throw new BusinessException(AppHttpCodeEnum.NO_OPERATOR_AUTH).setMsg("用户不存在（已删除、已封禁）");
        }
        return selectOne;
    }

    @Override
    public ListVo<AdminUserVo> getAdminUserList(UserListDto dto) {
        Integer skip = (dto.getCurrentPage() - 1) * dto.getPageSize();
        Integer limit = dto.getPageSize();
        List<AdminUserVo> list = userMapper.getAdminUserList(
                dto.getUserName(),
                dto.getNickName(),
                dto.getEmail(),
                dto.getStatus(),
                dto.getRoleName(),
                skip,
                limit
        );
        ListVo<AdminUserVo> listVo = new ListVo<>();
        listVo.setList(list);
        long size = userMapper.getAdminUserListCount(
                dto.getUserName(),
                dto.getNickName(),
                dto.getEmail(),
                dto.getStatus(),
                dto.getRoleName()
        );
        listVo.setTotal(size);

        return listVo;
    }

    @Transactional
    @Override
    public void deleteUserByIds(List<Integer> ids) {

        //删除用户，逻辑删除
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(!ObjectUtils.isEmpty(ids), User::getId, ids);
        updateWrapper.set(User::getDelFlag, CommonDictionary.del_disabled);

        int update = userMapper.update(null, updateWrapper);
        if (update == 0) {
            throw new BusinessException("删除用户失败");
        }

        //封禁文章
        articleMapper.update(
                null,
                new LambdaUpdateWrapper<BlogArticle>()
                        .in(BlogArticle::getCreateBy, ids)
                        .set(BlogArticle::getDelFlag, CommonDictionary.del_disabled)
        );
        //封禁评论
        commentMapper.update(
                null,
                new LambdaUpdateWrapper<Comment>()
                        .in(Comment::getCreateBy, ids)
                        .set(Comment::getDelFlag, CommonDictionary.del_disabled)
        );

        //更新用户角色表
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(!ObjectUtils.isEmpty(ids), UserRole::getUserId, ids);

        int delete = userRoleMapper.delete(queryWrapper);
        if (delete == 0) {
            throw new BusinessException("删除用户失败");
        }

        //强制下线
        List<String> delKeys = new ArrayList<>();
        ids.forEach(item -> {
            delKeys.add(UserDictionary.user_prefix + item);
        });
        redisUtils.del(delKeys);

    }

    @Transactional
    @Override
    public void updateUser(UserUpdateDto dto) {
        //1.组装set条件 set user_id 需要更新的id
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId, dto.getId());
//        updateWrapper.set(User::getUserName, dto.getUserName());
//        updateWrapper.set(User::getEmail, dto.getEmail());
//        updateWrapper.set(User::getAvatar, dto.getAvatar());
//        updateWrapper.set(User::getStatus, dto.getStatus());
//        updateWrapper.set(StringUtils.hasText(dto.getUrl()), User::getUrl, dto.getUrl());
//        updateWrapper.set(User::getUpdateTime, LocalDateTime.now());
//        updateWrapper.set(User::getUpdateBy, SecurityUtils.getUserId());
        User insert = BeanCopyUtils.copyBean(dto, User.class);
        int user = userMapper.update(insert, updateWrapper);


        if (user <= 0) {
            throw new SystemException("用户更新失败");
        }

        //2.roleId需要存在
        List<Integer> roleIdList = roleMapper.getRoleIdList(CommonDictionary.status_able);
        if (!roleIdList.contains(dto.getRoleId())) {
            throw new BusinessException("角色不存在");
        }

        int userRole = userRoleMapper.update(null,
                new LambdaUpdateWrapper<UserRole>()
                        .eq(UserRole::getUserId, dto.getId())
                        .set(UserRole::getRoleId, dto.getRoleId())
        );
        if (userRole <= 0) {
            throw new SystemException("用户更新失败");
        }

        //3.假如缓存中有用户数据要更新
        RedisString<LoginUserDetail> redis = redisUtils.getRedisString();
        //3.1 组装key
        String key = UserDictionary.user_prefix + dto.getId();
        //3.2 获取缓存中的User
        LoginUserDetail cache = redis.get(key);
        // TODO: 2023/11/21 有可能获取缓存后，用户下线了，缓存没了，但是用户下次登录时会刷新cache
        if (ObjectUtils.isEmpty(cache)) {
            return;
        }
        //3.3 设置更新后的User
        cache.setUser(userMapper.selectById(dto.getId()));
        //3.4 设置更新后的权限
        cache.setPermissions(menuMapper.selectMenuPermsByUserId(dto.getId()));
        //3.5 更新缓存，继承过期时间
        if (redis.ttl(key) > 1) {
            redisUtils.getRedisString().set(
                    key,
                    cache,
                    redis.ttl(key),
                    TimeUnit.SECONDS
            );
        }else {
            redisUtils.getRedisString().set(
                    key,
                    cache,
                    7,
                    TimeUnit.DAYS
            );
        }


    }

    @Transactional
    @Override
    public void updateUser(UserEditDto dto) {
        User user = BeanCopyUtils.copyBean(dto, User.class);
        if (!ObjectUtils.isEmpty(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        user.setId(SecurityUtils.getUserId());
        userMapper.updateById(user);

        //3.假如缓存中有用户数据要更新
        RedisString<LoginUserDetail> redis = redisUtils.getRedisString();
        //3.1 组装key
        String key = UserDictionary.user_prefix + user.getId();
        //3.2 获取缓存中的User
        LoginUserDetail cache = redis.get(key);
        // TODO: 2023/11/21 有可能获取缓存后，用户下线了，缓存没了，但是用户下次登录时会刷新cache
        if (ObjectUtils.isEmpty(cache)) {
            return;
        }
        //3.3 设置更新后的User
        cache.setUser(userMapper.selectById(user.getId()));
        //3.5 更新缓存，继承过期时间
        if (redis.ttl(key) > 1) {
            redisUtils.getRedisString().set(
                    key,
                    cache,
                    redis.ttl(key),
                    TimeUnit.SECONDS
            );
        }

    }

    @Override
    public UserSimpleVo getUserSimpleInfo() {
        UserSimpleVo userSimpleVo=userMapper.getUserSimpleInfo(SecurityUtils.getUserId());
        if (ObjectUtils.isEmpty(userSimpleVo)) {
            //用户不存在（已删除、已封禁）
            throw new BusinessException(AppHttpCodeEnum.NO_OPERATOR_AUTH).setMsg("用户不存在（已删除、已封禁）");
        }
        return userSimpleVo;
    }
}
