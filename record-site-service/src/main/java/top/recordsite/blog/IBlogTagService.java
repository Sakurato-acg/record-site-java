package top.recordsite.blog;

import com.baomidou.mybatisplus.extension.service.IService;
import top.recordsite.dto.blog.AdminBlogTagSearchDto;
import top.recordsite.dto.blog.BlogTagDto;
import top.recordsite.entity.blog.BlogTag;
import top.recordsite.vo.ListVo;
import top.recordsite.vo.blog.BlogTagCountVo;

import java.util.List;

/**
 * <p>
 * 博客标签表 服务类
 * </p>
 *
 * @author lpl
 * @since 2023-10-23
 */
public interface IBlogTagService extends IService<BlogTag> {


    ListVo<BlogTag> getFrontTagList(Integer pageSize,String categoryName);

    List<BlogTag> getTagSelectList();

    ListVo<BlogTagCountVo> getAdminTagList(AdminBlogTagSearchDto searchDto);

    void addTag(BlogTagDto dto);

    BlogTag getTagById(Integer id);

    void updateTagById(BlogTagDto dto);

    void deleteTagByIds(List<Integer> ids);
}
