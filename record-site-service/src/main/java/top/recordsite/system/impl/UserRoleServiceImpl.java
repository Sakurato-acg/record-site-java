package top.recordsite.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.recordsite.entity.system.UserRole;
import top.recordsite.enums.system.UserDictionary;
import top.recordsite.mapper.system.MenuMapper;
import top.recordsite.mapper.system.UserRoleMapper;
import top.recordsite.security.LoginUserDetail;
import top.recordsite.system.IUserRoleService;
import top.recordsite.utils.RedisUtlis.RedisString;
import top.recordsite.utils.RedisUtlis.RedisUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用户和角色关联表 服务实现类
 * </p>
 *
 * @author lpl
 * @since 2023-09-25
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private MenuMapper menuMapper;

    public void updateCache(List<Integer> roleIds) {
        //3.假如缓存中有用户数据要更新
        RedisString<LoginUserDetail> redis = redisUtils.getRedisString();
        //3.1 组装key
        List<UserRole> list = userRoleMapper.selectList(
                new LambdaQueryWrapper<UserRole>()
                        .in(UserRole::getRoleId, roleIds)
                        .select(UserRole::getUserId)
        );
        list.forEach(item -> {
            String key = UserDictionary.user_prefix + item.getUserId();
            //3.2 获取缓存中的User
            LoginUserDetail cache = redis.get(key);
            if (ObjectUtils.isEmpty(cache)) {
                return;
            }
            //3.4 设置更新后的权限
            cache.setPermissions(menuMapper.selectMenuPermsByUserId(item.getUserId()));
            //3.5 更新缓存，继承过期时间
            if (redis.ttl(key) > 1) {
                redisUtils.getRedisString().set(
                        key,
                        cache,
                        redis.ttl(key),
                        TimeUnit.SECONDS
                );
            }
        });


    }

    @Override
    public List<Integer> getUserIdsByMenuId(Integer menuId) {

        return userRoleMapper.getUserIdsByMenuId(menuId);
    }
}
