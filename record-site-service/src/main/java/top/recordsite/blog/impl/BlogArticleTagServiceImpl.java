package top.recordsite.blog.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.recordsite.blog.IBlogArticleTagService;
import top.recordsite.entity.blog.BlogArticleTag;
import top.recordsite.mapper.blog.BlogArticleTagMapper;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lpl
 * @since 2023-10-23
 */
@Service
public class BlogArticleTagServiceImpl extends ServiceImpl<BlogArticleTagMapper, BlogArticleTag> implements IBlogArticleTagService {

}
