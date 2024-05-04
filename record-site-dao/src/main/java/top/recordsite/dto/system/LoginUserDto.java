package top.recordsite.dto.system;


import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serial;
import java.io.Serializable;

@Data
public class LoginUserDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户名或邮箱
     */

    @NotEmpty
    private String account;

    /**
     * 密码
     */
    @NotEmpty
    private String password;


}
