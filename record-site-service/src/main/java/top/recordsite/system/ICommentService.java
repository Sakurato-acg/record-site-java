package top.recordsite.system;

import com.baomidou.mybatisplus.extension.service.IService;
import top.recordsite.dto.blog.comment.CommentAddDto;
import top.recordsite.dto.blog.comment.CommentDto;
import top.recordsite.entity.blog.Comment;
import top.recordsite.vo.blog.ListCommentVo;

/**
 * <p>
 * 博客评论表 服务类
 * </p>
 *
 * @author lpl
 * @since 2023-11-07
 */
public interface ICommentService extends IService<Comment> {

    ListCommentVo getComment(CommentDto commentDto);

    void addComment(CommentAddDto addDto);

}
