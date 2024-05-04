package top.recordsite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class RecordSiteApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(RecordSiteApplication.class, args);
//        StringBuffer buffer=new StringBuffer();
//        buffer.append()
    }

}
