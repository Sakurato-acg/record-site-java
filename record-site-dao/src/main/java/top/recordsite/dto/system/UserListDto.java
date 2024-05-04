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
public class UserListDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String userName;

    private String nickName;

//    @Email(message = "邮箱格式错误")
    private String email;

    @PositiveOrZero(message = "账号状态错误")
    private Integer status;

    @PositiveOrZero(message = "用户类型错误")
    private Integer type;

    private String roleName;

    @NotNull(message = "分页数据不能为空")
    @Positive(message = "当前页数要>0")
    private Integer currentPage;

    @NotNull(message = "pageSize不能为空")
    @PositiveOrZero(message = "pageSize不能为空")
    private Integer pageSize;
}
