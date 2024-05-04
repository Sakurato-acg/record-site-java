package top.recordsite;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ZipUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;
import top.recordsite.utils.FilesUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@Slf4j
public class FileTest {
    @Test
    void range() {
        int random = (int) (1 + Math.random() * 10);
        System.out.println("random = " + random);
    }

    @Test
    void test() {

        File zipPath = new File("C:\\Users\\lpl\\Desktop\\新建文件夹\\eclipse\\plugins\\org.eclipse.justj.openjdk.hotspot.jre.full.win32.x86_64_16.0.2.v20210721-1149\\jre\\lib\\src.zip");
        File unzip = null;
        try {
            unzip = ZipUtil.unzip(zipPath, StandardCharsets.UTF_8);
        } catch (Exception e) {
            unzip = ZipUtil.unzip(zipPath, Charset.forName("GBK"));
        }
        List<File> fileList = FilesUtil.getAllFile(unzip);
        for (File file : Objects.requireNonNull(fileList)) {
            System.out.println("file.getName() = " + file.getName());
            String type = cn.hutool.core.io.FileUtil.getType(file);
            //md文件
//            if (Objects.equals(type, "md")) {
//                String content = cn.hutool.core.io.FilesUtil.readString(file, StandardCharsets.UTF_8);
//                // 定义正则表达式 pattern
//                Pattern pattern = Pattern.compile("<img\\s+src=\"([^\"}]+)\"", Pattern.CASE_INSENSITIVE);
//
//                // 创建 matcher
//                Matcher matcher = pattern.matcher(content);
//
//                // 查找匹配项
//                while (matcher.find()) {
//                    // 输出匹配到的图片链接
//                    System.out.println(matcher.group(1));
//                }
//                System.out.println("===");
//                // 定义正则表达式 pattern
//                Pattern pattern2 = Pattern.compile("\\[([^\\]]+)\\]\\(([^\\)]+)\\)", Pattern.CASE_INSENSITIVE);
//
//                // 创建 matcher
//                Matcher matcher2 = pattern2.matcher(content);
//
//                // 查找匹配项
//                while (matcher2.find()) {
//                    // 输出匹配到的路径
//                    System.out.println(matcher2.group(2));
//                }
//            }
        }
    }

    @Test
    void listen() throws IOException {
        String filePath = "C:\\Users\\lpl\\eclipse-workspace\\springboot_project\\record-site\\logs\\sys-all.log";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while (true) {
                line = br.readLine();
                if (StringUtils.hasText(line)) {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void md5() throws IOException {
        for (int i = 0; i < 5; i++) {
            String id = IdUtil.getSnowflakeNextIdStr();
            System.out.println("id = " + id);
        }

   /*     String filePath = "C:\\Users\\lpl\\eclipse-workspace\\springboot_project\\record-site\\logs\\sys-all.log";
        File file = new File(filePath);
        String md5 = MD5.create().digestHex(file);
        System.out.println("md5 = " + md5);
        md5= DigestUtils.md5DigestAsHex(new FileInputStream(file));
        System.out.println("md5 = " + md5);*/

    }

    @Test
    void test2() {
        long snowflakeNextId = IdUtil.getSnowflakeNextId();

        System.out.println("snowflakeNextId = " + snowflakeNextId);
    }
}
