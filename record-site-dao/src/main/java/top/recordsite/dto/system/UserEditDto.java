package top.recordsite.dto.system;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import top.recordsite.exception.Url;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ToString
public class UserEditDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "用户名不能为空")
    @Size(min = 1,max = 20)
    private String userName;

    @Size(min = 1,max = 20)
    private String nickName;

//    @NotBlank(message = "密码不能为空")
    @Size(min = 6,max = 15)
    private String password;

    @NotBlank
    @Email
    @Size(min = 1,max = 30)
    private String email;

    @NotBlank
    private String avatar;

    @Url(required = false)
    private String url;

}
