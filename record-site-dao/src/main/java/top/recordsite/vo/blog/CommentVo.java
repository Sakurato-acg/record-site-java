package top.recordsite.vo.blog;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class CommentVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 评论id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer parentId;

    private Integer recoverId;

    /**
     * 文章id
     */
    private Integer articleId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 点赞
     */
    private Integer likeCount;

    /**
     * 回复的用户名
     */
    private String recoverUsername="";

    /**
     * 评论时间
     */
    private LocalDateTime createTime;

    private String createBy;

    private String avatar;

    private String url="";

    /**
     * ip地址
     */
    private String location;

    private List<CommentVo> children;

}
