package top.recordsite.mapper.blog;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.recordsite.dto.blog.AdminBlogCategorySearchDto;
import top.recordsite.entity.blog.BlogCategory;
import top.recordsite.vo.blog.BlogCategoryCountVo;

import java.util.List;

/**
 * <p>
 * 分类表 Mapper 接口
 * </p>
 *
 * @author lpl
 * @since 2023-10-23
 */
@Mapper
public interface BlogCategoryMapper extends BaseMapper<BlogCategory> {


    List<BlogCategoryCountVo> getFrontCategoryList(@Param("pageSize") Integer pageSize);

    Integer getCategoryListCount(@Param("status") Integer status );

    List<BlogCategory> getCategorySelectList();

    List<BlogCategoryCountVo> getAdminCategoryList(
            @Param("skip") Integer skip,
            @Param("limit") Integer limit,
            @Param("dto") AdminBlogCategorySearchDto searchDto);

    BlogCategory getCategoryById(@Param("id") Integer id);


//    FrontBlogArticleCategoryVo getCategoryVoByIdWithCondition(@Param("id") Integer categoryId);
}
