package top.recordsite.dto.blog.article;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FrontBlogArticleListDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    @PositiveOrZero
    private Integer currentPage;

    @NotNull
    @PositiveOrZero
    private Integer pageSize;

    private String categoryName;

    private String tagName;

}
