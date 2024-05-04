package top.recordsite.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.recordsite.entity.system.UserRole;

import java.util.List;

/**
 * <p>
 * 用户和角色关联表 Mapper 接口
 * </p>
 *
 * @author lpl
 * @since 2023-09-25
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {


    List<Integer> getUserIdsByMenuId(@Param("id") Integer id);
}
