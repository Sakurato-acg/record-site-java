package top.recordsite.job;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.recordsite.blog.IBlogArticleService;
import top.recordsite.entity.blog.BlogArticle;
import top.recordsite.utils.RedisUtlis.RedisString;
import top.recordsite.utils.RedisUtlis.RedisUtils;

import java.util.Set;

@Component
public class UpdateViewCountJob {

    @Autowired
    private IBlogArticleService articleService;

    @Autowired
    private RedisUtils<Long> redisUtils;

    //  秒，分钟，小时，日期，月，星期
    //0/*  20    *    *   *   ?
    @Scheduled(cron = "0 0 0/2 * * ?")
    public void UpdateViewCount() {
        //获取redis中的数据
        RedisString<Long> redisString = redisUtils.getRedisString();

        Set<String> keys = redisString.keys("article:viewCount:id*");
        keys.forEach(item->{
            Long viewCount = redisString.get(item);
            String[] split = item.split(":");
            Integer id = Integer.valueOf(split[split.length-1]);
            // 存入数据库中
            LambdaUpdateWrapper<BlogArticle> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(BlogArticle::getViewCount,viewCount);
            updateWrapper.eq(BlogArticle::getId,id);
            articleService.update(null,updateWrapper);
        });



    }

}
