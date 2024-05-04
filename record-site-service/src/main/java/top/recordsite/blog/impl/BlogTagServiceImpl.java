package top.recordsite.blog.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import top.recordsite.blog.IBlogTagService;
import top.recordsite.dto.blog.AdminBlogTagSearchDto;
import top.recordsite.dto.blog.BlogTagDto;
import top.recordsite.entity.blog.BlogArticleTag;
import top.recordsite.entity.blog.BlogTag;
import top.recordsite.enums.system.CommonDictionary;
import top.recordsite.exception.BusinessException;
import top.recordsite.mapper.blog.BlogArticleTagMapper;
import top.recordsite.mapper.blog.BlogTagMapper;
import top.recordsite.utils.BeanCopyUtils;
import top.recordsite.vo.ListVo;
import top.recordsite.vo.blog.BlogTagCountVo;

import java.util.List;


/**
 * <p>
 * 博客标签表 服务实现类
 * </p>
 *
 * @author lpl
 * @since 2023-10-23
 */
@Service
public class BlogTagServiceImpl extends ServiceImpl<BlogTagMapper, BlogTag> implements IBlogTagService {

    @Autowired
    private BlogTagMapper tagMapper;

    @Autowired
    private BlogArticleTagMapper articleTagMapper;


    @Override
    public ListVo<BlogTag> getFrontTagList(Integer pageSize,String categoryName) {
        ListVo<BlogTag> listVo = new ListVo<>();
        if (!StringUtils.hasText(categoryName)) {
            categoryName=null;
        }

        List<BlogTag> list = tagMapper.getFrontTagList(pageSize,categoryName);
        if (list.size() == 0) {
            throw new BusinessException("标签数为0");
        }
        listVo.setList(list);
        Integer count = tagMapper.getTagListCount(CommonDictionary.status_able,categoryName);
        listVo.setTotal(Long.valueOf(count));
        return listVo;

    }

    @Override
    public List<BlogTag> getTagSelectList() {
        List<BlogTag> list = tagMapper.getTagSelectList();
        if (list.size() == 0) {
            throw new BusinessException("标签数为0");
        }
        return list;
    }

    @Override
    public ListVo<BlogTagCountVo> getAdminTagList(AdminBlogTagSearchDto searchDto) {
        //设置分页参数
        Integer skip = (searchDto.getCurrentPage() - 1) * searchDto.getPageSize();
        Integer limit = searchDto.getPageSize();

        List<BlogTagCountVo> list = tagMapper.getAdminTagList(skip, limit, searchDto);
        if (ObjectUtils.isEmpty(list)) {
            throw new BusinessException("标签数为0");
        }

        ListVo<BlogTagCountVo> listVo = new ListVo<>();
        listVo.setList(list);
        Integer count = tagMapper.getTagListCount(null,null);
        listVo.setTotal(Long.valueOf(count));
        return listVo;
    }

    @Transactional
    @Override
    public void addTag(BlogTagDto dto) {
        int insert = tagMapper.insert(BeanCopyUtils.copyBean(dto, BlogTag.class));
        if (insert == 0) {
            throw new BusinessException("标签更新失败");
        }
    }

    @Override
    public BlogTag getTagById(Integer id) {
        BlogTag countVo = tagMapper.getTagById(id);
        return countVo;
    }

    @Transactional
    @Override
    public void updateTagById(BlogTagDto dto) {
        if (ObjectUtils.isEmpty(dto.getId())) {
            throw new BusinessException("标签更新失败");
        }
        int update = tagMapper.updateById(BeanCopyUtils.copyBean(dto, BlogTag.class));
        if (update == 0) {
            throw new BusinessException("标签更新失败");
        }
    }

    @Transactional
    @Override
    public void deleteTagByIds(List<Integer> ids) {
        //更新数据库
        LambdaUpdateWrapper<BlogTag> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(BlogTag::getId, ids);
        updateWrapper.set(BlogTag::getDelFlag, CommonDictionary.del_disabled);
        int update = tagMapper.update(null, updateWrapper);
        if (update == 0) {
            throw new BusinessException("删除失败");
        }
        //更新关联表
        LambdaUpdateWrapper<BlogArticleTag> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(BlogArticleTag::getTagId,ids);
        articleTagMapper.delete(wrapper);


    }
}
