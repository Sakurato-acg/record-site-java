package top.recordsite.blog;

import com.baomidou.mybatisplus.extension.service.IService;
import top.recordsite.dto.blog.AdminBlogCategorySearchDto;
import top.recordsite.dto.blog.BlogCategoryDto;
import top.recordsite.entity.blog.BlogCategory;
import top.recordsite.vo.ListVo;
import top.recordsite.vo.blog.BlogCategoryCountVo;

import java.util.List;

/**
 * <p>
 * 分类表 服务类
 * </p>
 *
 * @author lpl
 * @since 2023-10-23
 */
public interface IBlogCategoryService extends IService<BlogCategory> {

    ListVo<BlogCategoryCountVo> getFrontCategoryList(Integer pageSize);

    List<BlogCategory> getCategorySelectList();

    ListVo<BlogCategoryCountVo> getAdminCategoryList(AdminBlogCategorySearchDto searchDto);

    void addCategory(BlogCategoryDto dto);

    BlogCategory getCategoryById(Integer id);

    void updateCategoryById(BlogCategoryDto dto);

    void deleteCategoryByIds(List<Integer> ids);

}
