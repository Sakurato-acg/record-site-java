package top.recordsite;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import top.recordsite.blog.IBlogArticleService;
import top.recordsite.dto.blog.article.FrontBlogArticleListDto;
import top.recordsite.dto.blog.comment.CommentDto;
import top.recordsite.system.ICommentService;
import top.recordsite.utils.RedisUtlis.RedisUtils;
import top.recordsite.vo.blog.ListCommentVo;

@SpringBootTest
public class ArticleTest {

    @Autowired
    private ICommentService commentService;
    @Autowired
    private IBlogArticleService articleService;

    @Test
    void test() {
        CommentDto commentDto = new CommentDto().setArticleId(100).setType(0).setCurrentPage(1).setPageSize(10);
        ListCommentVo comment = commentService.getComment(commentDto);
        System.out.println("comment = " + comment);
    }

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisUtils redisUtils;

    @Test
    void redis(){

    }

    @Test
    void getList() {
        articleService.getFrontArticleList(new FrontBlogArticleListDto().setPageSize(10).setCurrentPage(1));
    }
}
