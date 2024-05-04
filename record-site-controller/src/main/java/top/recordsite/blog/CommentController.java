package top.recordsite.blog;


import com.info.annotation.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.recordsite.dto.blog.comment.CommentAddDto;
import top.recordsite.dto.blog.comment.CommentDto;
import top.recordsite.dto.blog.comment.CommentGroup;
import top.recordsite.system.ICommentService;
import top.recordsite.vo.Result;
import top.recordsite.vo.blog.ListCommentVo;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 博客评论表 前端控制器
 * </p>
 *
 * @author lpl
 * @since 2023-11-07
 */
@Slf4j
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private ICommentService commentService;

    @SystemLog(businessName = "获取评论")
    @PostMapping
    public Result getComment(@RequestBody @Validated CommentDto commentDto) {

        ListCommentVo list = commentService.getComment(commentDto);
        return Result.success(list);
    }

    @SystemLog(businessName = "添加评论")
    @PostMapping("/add")
    public Result addComment(
            @RequestBody
            @Validated(value = {CommentGroup.articleType.class}) CommentAddDto addDto,
            HttpServletRequest request) {

        if (addDto.getParentId() != -1 && ObjectUtils.isEmpty(addDto.getRecoverId())) {
            return Result.error(400, "缺少回复者");
        }

        String ua = request.getHeader("user-Agent");
        addDto.setUa(ua);
        commentService.addComment(addDto);

        return Result.success();
    }


}

