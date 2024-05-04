package top.recordsite.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.recordsite.entity.system.Menu;
import top.recordsite.vo.system.MenuVo;

import java.util.List;

/**
 * <p>
 * 菜单权限表 Mapper 接口
 * </p>
 *
 * @author lpl
 * @since 2023-09-25
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    List<MenuVo> selectAllRouterMenu();

    List<MenuVo> selectRouterMenuTreeByUserId(@Param("userId") Integer userId);

    List<String> selectMenuPermsByUserId(@Param("userId") Integer userId);

    List<MenuVo> selectRouterMenuTreeByRole(@Param("roleId") Integer roleId);

}
