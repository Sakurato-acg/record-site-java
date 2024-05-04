package top.recordsite;

import cn.hutool.core.util.IdUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class HutoolTest {

    @Test
    void id(){
        long first = IdUtil.getSnowflakeNextId();
        long second = IdUtil.getSnowflakeNextId();

        System.err.println("first = " + first);
        System.err.println("second = " + second);
    }


}
