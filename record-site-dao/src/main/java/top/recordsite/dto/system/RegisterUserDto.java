package top.recordsite.dto.system;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serial;
import java.io.Serializable;

@Data
public class RegisterUserDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    @NotEmpty
    private String userName;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 邮箱
     */
    @NotEmpty
    private String email;

    /**
     * 密码
     */
    @NotEmpty
    private String password;

    private String url;
}
