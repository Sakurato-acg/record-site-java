package top.recordsite.vo.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class RoleSelectVo  implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String roleName;


}
