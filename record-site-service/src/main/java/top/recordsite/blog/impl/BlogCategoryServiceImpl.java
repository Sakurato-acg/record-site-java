package top.recordsite.blog.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.recordsite.blog.IBlogCategoryService;
import top.recordsite.dto.blog.AdminBlogCategorySearchDto;
import top.recordsite.dto.blog.BlogCategoryDto;
import top.recordsite.entity.blog.BlogCategory;
import top.recordsite.enums.system.CommonDictionary;
import top.recordsite.exception.BusinessException;
import top.recordsite.mapper.blog.BlogCategoryMapper;
import top.recordsite.utils.BeanCopyUtils;
import top.recordsite.vo.ListVo;
import top.recordsite.vo.blog.BlogCategoryCountVo;

import java.util.List;

/**
 * <p>
 * 分类表 服务实现类
 * </p>
 *
 * @author lpl
 * @since 2023-10-23
 */
@Transactional
@Service
public class BlogCategoryServiceImpl extends ServiceImpl<BlogCategoryMapper, BlogCategory> implements IBlogCategoryService {
    @Autowired
    private BlogCategoryMapper categoryMapper;

    @Override
    public ListVo<BlogCategoryCountVo> getFrontCategoryList(Integer pageSize) {
        List<BlogCategoryCountVo> list = null;
        if (ObjectUtils.isNotEmpty(pageSize)) {
            list = categoryMapper.getFrontCategoryList(pageSize);
        } else {
            list = categoryMapper.getFrontCategoryList(null);
        }
        if (ObjectUtils.isEmpty(list)) {
            throw new BusinessException("分类数为0");
        }
        ListVo<BlogCategoryCountVo> listVo = new ListVo<>();
        list.sort((o1, o2) -> o2.getCount()-o1.getCount());
        listVo.setList(list);
        Integer count = categoryMapper.getCategoryListCount(CommonDictionary.status_able);
        listVo.setTotal(Long.valueOf(count));
        return listVo;
    }

    @Override
    public List<BlogCategory> getCategorySelectList() {
        List<BlogCategory> list = categoryMapper.getCategorySelectList();
        if (ObjectUtils.isEmpty(list)) {
            throw new BusinessException("分类数为0");
        }
        return list;
    }

    @Override
    public ListVo<BlogCategoryCountVo> getAdminCategoryList(AdminBlogCategorySearchDto searchDto) {
        //设置分页参数
        Integer skip = (searchDto.getCurrentPage() - 1) * searchDto.getPageSize();
        Integer limit = searchDto.getPageSize();

        List<BlogCategoryCountVo> list = categoryMapper.getAdminCategoryList(skip, limit, searchDto);
        if (ObjectUtils.isEmpty(list)) {
            throw new BusinessException("分类数为0");
        }

        ListVo<BlogCategoryCountVo> listVo = new ListVo<>();
        listVo.setList(list);
        Integer count = categoryMapper.getCategoryListCount(null);
        listVo.setTotal(Long.valueOf(count));
        return listVo;
    }

    @Transactional
    @Override
    public void addCategory(BlogCategoryDto dto) {
        int insert = categoryMapper.insert(BeanCopyUtils.copyBean(dto, BlogCategory.class));
        if (insert == 0) {
            throw new BusinessException("分类更新失败");
        }
    }

    @Override
    public BlogCategory getCategoryById(Integer id) {
        BlogCategory countVo = categoryMapper.getCategoryById(id);

        return countVo;
    }

    @Transactional
    @Override
    public void updateCategoryById(BlogCategoryDto dto) {
        if (ObjectUtils.isEmpty(dto.getId())) {
            throw new BusinessException("分类更新失败");
        }
        int update = categoryMapper.updateById(BeanCopyUtils.copyBean(dto, BlogCategory.class));
        if (update == 0) {
            throw new BusinessException("分类更新失败");
        }
    }

    @Override
    public void deleteCategoryByIds(List<Integer> ids) {
        LambdaUpdateWrapper<BlogCategory> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(BlogCategory::getId, ids);
        updateWrapper.set(BlogCategory::getDelFlag, 1);
        int update = categoryMapper.update(null, updateWrapper);
        if (update == 0) {
            throw new BusinessException("删除失败");
        }

    }


}
