package top.recordsite.runner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import top.recordsite.entity.blog.BlogArticle;
import top.recordsite.enums.blog.ArticleDictionary;
import top.recordsite.mapper.blog.BlogArticleMapper;
import top.recordsite.utils.RedisUtlis.RedisString;
import top.recordsite.utils.RedisUtlis.RedisUtils;

import java.util.List;

@Slf4j
@Component
public class ViewCountRunner implements CommandLineRunner {
    @Autowired
    private RedisUtils<Long> redisUtils;

    @Autowired
    private BlogArticleMapper articleMapper;

    @Override
    public void run(String... args) throws Exception {
        log.info("程序初始化");
        LambdaQueryWrapper<BlogArticle> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(BlogArticle::getId,BlogArticle::getViewCount);

        List<BlogArticle> selectList = articleMapper
                .selectList(queryWrapper);

        RedisString<Long> redisString = redisUtils.getRedisString();

        for (BlogArticle blogArticle : selectList) {
            String key= ArticleDictionary.article_prefix+blogArticle.getId();
            redisString.set(key,blogArticle.getViewCount());
        }

    }
}
