package top.recordsite.dto.system;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 菜单权限表
 * </p>
 *
 * @author lpl
 * @since 2023-09-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MenuSearchDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;


    @NotNull(message = "分页数据不能为空")
    @Positive(message = "当前页数要>0")
    private Integer currentPage;

    @NotNull(message = "pageSize不能为空")
    @PositiveOrZero(message = "pageSize不能为空")
    private Integer pageSize;

    /**
     * 路由path
     */

    private String path;

    /**
     * 菜单name
     */
    private String name;

    /**
     * 组件路径
     */
    private String component;
//
//    /**
//     * meta/title
//     */
//    private String meta;

    /**
     * 菜单类型（M目录 C菜单 F按钮）
     */
    private String menuType;

    /**
     * 菜单状态（0显示 1隐藏）
     */
    @PositiveOrZero
    private Integer visible;

    private String perms;

    /**
     * 备注
     */
    private String remark;

}
