package top.recordsite.entity.blog;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 博客评论表
 * </p>
 *
 * @author lpl
 * @since 2023-11-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("blog_comment")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评论id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 评论类型（0代表文章评论,1代表‘关于我’评论）
     */
    private Integer type;

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
     * 根评论id,用于生成children
     */
    private Integer parentId;

    /**
     * 回复id,用于生成二级评论列表
     */
    private Integer recoverId;

    /**
     * 评论时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 评论者
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer createBy;

    /**
     * 删除标志（0代表未删除，1代表已删除）
     */
    private Integer delFlag;

    private String location;

    private String ua;

}
