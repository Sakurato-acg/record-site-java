package top.recordsite;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.recordsite.anime.IAnimeService;

import java.util.Arrays;

@SpringBootTest
public class AnimeTest {

    @Autowired
    private IAnimeService animeService;


    @Test
    void animeList() {
//        IPage<AdminAnimeListVo> animeList = animeService.getAnimeList(a);
//
//        animeList.getRecords().forEach(System.out::println);
        Integer[] array = {1, 24, 5};
        Arrays.sort(array, (o1, o2) -> o2-o1);
        for (Integer item : array) {
            System.out.println("item = " + item);
        }
    }

    @Test
    void animeUserList() {
//        List<Integer> list = userService.animeUserList();
//        list.forEach(System.out::println);
    }

}
