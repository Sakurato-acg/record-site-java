package top.recordsite.blog;


import com.info.annotation.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.recordsite.dto.blog.AdminBlogCategorySearchDto;
import top.recordsite.dto.blog.BlogCategoryDto;
import top.recordsite.entity.blog.BlogCategory;
import top.recordsite.vo.ListVo;
import top.recordsite.vo.Result;
import top.recordsite.vo.blog.BlogCategoryCountVo;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 分类表 前端控制器
 * </p>
 *
 * @author lpl
 * @since 2023-10-23
 */
@Slf4j
@RestController
@RequestMapping("/blogCategory")
public class BlogCategoryController {

    @Autowired
    private IBlogCategoryService categoryService;

    @SystemLog(businessName = "获取前台分类列表")
    @GetMapping("/front")
    public Result getFrontCategoryList(Integer pageSize) {
        ListVo<BlogCategoryCountVo> list = categoryService.getFrontCategoryList(pageSize);
        return Result.success(list);
    }

    @SystemLog(businessName = "获取select分类列表")
    @GetMapping("/select")
    public Result getCategorySelectList() {
        List<BlogCategory> list = categoryService.getCategorySelectList();
        return Result.success(list);
    }

    @SystemLog(businessName = "获取后台分类列表")
    @PostMapping("/admin")
    public Result getAdminCategoryList(@Validated @RequestBody AdminBlogCategorySearchDto searchDto) {
        ListVo<BlogCategoryCountVo> list = categoryService.getAdminCategoryList(searchDto);
        return Result.success(list).setMsg("获取列表成功");
    }

    @SystemLog(businessName = "添加分类")
    @PutMapping
    public Result addCategory(@Validated @RequestBody BlogCategoryDto dto){
        categoryService.addCategory(dto);
        return  Result.success("分类添加成功");
    }

    @SystemLog(businessName = "获取分类详情")
    @GetMapping("/{id}")
    public Result getCategoryInfo(@PathVariable("id") @NotNull Integer id){
        BlogCategory blogCategory=categoryService.getCategoryById(id);
        return  Result.success(blogCategory);
    }

    @SystemLog(businessName = "更新分类")
    @PreAuthorize("hasAnyAuthority('blog:category')")
    @PostMapping("/{id}")
    public Result updateCategory(@Validated @RequestBody BlogCategoryDto dto){
        categoryService.updateCategoryById(dto);
        return  Result.success("分类添加成功");
    }

    @SystemLog(businessName = "删除分类")
    @PreAuthorize("hasAnyAuthority('blog:category')")
    @DeleteMapping("/{id}")
    public Result deleteCategory(@PathVariable("id") @NotEmpty List<Integer> ids) {
        categoryService.deleteCategoryByIds(ids);
        return Result.success("删除成功");
    }

}

