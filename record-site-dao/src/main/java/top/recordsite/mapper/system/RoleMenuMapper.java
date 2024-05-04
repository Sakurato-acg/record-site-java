package top.recordsite.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.recordsite.entity.system.RoleMenu;

/**
 * <p>
 * 角色和菜单关联表 Mapper 接口
 * </p>
 *
 * @author lpl
 * @since 2023-09-25
 */
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {


}
