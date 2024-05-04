package top.recordsite.vo.blog.article;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BlogArticleEditVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String title;

    private LocalDateTime createTime;

    private List<Integer> tagIds;

    private Integer isTop;

    private Long viewCount;

    private Integer status;

    private Integer categoryId;

    private String content;

}
