package top.recordsite.vo.blog.article;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import top.recordsite.entity.User;
import top.recordsite.entity.blog.BlogCategory;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FrontBlogArticleMapperVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String title;

    private User author;

    private LocalDateTime createTime;

    private String tagIds;

    private String tagNames;

    private Integer isTop;

    private Long viewCount;

    private BlogCategory category;

    private String content;

}
