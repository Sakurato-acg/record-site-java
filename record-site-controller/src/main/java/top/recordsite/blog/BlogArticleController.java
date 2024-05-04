package top.recordsite.blog;


import com.info.annotation.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.recordsite.dto.blog.article.AdminBlogArticleSearchDto;
import top.recordsite.dto.blog.article.BlogArticleAddDto;
import top.recordsite.dto.blog.article.BlogArticleUpdateDto;
import top.recordsite.dto.blog.article.FrontBlogArticleListDto;
import top.recordsite.vo.ListVo;
import top.recordsite.vo.Result;
import top.recordsite.vo.blog.article.AdminBlogArticleVo;
import top.recordsite.vo.blog.article.BlogArticleEditVo;
import top.recordsite.vo.blog.article.FrontBlogArticleVo;

import javax.validation.constraints.NotEmpty;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 博客文章表 前端控制器
 * </p>
 *
 * @author lpl
 * @since 2023-10-23
 */
@RestController
@RequestMapping("/blogArticle")
@Slf4j
public class BlogArticleController {
    @Autowired
    protected IBlogArticleService articleService;

    @SystemLog(businessName = "获取前台文章列表")
    @PostMapping("/front/list")
    public Result getFrontArticleList(@RequestBody @Validated FrontBlogArticleListDto articleListDto) {
        //博客首页文章列表
        ListVo<FrontBlogArticleVo> listVo = articleService.getFrontArticleList(articleListDto);
        return Result.success(listVo);
    }


    @SystemLog(businessName = "前台根据文章id获取文章")
    @GetMapping("/front/{id}")
    public Result getFrontArticleById(@PathVariable("id") Integer id) {
//        log.error("获取文章");
//        throw new BusinessException("测试");

        FrontBlogArticleVo articleVo = articleService.getFrontArticleById(id);
        return Result.success(articleVo);
    }

    @SystemLog(businessName = "前台根据文章id更新文章pv")
    @PostMapping("/updateView/{id}")
    public Result updateArticleViewCount(@PathVariable("id") Integer id) {
        log.info("更新文章viewCount，文章id为{}", id.toString());
        articleService.updateArticleViewCount(id);
        return Result.success();
    }


    @PreAuthorize("hasAnyAuthority('blog:article')")
    @SystemLog(businessName = "获取后台文章列表")
    @PostMapping("/admin/list")
    public Result getAdminArticleList(@RequestBody @Validated AdminBlogArticleSearchDto searchDto) {
        if (searchDto.getTagIds() != null && CollectionUtils.isEmpty(Arrays.asList(searchDto.getTagIds()))) {
            searchDto.setTagIds(null);
        }
        ListVo<AdminBlogArticleVo> listVo = articleService.getAdminArticleList(searchDto);
        if (listVo.getTotal() == 0) {
            return Result.success("没有文章，赶紧创作吧", listVo);
        }
        return Result.success("获取文章列表成功", listVo);
    }

    @SystemLog(businessName = "后台根据文章id获取文章")
    @GetMapping("/admin/{id}")
    public Result getAdminArticleById(@PathVariable("id") Integer id) {
        BlogArticleEditVo articleVo = articleService.getAdminArticleById(id);
        return Result.success(articleVo);
    }

    @SystemLog(businessName = "新增博客文章")
    @PreAuthorize("hasAnyAuthority('blog:edit')")
    @PostMapping("/admin")
    public Result addArticle(@RequestBody @Validated BlogArticleAddDto addDto) {
        articleService.addArticle(addDto);
        return Result.success("新增博客成功");
    }

    @SystemLog(businessName = "更新博客文章")
    @PreAuthorize("hasAnyAuthority('blog:edit')")
    @PutMapping("/admin")
    public Result updateArticle(@RequestBody @Validated BlogArticleUpdateDto updateDto) {
        articleService.updateArticle(updateDto);
        return Result.success("更新博客成功");
    }


    @SystemLog(businessName = "删除博客文章")
    @PreAuthorize("hasAnyAuthority('blog:edit')")
    @DeleteMapping("/{id}")
    public Result deleteArticle(@PathVariable("id") @NotEmpty List<Integer> ids) {
        articleService.deleteArticleByIds(ids);
        return Result.success("删除成功");
    }
}

