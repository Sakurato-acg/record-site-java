package top.recordsite.dto.blog.comment;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CommentDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 评论类型
     */
    @NotNull
    @PositiveOrZero
    private Integer type;

    private Integer articleId;

    @NotNull
    @Positive
    private Integer currentPage;

    @NotNull
    @PositiveOrZero
    private Integer pageSize;
}
