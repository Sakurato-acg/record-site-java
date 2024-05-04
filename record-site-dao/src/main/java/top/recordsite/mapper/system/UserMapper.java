package top.recordsite.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.recordsite.entity.User;
import top.recordsite.vo.system.AdminUserVo;
import top.recordsite.vo.system.UserSimpleVo;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author lpl
 * @since 2023-09-25
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    public String getNickNameByIdWithCondition(@Param("id") Integer id);

    public List<AdminUserVo> getAdminUserList(
            @Param("userName") String userName,
            @Param("nickName") String nickName,
            @Param("email") String email,
            @Param("status") Integer status,
            @Param("roleName") String roleName,
            @Param("skip") Integer skip,
            @Param("limit") Integer limit);

    public long getAdminUserListCount(
            @Param("userName") String userName,
            @Param("nickName") String nickName,
            @Param("email") String email,
            @Param("status") Integer status,
            @Param("roleName") String roleName
    );

    AdminUserVo getAdminUserInfo(@Param("userId") Integer userId);

    UserSimpleVo getUserSimpleInfo(@Param("userId") Integer userId);
}
