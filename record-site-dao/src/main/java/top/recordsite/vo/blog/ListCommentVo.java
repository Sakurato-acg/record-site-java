package top.recordsite.vo.blog;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ListCommentVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<CommentVo> list;

    /**
     * 共有评论（包含一级和二级）
     */
    private Long sum;

    /**
     * 共有几条根评论
     */
    private Long total;
}
