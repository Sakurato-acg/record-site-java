package top.recordsite.vo.system;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import top.recordsite.entity.system.Role;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AdminUserVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String userName;

    private String nickName;

    private String email;

    private String avatar;

    private Integer status;

    private String url;

    private Role role;

    private LocalDateTime createTime;
}
