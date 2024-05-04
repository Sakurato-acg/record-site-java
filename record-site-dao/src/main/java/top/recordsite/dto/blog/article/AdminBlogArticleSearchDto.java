package top.recordsite.dto.blog.article;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AdminBlogArticleSearchDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    // TODO: 2024/3/6 抽取公共分页DTO
    @NotNull
    @Positive(message = "当前页数必须为正数")
    private Integer currentPage;

    @NotNull
    @Positive(message = "pageSize必须为正数")
    private Integer pageSize;

    private String title;

    private String author;

    private Integer status;

    private Integer isTop;
//    private String content;

    private Integer categoryId;

    private Integer[] tagIds;

}
