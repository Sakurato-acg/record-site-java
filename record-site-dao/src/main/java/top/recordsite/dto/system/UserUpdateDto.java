package top.recordsite.dto.system;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import top.recordsite.exception.Url;

import javax.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ToString
public class UserUpdateDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "用户id不能为空")
    @Positive(message = "用户id错误")
    private Integer id;

    @NotBlank(message = "用户名不能为空")
    @Size(min = 1,max = 10)
    private String userName;

    @Size(min = 1,max = 10)
    private String nickName;

    @NotBlank
    @Email
    @Size(min = 1,max = 30)
    private String email;

    @NotBlank
    private String avatar;

    @NotNull
    @PositiveOrZero
    private Integer status;

    @Url(required = false)
    private String url;

    @NotNull
    @Positive
    private Integer roleId;

}
