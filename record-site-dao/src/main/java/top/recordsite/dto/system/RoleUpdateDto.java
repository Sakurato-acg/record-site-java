package top.recordsite.dto.system;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 角色信息表
 * </p>
 *
 * @author lpl
 * @since 2023-09-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RoleUpdateDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 角色id
     */
    @NotNull
    @PositiveOrZero
    private Integer id;

    /**
     * 角色名称
     */
    @NotNull
    @NotEmpty
    private String roleName;

    /**
     * 角色权限字符串
     */
    @NotNull
    @NotEmpty
    private String roleKey;

    /**
     * 角色状态（0 正常 1停用）
     */
    @NotNull
    @PositiveOrZero
    private Integer status;


    /**
     * 备注
     */
    private String remark;


    @Size
    private List<Integer> menuList;

}
