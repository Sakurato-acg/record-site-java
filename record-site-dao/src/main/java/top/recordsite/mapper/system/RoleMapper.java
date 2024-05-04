package top.recordsite.mapper.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.recordsite.entity.system.Role;
import top.recordsite.vo.system.RoleVo;

import java.util.List;

/**
 * <p>
 * 角色信息表 Mapper 接口
 * </p>
 *
 * @author lpl
 * @since 2023-09-25
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    List<String> getUserRolePermsById(@Param("id") Integer userId);

    List<Integer> getRoleIdList(@Param("status")Integer status);

    List<RoleVo> getAdminRoleList(
            @Param(Constants.WRAPPER)LambdaQueryWrapper<Role> queryWrapper,
            Page<Role> page);

    RoleVo getRoleInfoById(
            @Param(Constants.WRAPPER)LambdaQueryWrapper<Role> queryWrapper
    );
}
