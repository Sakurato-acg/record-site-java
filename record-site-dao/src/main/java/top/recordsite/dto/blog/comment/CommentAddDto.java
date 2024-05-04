package top.recordsite.dto.blog.comment;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CommentAddDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(groups = CommentGroup.articleType.class, message = "文章id不能为空")
    @Positive(message = "文章id错误")
    private Integer articleId;

    @NotNull(message = "评论类型不能为空")
    private Integer type;

    @NotEmpty(message = "评论内容不能为空")
    private String content;

    @NotNull(message = "缺少根评论id")
    private Integer parentId;

    private Integer recoverId;

    private String location;

    private String ua;

}
