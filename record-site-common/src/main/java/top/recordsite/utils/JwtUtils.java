package top.recordsite.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

public class JwtUtils {

    //有效期
//    public static final Long JWT_TTL = 60 * 60 * 24 * 7L*1000;//七天
//    public static final Long JWT_TTL = 1000L*30;//1秒
    //设置秘钥明文
    public static final String JWT_KEY = "lpl";

    /**
     * @param subject token中要存放的数据
     * @return token
     */
    public static String createJwt(String subject) {
        JwtBuilder builder = getJwtBuilder(subject, null, getUUID());
        return builder.compact();
    }

    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();

        //设置过期时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
//        if (ttlMillis == null) {
//            ttlMillis = JWT_TTL;
//        }
//        long expMillis = nowMillis + ttlMillis;
//        Date expDate = new Date(expMillis);
        return Jwts.builder()
                .setId(uuid)              //唯一的ID
                .setSubject(subject)   // 主题  可以是JSON数据
                .setIssuer("sg")     // 签发者
                .setIssuedAt(now)      // 签发时间
                .signWith(algorithm, secretKey); //使用HS256对称加密算法签名, 第二个参数为秘钥
//                .setExpiration(expDate);
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.getDecoder().decode(JWT_KEY);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }

    public static void main(String[] args) throws Exception {
        Claims claims = parseJWT("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1N2YxOGE3ZTMyYWE0MjQyYWUzNzc2MzcxYzg3YzUxNCIsInN1YiI6IjMiLCJpc3MiOiJzZyIsImlhdCI6MTY5NTc4NDg3OSwiZXhwIjoxNjk1Nzg1NDg0fQ.6Wui8wEEfJiZHzLPwqK97vOn3AGYOE3DyGMu03wLuE4");
        System.out.println("claims.getSubject() = " + claims.getSubject());

    }
}
