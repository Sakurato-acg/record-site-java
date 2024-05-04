package top.recordsite.blog;

import com.baomidou.mybatisplus.extension.service.IService;
import top.recordsite.dto.blog.article.AdminBlogArticleSearchDto;
import top.recordsite.dto.blog.article.BlogArticleAddDto;
import top.recordsite.dto.blog.article.BlogArticleUpdateDto;
import top.recordsite.dto.blog.article.FrontBlogArticleListDto;
import top.recordsite.entity.blog.BlogArticle;
import top.recordsite.vo.ListVo;
import top.recordsite.vo.blog.article.AdminBlogArticleVo;
import top.recordsite.vo.blog.article.BlogArticleEditVo;
import top.recordsite.vo.blog.article.FrontBlogArticleVo;

import java.util.List;

/**
 * <p>
 * 博客文章表 服务类
 * </p>
 *
 * @author lpl
 * @since 2023-10-23
 */

public interface IBlogArticleService extends IService<BlogArticle> {

    ListVo<FrontBlogArticleVo> getFrontArticleList(FrontBlogArticleListDto articleListDto);

    FrontBlogArticleVo getFrontArticleById(Integer id);

    ListVo<AdminBlogArticleVo> getAdminArticleList(AdminBlogArticleSearchDto searchDto);

    BlogArticleEditVo getAdminArticleById(Integer id);

    void addArticle(BlogArticleAddDto addDto);

    void updateArticle(BlogArticleUpdateDto updateDto);

    void deleteArticleByIds(List<Integer> ids);

    void updateArticleViewCount(Integer id);
}
