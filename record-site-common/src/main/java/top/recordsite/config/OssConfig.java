package top.recordsite.config;

import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "oss")
public class OssConfig {

    private QiniuConfig qiniu;

    private AliyunConfig aliyun;

    @Data
    public static class QiniuConfig {
        private String accessKey;
        private String secretKey;
        private String bucket;
        private String url;

        public String getUploadToken() {
            Auth auth = Auth.create(accessKey, secretKey);
            return auth.uploadToken(bucket);
        }

    }

    @Data
    public static class AliyunConfig {
        private String name;
        private int age;
    }

}
