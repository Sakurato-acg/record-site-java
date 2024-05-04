package top.recordsite;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.recordsite.mapper.system.MenuMapper;
import top.recordsite.system.IMenuService;
import top.recordsite.vo.system.MenuVo;

import java.util.List;

@Slf4j
@SpringBootTest
public class MenuTest {
    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private IMenuService menuService;

    @Test
    void test() {
        List<MenuVo> menuVoList = menuMapper.selectAllRouterMenu();
        menuVoList.forEach(System.out::println);
    }

    @Test
    void tree() {
        List<MenuVo> menuVoList = menuService.selectRouterMenuTreeByUserId(1);
        menuVoList.forEach(System.out::println);
    }

    @Test
    void getMenuById() {
        List<MenuVo> menuVoList = menuMapper.selectRouterMenuTreeByUserId(4);
        menuVoList.forEach(item-> log.info(item.toString()));
    }
}
