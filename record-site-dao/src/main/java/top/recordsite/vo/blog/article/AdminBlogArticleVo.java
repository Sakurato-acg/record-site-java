package top.recordsite.vo.blog.article;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import top.recordsite.entity.User;
import top.recordsite.entity.blog.BlogCategory;
import top.recordsite.entity.blog.BlogTag;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AdminBlogArticleVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String title;

    private User author;

    private LocalDateTime updateTime;

    private List<BlogTag> tags;

    private Integer isTop;

    private Long viewCount;

    private Integer status;

    private BlogCategory category;

    private String content;
}
