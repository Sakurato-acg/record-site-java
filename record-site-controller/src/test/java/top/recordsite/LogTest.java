package top.recordsite;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.recordsite.utils.IpUtils;

import java.util.stream.Stream;

@Slf4j
@SpringBootTest
public class LogTest {
    @Autowired
    private IpUtils ipUtils;

    @SneakyThrows
    @Test
    void test() {
        //已知一个json 字符串
        String json = "{\"name\":\"sojson\",\"age\":4,\"domain\":\"https://www.sojson.com\"}";
        //求优雅输出
        ObjectMapper mapper = new ObjectMapper();
        Object obj = mapper.readValue(json, Object.class);
        String value = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        String[] split = value.split("\n");
        Stream.of(split).forEach(item -> log.info("{}", item));

    }

}
