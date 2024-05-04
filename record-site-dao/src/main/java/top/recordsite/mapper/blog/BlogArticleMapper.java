package top.recordsite.mapper.blog;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.recordsite.dto.blog.article.AdminBlogArticleSearchDto;
import top.recordsite.dto.blog.article.FrontBlogArticleListDto;
import top.recordsite.entity.blog.BlogArticle;
import top.recordsite.vo.blog.article.AdminBlogArticleVo;
import top.recordsite.vo.blog.article.FrontBlogArticleMapperVo;
import top.recordsite.vo.blog.article.FrontBlogArticleVo;

import java.util.List;

/**
 * <p>
 * 博客文章表 Mapper 接口
 * </p>
 *
 * @author lpl
 * @since 2023-10-23
 */
@Mapper
public interface BlogArticleMapper extends BaseMapper<BlogArticle> {


//    List<BlogArticle> selectByPage(@Param("skip") Integer skip, @Param("limit") Integer limit);


    FrontBlogArticleVo getFrontArticleById(@Param("id") Integer id);

//    List<BlogArticle> getFrontArticleByPageWithCondition(@Param("skip") Integer skip, @Param("limit") Integer limit);

    Long getFrontArticleCountWithCondition(@Param("dto") FrontBlogArticleListDto articleListDto);

    List<FrontBlogArticleMapperVo> getFrontArticleByPageWithCondition(
            @Param("skip") Integer skip,
            @Param("limit") Integer limit,
            @Param("dto") FrontBlogArticleListDto articleListDto);

    Boolean articleExist(@Param("id") Integer articleId);

    List<AdminBlogArticleVo> getAdminArticleByPage(
            @Param("skip") Integer skip,
            @Param("limit") Integer limit,
            @Param("userId") Integer userId,
            @Param("dto") AdminBlogArticleSearchDto searchDto
    );

    Long getAdminArticleCount(@Param("userId") Integer userId, @Param("dto") AdminBlogArticleSearchDto searchDto);

    AdminBlogArticleVo getAdminArticleById(@Param("id") Integer id);

}

