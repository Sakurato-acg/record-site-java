package top.recordsite.dto.system;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MenuAddDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * 父菜单id
     */
    @NotNull
    private Integer parentId;

    /**
     * 图标
     */
//    @NotNull
//    @NotBlank
    private String icon;

    /**
     * 菜单中文名
     */
    @NotNull
    @NotBlank
    private String name;

    /**
     * 路径
     */
    @NotBlank
    private String path;

    /**
     * 组件路径
     */

    private String component;


    /**
     * 显示顺序
     */
    @NotNull
    @Positive
    private Integer orderNum;

    /**
     * 权限标识
     */
    @NotBlank(message = "权限字符不能为空")
    private String perms;

    @NotNull
    @NotBlank
    private String menuType;

    /**
     * 菜单状态（0显示 1隐藏）
     */
    @NotNull
    @PositiveOrZero
    private Integer visible;

}
