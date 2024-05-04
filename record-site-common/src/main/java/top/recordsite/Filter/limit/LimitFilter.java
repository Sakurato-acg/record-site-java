//package top.recordsite.Filter.limit;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.script.DefaultRedisScript;
//import org.springframework.scripting.support.ResourceScriptSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//import org.springframework.web.filter.OncePerRequestFilter;
//import top.recordsite.utils.WebUtils;
//import top.recordsite.vo.AppHttpCodeEnum;
//import top.recordsite.vo.Result;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.Resource;
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.Serializable;
//import java.util.List;
//
///**
// * @author fu
// * @description 限流切面实现
// * @date 2020/4/8 13:04
// */
//@Slf4j
//@Component
//public class LimitFilter extends OncePerRequestFilter {
//
//    private static final String UNKNOWN = "UNKNOWN";
//    private static final String LIMIT_LUA_PATH = "limit.lua";
//    //访问次数
//    private static final int limitCount = 10;
//    //给定时间范围，单位秒
//    private static final int limitPeriod = 1;
//
//    @Resource
//    private RedisTemplate<String, Serializable> redisTemplate;
//
//    private DefaultRedisScript<Long> script;
//
//    @PostConstruct
//    public void init() {
//        script = new DefaultRedisScript<>();
//        script.setResultType(Long.class);
//        ClassPathResource classPathResource = new ClassPathResource(LIMIT_LUA_PATH);
//        try {
//            classPathResource.getInputStream();//探测资源是否存在
//            script.setScriptSource(new ResourceScriptSource(classPathResource));
//        } catch (IOException e) {
//            log.error("未找到文件：{}", LIMIT_LUA_PATH);
//        }
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String key = getIpAddress();
////        ImmutableList<String> keys = ImmutableList.of(StringUtils.join("limiter:", key));
//        List<String> keys = List.of(key);
//
//        try {
//            Long count = redisTemplate.execute(script, keys, limitCount, limitPeriod);
//            log.info("try to access, this time count is {} for key: {}", count, key);
//            if (count != null && count.intValue() <= limitCount) {
//                filterChain.doFilter(request, response);
//            } else {
//                throw new RuntimeException("限流");
//            }
//
//        } catch (Throwable e) {
//            WebUtils.renderString(response, Result.error(AppHttpCodeEnum.LIMIT), 404);
//            log.error("ip：{} 限流", key);
//            //            if (e instanceof RuntimeException) {
////                throw new RuntimeException(e.getLocalizedMessage());
////            }
////            throw new RuntimeException("服务器出现异常，请稍后再试");
//
//        }
//    }
//
//    public String getIpAddress() {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        String ip = request.getHeader("x-forwarded-for");
//        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
//            ip = request.getHeader("Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
//            ip = request.getHeader("WL-Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
//            ip = request.getRemoteAddr();
//        }
//        return ip;
//    }
//
//
//}