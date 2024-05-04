package top.recordsite.blog.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import top.recordsite.blog.IBlogArticleService;
import top.recordsite.dto.blog.article.AdminBlogArticleSearchDto;
import top.recordsite.dto.blog.article.BlogArticleAddDto;
import top.recordsite.dto.blog.article.BlogArticleUpdateDto;
import top.recordsite.dto.blog.article.FrontBlogArticleListDto;
import top.recordsite.entity.blog.BlogArticle;
import top.recordsite.entity.blog.BlogArticleTag;
import top.recordsite.entity.blog.BlogTag;
import top.recordsite.entity.system.Media;
import top.recordsite.enums.blog.ArticleDictionary;
import top.recordsite.enums.system.CommonDictionary;
import top.recordsite.exception.BusinessException;
import top.recordsite.mapper.blog.BlogArticleMapper;
import top.recordsite.mapper.blog.BlogArticleTagMapper;
import top.recordsite.mapper.blog.BlogTagMapper;
import top.recordsite.mapper.system.MediaMapper;
import top.recordsite.security.SecurityUtils;
import top.recordsite.utils.BeanCopyUtils;
import top.recordsite.utils.RedisUtlis.RedisString;
import top.recordsite.utils.RedisUtlis.RedisUtils;
import top.recordsite.vo.ListVo;
import top.recordsite.vo.blog.article.AdminBlogArticleVo;
import top.recordsite.vo.blog.article.BlogArticleEditVo;
import top.recordsite.vo.blog.article.FrontBlogArticleMapperVo;
import top.recordsite.vo.blog.article.FrontBlogArticleVo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 博客文章表 服务实现类
 * </p>
 *
 * @author lpl
 * @since 2023-10-23
 */
@Service
public class BlogArticleServiceImpl extends ServiceImpl<BlogArticleMapper, BlogArticle> implements IBlogArticleService {

    @Autowired
    private BlogArticleMapper articleMapper;

    @Autowired
    private BlogTagMapper tagMapper;

    @Autowired
    private BlogArticleTagMapper articleTagMapper;

    @Autowired
    private MediaMapper mediaMapper;

    @Autowired
    private RedisUtils<Long> redisUtils;

    @Override
    public ListVo<FrontBlogArticleVo> getFrontArticleList(FrontBlogArticleListDto articleListDto) {
        //设置分页参数
        Integer skip = (articleListDto.getCurrentPage() - 1) * articleListDto.getPageSize();
        Integer limit = articleListDto.getPageSize();
        if (!StringUtils.hasText(articleListDto.getCategoryName())) {
            articleListDto.setCategoryName(null);
        }

        //分页查询符合条件文章，文章状态位发布，未删除
        List<FrontBlogArticleMapperVo> articleVoList = articleMapper.getFrontArticleByPageWithCondition(skip, limit, articleListDto);
        List<FrontBlogArticleVo> articleVos = articleVoList
                .parallelStream()
                .map(item -> {
                    FrontBlogArticleVo articleVo = BeanCopyUtils.copyBean(item, FrontBlogArticleVo.class);
                    List<Integer> tagIds = Arrays
                            .stream(item.getTagIds().split(","))
                            .map(Integer::parseInt).toList();
                    List<String> tagNames = Arrays
                            .stream(item.getTagNames().split(","))
                            .toList();
                    for (int index = 0; index < tagIds.size(); index++) {
                        BlogTag blogTag = new BlogTag()
                                .setId(tagIds.get(index))
                                .setName(tagNames.get(index));
                        articleVo.getTags().add(blogTag);
                    }
                    articleVo.setViewCount(getArticleViewCount(articleVo.getId()));
                    return articleVo;
                }).collect(Collectors.toList());

        Long total = articleMapper.getFrontArticleCountWithCondition(articleListDto);

        //如果没有文章，则抛出异常
        if (ObjectUtils.isEmpty(articleVoList)) {
            throw new BusinessException("暂无文章");
        }


        ListVo<FrontBlogArticleVo> listVo = new ListVo<>();

//        List<FrontBlogArticleVo> collect = articleIPage
//                .stream()
//                .map(this::getFrontBlogArticleVo
//                ).filter(ObjectUtils::isNotEmpty)
//                .collect(Collectors.toList());
//
        listVo.setList(articleVos);
        //组装参数
        listVo.setTotal(total);
        return listVo;

    }

    @Override
    public FrontBlogArticleVo getFrontArticleById(Integer id) {
        FrontBlogArticleVo articleVo = articleMapper.getFrontArticleById(id);
   /*     LambdaQueryWrapper<BlogArticle> queryWrapper = new LambdaQueryWrapper<>();

        //文章状态要为已发布,文章要未被删除
        queryWrapper.eq(BlogArticle::getId, id);
        queryWrapper.eq(BlogArticle::getStatus, 1);
        queryWrapper.select(
                BlogArticle::getId,
                BlogArticle::getTitle,
                BlogArticle::getCreateTime,
                BlogArticle::getCreateBy,
                BlogArticle::getViewCount,
                BlogArticle::getCategoryId,
                BlogArticle::getContent
        );
        BlogArticle blogArticle = articleMapper.selectOne(queryWrapper);
        //如果查不到文章，可能文章id不存在、文章未发布、文章已逻辑删除
        if (ObjectUtils.isEmpty(blogArticle)) {
            throw new BusinessException("文章不存在");
        }
        FrontBlogArticleVo articleVo = getFrontBlogArticleVo(blogArticle);
        if (ObjectUtils.isEmpty(articleVo)) {
            throw new BusinessException("文章不存在");
        }*/
        for (BlogTag tag : articleVo.getTags()) {
            if (Objects.equals(tag.getStatus(), CommonDictionary.status_disabled)) {
                articleVo = null;
            } else {
                tag.setStatus(null);
            }
        }
        //如果没有文章，则抛出异常
        if (ObjectUtils.isEmpty(articleVo)) {
            throw new BusinessException("暂无文章");
        }

        articleVo.setViewCount(getArticleViewCount(articleVo.getId()));
        return articleVo;

    }

    public Long getArticleViewCount(Integer id) {
        RedisString<Long> redisString = redisUtils.getRedisString();
        return redisString.get(ArticleDictionary.article_prefix + id);
    }

    @Override
    public ListVo<AdminBlogArticleVo> getAdminArticleList(AdminBlogArticleSearchDto searchDto) {
        Integer skip = (searchDto.getCurrentPage() - 1) * searchDto.getPageSize();
        Integer limit = searchDto.getPageSize();

        //组装查询条件
//        if (!StringUtils.hasText(searchDto.getTitle())) {
//            searchDto.setTitle(null);
//        }
//        if (!StringUtils.hasText(searchDto.getAuthor())) {
//            searchDto.setAuthor(null);
//        }
//        if (searchDto.getStatus() != null && searchDto.getStatus() < 0) {
//            searchDto.setStatus(null);
//        }
//        if (searchDto.getIsTop() != null && searchDto.getIsTop() < 0) {
//            searchDto.setIsTop(null);
//        }


        //判断身份
        if (SecurityUtils.isAdmin()) {
            //是管理员则获取所有文章,即使文章、分类、用户、标签被封
            List<AdminBlogArticleVo> articleVoList = articleMapper
                    .getAdminArticleByPage(
                            skip,
                            limit,
                            null,
                            searchDto
                    );
            articleVoList = articleVoList.stream().filter(item -> {
                if (searchDto.getTagIds() != null) {
                    boolean check = false;
                    for (BlogTag tag : item.getTags()) {
                        if (Arrays.asList(searchDto.getTagIds()).contains(tag.getId())) {
                            check = true;
                            break;
                        }
                    }
                    return check;
                }
                return true;

            }).collect(Collectors.toList());


            Long total = articleMapper.getAdminArticleCount(null, searchDto);
            ListVo<AdminBlogArticleVo> listVo = new ListVo<>();
            listVo.setList(articleVoList);
            listVo.setTotal(total);
            return listVo;
        }
        else {
            //是平台用户则只能获取自己的文章，即使前台文章无法显示
            List<AdminBlogArticleVo> articleVoList = articleMapper
                    .getAdminArticleByPage(
                            skip,
                            limit,
                            SecurityUtils.getUserId(),
                            searchDto
                    );

            Long total = articleMapper.getAdminArticleCount(SecurityUtils.getUserId(), searchDto);
            ListVo<AdminBlogArticleVo> listVo = new ListVo<>();
            listVo.setList(articleVoList);
            listVo.setTotal(total);
            return listVo;
        }
    }

    @Override
    public BlogArticleEditVo getAdminArticleById(Integer id) {

        AdminBlogArticleVo articleVo = articleMapper.getAdminArticleById(id);
        //管理员可以看到所有文章，普通用户只能看到自己的
        if (!SecurityUtils.isAdmin()
                && !Objects.equals(articleVo.getAuthor().getId(), SecurityUtils.getUserId())) {
            throw new BusinessException("只能查看自己的文章");
        }
//        for (BlogTag tag : articleVo.getTags()) {
//            //
//            if (Objects.equals(tag.getStatus(), CommonDictionary.status_disabled)) {
//                articleVo = null;
//            } else {
//                tag.setStatus(null);
//            }
//        }
        //如果没有文章，则抛出异常
        if (ObjectUtils.isEmpty(articleVo)) {
            throw new BusinessException("暂无文章");
        }
        BlogArticleEditVo editVo = BeanCopyUtils.copyBean(articleVo, BlogArticleEditVo.class);
//        if (ObjectUtils.isNotEmpty(articleVo.getAuthor())) {
//            editVo.setUserId(articleVo.getAuthor().getId());
//        }
        if (ObjectUtils.isNotEmpty(articleVo.getCategory())) {
            editVo.setCategoryId(articleVo.getCategory().getId());
        }
        if (ObjectUtils.isNotEmpty(articleVo.getTags())) {
            editVo.setTagIds(articleVo.getTags().stream().map(BlogTag::getId).collect(Collectors.toList()));
        }

        return editVo;

    }

    @Transactional
    @Override
    public void addArticle(BlogArticleAddDto addDto) {
        //1.存储文章
        BlogArticle article = BeanCopyUtils.copyBean(addDto, BlogArticle.class);
        articleMapper.insert(article);
        //2.存储tag
        List<Object> tagIds = addDto.getTagIds();
        List<Integer> hasId = new ArrayList<>();
        for (Object tagId : tagIds) {
            try {
                Integer id = (Integer) tagId;
                hasId.add(id);
            } catch (Exception e) {
                String newTag = (String) tagId;
                BlogTag blogTag = new BlogTag().setName(newTag);
                try {
                    tagMapper.insert(blogTag);
                    hasId.add(blogTag.getId());
                } catch (DuplicateKeyException duplicateKeyException) {
                    log.error("tag已存在，标签名不能重复");
                }

            }
        }
        //3.关联表
        hasId.forEach(item -> {
            BlogArticleTag blogArticleTag = new BlogArticleTag();
            blogArticleTag.setArticleId(article.getId());
            blogArticleTag.setTagId(item);
            articleTagMapper.insert(blogArticleTag);
        });
        //4.media
        List<Long> fileList = addDto.getFileList();
        LambdaUpdateWrapper<Media> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(ObjectUtils.isNotEmpty(fileList), Media::getId, fileList);
        updateWrapper.set(Media::getArticleId, article.getId());
        mediaMapper.update(new Media(), updateWrapper);

    }

    @Transactional
    @Override
    public void updateArticle(BlogArticleUpdateDto updateDto) {
        //1.更新文章
        BlogArticle article = BeanCopyUtils.copyBean(updateDto, BlogArticle.class);
        articleMapper.updateById(article);
        //2.更新tag
        List<Object> tagIds = updateDto.getTagIds();
        List<Integer> hasId = new ArrayList<>();
        for (Object tagId : tagIds) {
            try {
                Integer id = (Integer) tagId;
                hasId.add(id);
            } catch (Exception e) {
                String newTag = (String) tagId;
                BlogTag blogTag = new BlogTag().setName(newTag);
                try {
                    tagMapper.insert(blogTag);
                    hasId.add(blogTag.getId());
                } catch (DuplicateKeyException duplicateKeyException) {
                    log.error("tag已存在，标签名不能重复");
                }

            }
        }
        //3.关联表
        LambdaQueryWrapper<BlogArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlogArticleTag::getArticleId, article.getId());
        List<Integer> list = articleTagMapper.selectList(queryWrapper).stream().map(BlogArticleTag::getTagId).toList();
        if (list.equals(hasId)) {
            //tag没变
            return;
        }
        articleTagMapper.delete(queryWrapper);
        hasId.forEach(item -> {
            BlogArticleTag blogArticleTag = new BlogArticleTag();
            blogArticleTag.setArticleId(article.getId());
            blogArticleTag.setTagId(item);
            articleTagMapper.insert(blogArticleTag);
        });
        //4.media
        List<Long> fileList = updateDto.getFileList();
        LambdaUpdateWrapper<Media> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(ObjectUtils.isNotEmpty(fileList), Media::getId, fileList);
        updateWrapper.set(Media::getArticleId, article.getId());
        mediaMapper.update(new Media(), updateWrapper);
    }

    @Transactional
    @Override
    public void deleteArticleByIds(List<Integer> ids) {
        //删除文章
        LambdaUpdateWrapper<BlogArticle> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(BlogArticle::getId, ids);
        updateWrapper.set(BlogArticle::getDelFlag, CommonDictionary.del_disabled);
        articleMapper.update(null, updateWrapper);
        //分类、标签不用删

        //删除关联表
        LambdaQueryWrapper<BlogArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(BlogArticleTag::getArticleId, ids);
        articleTagMapper.delete(queryWrapper);

        // TODO: 2024/1/25 文章图片
        //  1.数据库status先失效
        //  2.引入mq，异步删除oss
        //  3.分类、标签同理
    }

    @Override
    public void updateArticleViewCount(Integer id) {
        //1.在缓存中更新文章的浏览量
        //1.1 使用redis中的string key为article:id:1
        RedisString<Long> redis = redisUtils.getRedisString();
        String key = ArticleDictionary.article_prefix + id;
        Long aLong = redis.get(key);
        redis.set(key, ++aLong);
        //1.2 在项目启动时将数据库里的viewCount同步到redis中 √
        //2. 使用定时任务 异步同步缓存到数据库
    }

/*    private FrontBlogArticleVo getFrontBlogArticleVo(BlogArticle blogArticle) {
        //如果查到找文章先转成Vo,目前还需要username、tags、categoryVo
        FrontBlogArticleVo articleVo = BeanCopyUtils.copyBean(blogArticle, FrontBlogArticleVo.class);

        //根据create_by获取username,用户需要未被封禁,未被逻辑删除
        String username = userMapper.getUserNameByIdWithCondition(blogArticle.getCreateBy());
        //如果没有符合条件的用户，有可能文章存在，而用户封禁了，被封禁的用户的文章不该显示
        if (StringUtils.isEmpty(username)) {
            return null;
//            throw new BusinessException("文章用户不存在");
        }
        articleVo.setAuthor(username);

        //根据category_id 获取vo
        FrontBlogArticleCategoryVo categoryVo = categoryMapper.getCategoryVoByIdWithCondition(blogArticle.getCategoryId());
        if (ObjectUtils.isEmpty(categoryVo)) {
            return null;
//            throw new BusinessException("分类不存在");
        }
        articleVo.setCategory(categoryVo);

        //获取文章的tag
        List<FrontBlogArticleTagVo> tagVos = tagMapper.getTagByArticleIdWithCondition(blogArticle.getId());
        if (ObjectUtils.isEmpty(tagVos)) {
            return null;
//            throw new BusinessException("标签不存在");
        }
        articleVo.setTags(tagVos);
        return articleVo;



                articleVoList = articleVoList.stream()
                .filter(item -> {
                    List<BlogTag> tags = item.getTags();
                    boolean hasTagName=!StringUtils.hasText(articleListDto.getTagName());
                    for (BlogTag tag : tags) {
                        if (StringUtils.hasText(articleListDto.getTagName())
                                && tag.getName().equals(articleListDto.getTagName())){
                            hasTagName=true;
                        }
                        if (Objects.equals(tag.getStatus(), CommonDictionary.status_disabled)) {
                            return false;
                        } else {
                            tag.setStatus(null);
                        }
                    }
                    return hasTagName;
                })
                .collect(Collectors.toList());
    }*/
}













