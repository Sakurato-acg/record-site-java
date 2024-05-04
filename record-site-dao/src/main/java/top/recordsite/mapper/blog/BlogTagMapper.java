package top.recordsite.mapper.blog;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.recordsite.dto.blog.AdminBlogTagSearchDto;
import top.recordsite.entity.blog.BlogTag;
import top.recordsite.vo.blog.BlogTagCountVo;

import java.util.List;

/**
 * <p>
 * 博客标签表 Mapper 接口
 * </p>
 *
 * @author lpl
 * @since 2023-10-23
 */
@Mapper
public interface BlogTagMapper extends BaseMapper<BlogTag> {

    //    List<FrontBlogArticleTagVo> getTagByArticleIdWithCondition(@Param("id") Integer id);
//
    List<BlogTag> getFrontTagList(@Param("pageSize") Integer pageSize, @Param("categoryName") String categoryName);

    Integer getTagListCount(@Param("status") Integer status, @Param("categoryName") String categoryName );

    List<BlogTag> getTagSelectList();

    List<BlogTagCountVo> getAdminTagList(
            @Param("skip") Integer skip,
            @Param("limit") Integer limit,
            @Param("dto") AdminBlogTagSearchDto searchDto
    );

    BlogTag getTagById(@Param("id") Integer id);
}
