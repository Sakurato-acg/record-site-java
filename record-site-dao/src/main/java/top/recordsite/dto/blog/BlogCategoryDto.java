package top.recordsite.dto.blog;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class BlogCategoryDto implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    private Integer status;
}
