package top.recordsite.mapper.blog;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.recordsite.entity.blog.Comment;
import top.recordsite.vo.blog.CommentVo;

import java.util.List;

/**
 * <p>
 * 博客评论表 Mapper 接口
 * </p>
 *
 * @author lpl
 * @since 2023-11-07
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

//    List<CommentVo> getArticleParentComment(@Param("type") int type,
//                                            @Param("id") Integer id,
//                                            @Param("skip") Integer skip,
//                                            @Param("pageSize") Integer pageSize);


    List<CommentVo> getCommentList(@Param("type") int type,
                                   @Param("id") Integer id,
                                   @Param("skip") Integer skip,
                                   @Param("pageSize") Integer pageSize);

    Long getCommentListCount(@Param("type") int type,
                             @Param("id") int id,
                             @Param("parentId")Integer parentId
                             );
}
