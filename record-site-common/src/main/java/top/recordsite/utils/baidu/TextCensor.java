package top.recordsite.utils.baidu;


import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 文本审核接口
 */
public class TextCensor {

    /**
     * 重要提示代码中所需工具类
     * FilesUtil,Base64Util,HttpUtil,GsonUtils请从
     * https://ai.baidu.com/file/658A35ABAB2D404FBF903F64D47C1F72
     * https://ai.baidu.com/file/C8D81F3301E24D2892968F09AE1AD6E2
     * https://ai.baidu.com/file/544D677F5D4E4F17B4122FBD60DB82B3
     * https://ai.baidu.com/file/470B3ACCA3FE43788B5A963BF0B625F3
     * 下载
     */
    public static String TextCensor(String msg) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/solution/v1/text_censor/v2/user_defined";
        try {
            String param = "text=" + URLEncoder. encode(
                    msg,
                    StandardCharsets.UTF_8
            );

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = "24.b41fe72e37e4f3702361fe9ef983b24e.2592000.1704274157.282335-44298157";

            String result = HttpUtil.post(url, accessToken, param);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) {
        String msg="猜十二生肖?(奔逸绝尘打一个生肖)(一句真言:奔逸绝尘)猜什么动物?<p><p>###040+#加·威·∨·信：【 W855～329 】 为您解--答";
        String s = TextCensor.TextCensor(msg);

        System.out.println("s = " + s);
    }
}