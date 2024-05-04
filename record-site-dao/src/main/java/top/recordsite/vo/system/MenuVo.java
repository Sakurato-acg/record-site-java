package top.recordsite.vo.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MenuVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * 菜单ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 父菜单id
     */
    private Integer parentId;

    /**
     * 图标
     */
    private String icon;

    /**
     * 菜单中文名
     */
    private String name;

    /**
     * 路径
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;


    private String meta;

    /**
     * 显示数据
     */
    private Integer orderNum;

    /**
     * 权限标识
     */
    private String perms;

    private List<MenuVo> children;

    private String menuType;

    /**
     * 菜单状态（0显示 1隐藏）
     */
    private Integer visible;

}
