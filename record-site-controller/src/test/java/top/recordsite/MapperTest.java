package top.recordsite;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.recordsite.dto.system.RoleSearchDto;
import top.recordsite.entity.anime.Anime;
import top.recordsite.entity.blog.BlogArticle;
import top.recordsite.mapper.anime.AnimeMapper;
import top.recordsite.mapper.blog.BlogArticleMapper;
import top.recordsite.mapper.blog.BlogCategoryMapper;
import top.recordsite.mapper.blog.CommentMapper;
import top.recordsite.mapper.system.UserMapper;
import top.recordsite.system.IRoleService;
import top.recordsite.vo.ListVo;
import top.recordsite.vo.system.RoleVo;

import java.util.List;

@SpringBootTest
public class MapperTest {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AnimeMapper animeMapper;
    @Autowired
    private BlogArticleMapper articleMapper;

//    @Test
//    void CommentTest() {
//        List<CommentVo> test = commentMapper.test(0, 3, 1, 1);
//        test.forEach(System.out::println);
//    }

//    @Test
//    void test(){
//        String test = userMapper.test();
//        System.out.println("test = " + test);
//    }
    @Autowired
    private IRoleService roleService;

    @Test
    void anime() {
        Integer count = animeMapper.bangumiExist(new Anime().setName("生而为狗，我很幸福"), 4);
        System.out.println("count = " + count);
    }

    @Test
    void blog() {
        LambdaQueryWrapper<BlogArticle> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlogArticle::getStatus, 1);
        List<BlogArticle> blogArticles = articleMapper.selectList(queryWrapper);
        blogArticles.forEach(System.out::println);
    }

    @Test
    void getById() {
//        BlogArticle article = articleMapper.getFrontArticleById(1);
//        System.out.println(article);
    }

    @Test
    void getAdminRoleList() {
        RoleSearchDto dto = new RoleSearchDto()
                .setCurrentPage(1)
                .setPageSize(10);
        ListVo<RoleVo> adminRoleList = roleService.getAdminRoleList(dto);

        System.out.println("adminRoleList = " + adminRoleList);

    }

    @Autowired
    private BlogCategoryMapper categoryMapper;

}
