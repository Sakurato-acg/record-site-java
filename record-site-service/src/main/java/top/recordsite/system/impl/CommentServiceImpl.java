package top.recordsite.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import top.recordsite.dto.blog.comment.CommentAddDto;
import top.recordsite.dto.blog.comment.CommentDto;
import top.recordsite.entity.blog.Comment;
import top.recordsite.exception.BusinessException;
import top.recordsite.mapper.blog.BlogArticleMapper;
import top.recordsite.mapper.blog.CommentMapper;
import top.recordsite.mapper.system.UserMapper;
import top.recordsite.security.SecurityUtils;
import top.recordsite.system.ICommentService;
import top.recordsite.utils.BeanCopyUtils;
import top.recordsite.vo.blog.CommentVo;
import top.recordsite.vo.blog.ListCommentVo;

import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 博客评论表 服务实现类
 * </p>
 *
 * @author lpl
 * @since 2023-11-07
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private BlogArticleMapper articleMapper;

    @Autowired
    private UserMapper userMapper;


    @Override
    public ListCommentVo getComment(CommentDto commentDto) {

        //文章评论
        if (commentDto.getType() == 0) {
            //先判断文章是否存在
            Boolean exist = articleMapper.articleExist(commentDto.getArticleId());
            if (commentDto.getArticleId() == null || !exist) {
                throw new BusinessException("文章不存在");
            }
            //保证文章肯定存在
            Integer skip = (commentDto.getCurrentPage() - 1) * commentDto.getPageSize();
            List<CommentVo> commentList = commentMapper.getCommentList(0, commentDto.getArticleId(), skip, commentDto.getPageSize());
            ListCommentVo vo = new ListCommentVo();
            //如果文章评论数量为零
            if (ObjectUtils.isEmpty(commentList)) {
                vo.setTotal(0L);
                vo.setSum(0L);
                return vo;
            }

            //获取根评论
            //设置根评论数量
            vo.setTotal(commentMapper.getCommentListCount(0, commentDto.getArticleId(), -1));
            vo.setSum(commentMapper.getCommentListCount(0, commentDto.getArticleId(), null));
            vo.setList(buildTree(commentList, -1));

            return vo;
        } else {
            return null;
        }
    }

    @Transactional
    @Override
    public void addComment(CommentAddDto addDto) {
        //文章评论
        if (addDto.getType() == 0) {
            Boolean frontArticleById = articleMapper.articleExist(addDto.getArticleId());
            //先判断文章是否存在
            if (frontArticleById == null || !frontArticleById) {
                throw new BusinessException("文章不存在");
            }
            //获取用户信息
            Comment comment = BeanCopyUtils.copyBean(addDto, Comment.class);
            comment.setCreateBy(SecurityUtils.getUserId());

            commentMapper.insert(comment);

        }
    }

    private List<CommentVo> buildTree(List<CommentVo> commentList, Integer parentId) {
        List<CommentVo> collect = commentList.stream()
                .filter(item -> Objects.equals(item.getParentId(), parentId))
                .map(item -> item.setChildren(buildTree(commentList, item.getId())))
                .sorted((o1, o2) -> {
                    if (o1.getParentId() == -1 && o2.getParentId() == -1) {
                        return Math.toIntExact(o2.getCreateTime().toEpochSecond(ZoneOffset.UTC) - o1.getCreateTime().toEpochSecond(ZoneOffset.UTC));
                    } else {
                        return Math.toIntExact(o1.getCreateTime().toEpochSecond(ZoneOffset.UTC) - o2.getCreateTime().toEpochSecond(ZoneOffset.UTC));
                    }
                })
                .collect(Collectors.toList());
        if (collect.size() == 0) {
            return null;
        }
        return collect;
    }
}
