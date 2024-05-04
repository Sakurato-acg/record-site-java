package top.recordsite;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.MimeTypeUtils;
import top.recordsite.config.OssConfig;
import top.recordsite.entity.system.Media;
import top.recordsite.mapper.system.MediaMapper;

import java.util.List;

@SpringBootTest
public class MediaTest {

    @Autowired
    private MediaMapper mediaMapper;

    @Autowired
    private OssConfig ossConfig;

    @Test
    void upload() {
        System.out.println("ossConfig.getAliyun() = " + ossConfig.getAliyun());
        System.out.println("ossConfig.getQiniu() = " + ossConfig.getQiniu());
    }

    @Test
    void getType(){
        String type="image/png";
        String mime = MimeTypeUtils.parseMimeType(type).getSubtypeSuffix();
        System.out.println("mime = " + mime);
    }

    @Test
    void getAll(){
        List<Media> media = mediaMapper.selectList(null);
        media.forEach(System.out::println);
    }

}
