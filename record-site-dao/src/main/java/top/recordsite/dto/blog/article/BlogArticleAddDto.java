package top.recordsite.dto.blog.article;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BlogArticleAddDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    private String content;

    private Integer id;

    @NotNull
    private Integer isTop;

    @NotNull
    private Integer status;

    @NotNull
    private List<Object> tagIds;

    @NotNull
    private String title;

    @NotNull
    private Integer categoryId;

    private List<Long> fileList;


}
