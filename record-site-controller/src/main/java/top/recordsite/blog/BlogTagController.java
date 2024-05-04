package top.recordsite.blog;


import com.info.annotation.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.recordsite.dto.blog.AdminBlogTagSearchDto;
import top.recordsite.dto.blog.BlogTagDto;
import top.recordsite.entity.blog.BlogTag;
import top.recordsite.vo.ListVo;
import top.recordsite.vo.Result;
import top.recordsite.vo.blog.BlogTagCountVo;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 博客标签表 前端控制器
 * </p>
 *
 * @author lpl
 * @since 2023-10-23
 */
@Slf4j
@RestController
@RequestMapping("/blogTag")
public class BlogTagController {

    @Autowired
    private IBlogTagService blogTagService;

    @SystemLog(businessName = "获取前台博客标签列表")
    @GetMapping("/front")
    public Result getFrontTagList(Integer pageSize,String categoryName) {
        ListVo<BlogTag> listVo = blogTagService.getFrontTagList(pageSize,categoryName);
        return Result.success(listVo);
    }


    @SystemLog(businessName = "获取select标签列表")
    @GetMapping("/select")
    public Result getTagSelectList() {
        List<BlogTag> list = blogTagService.getTagSelectList();
        return Result.success(list);
    }

    @SystemLog(businessName = "获取后台标签列表")
    @PostMapping("/admin")
    public Result getAdminTagList(@Validated @RequestBody AdminBlogTagSearchDto searchDto) {
        ListVo<BlogTagCountVo> list = blogTagService.getAdminTagList(searchDto);
        return Result.success(list).setMsg("获取列表成功");
    }

    @SystemLog(businessName = "添加标签")
    @PutMapping
    public Result addTag(@Validated @RequestBody BlogTagDto dto){
        blogTagService.addTag(dto);
        return  Result.success("标签添加成功");
    }

    @SystemLog(businessName = "获取标签详情")
    @GetMapping("/{id}")
    public Result getTagInfo(@PathVariable("id") @NotNull Integer id){
        BlogTag blogTag=blogTagService.getTagById(id);
        return  Result.success(blogTag);
    }

    @SystemLog(businessName = "更新标签")
    @PreAuthorize("hasAnyAuthority('blog:tag')")
    @PostMapping("/{id}")
    public Result updateCategory(@Validated @RequestBody BlogTagDto dto){
        blogTagService.updateTagById(dto);
        return  Result.success("标签添加成功");
    }

    @SystemLog(businessName = "删除标签")
    @PreAuthorize("hasAnyAuthority('blog:tag')")
    @DeleteMapping("/{id}")
    public Result deleteCategory(@PathVariable("id") @NotEmpty List<Integer> ids) {
        blogTagService.deleteTagByIds(ids);
        return Result.success("删除成功");
    }


}

