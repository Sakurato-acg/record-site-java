package top.recordsite.vo.system;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserSimpleVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String nickName;

    private String avatar;
}
