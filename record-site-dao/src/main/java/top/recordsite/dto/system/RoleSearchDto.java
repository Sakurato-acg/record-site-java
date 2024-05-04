package top.recordsite.dto.system;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode
@Accessors(chain = true)
public class RoleSearchDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "分页数据不能为空")
    @Positive(message = "当前页数要>0")
    private Integer currentPage;

    @NotNull(message = "pageSize不能为空")
    @PositiveOrZero(message = "pageSize不能为空")
    private Integer pageSize;


    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色权限标识
     */
    private String roleKey;

    /**
     * 角色状态（0正常 1停用）
     */
    @PositiveOrZero
    private Integer status;


}
