//package top.recordsite.Filter;
//
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.annotation.Order;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//import top.recordsite.utils.WebUtils;
//import top.recordsite.vo.AppHttpCodeEnum;
//import top.recordsite.vo.Result;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.concurrent.TimeUnit;
//
//@Order(1)
//@Component
//@Slf4j
//public class AccessLimitFilter implements Filter {
//
//    //统计时间
//    @SuppressWarnings("all")
//    private final int time = 5;
//
//    //限流次数
//    @SuppressWarnings("all")
//    private final int limit = 100;
//
//
//    @Autowired
//    private RedisTemplate redisTemplate;
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        Filter.super.init(filterConfig);
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//
//        //拼接key = ip + api 限流
//        String key = "AccessLimit  " + getRemoteIP(request) + request.getRequestURI();
//
//
//        boolean exists = Boolean.TRUE.equals(redisTemplate.hasKey(key));
//        if (!exists) {
//            //初次访问
//            Boolean ifAbsent = redisTemplate.opsForValue().setIfAbsent(key, 1, time, TimeUnit.SECONDS);
//            filterChain.doFilter(servletRequest, servletResponse);
//            return;
//        }
//
//        //访问过了
//        int count = (int) redisTemplate.opsForValue().get(key);
////        log.error("count={}", count);
//        if (count > limit) {
//            WebUtils.renderString((HttpServletResponse) servletResponse, Result.error(AppHttpCodeEnum.SYSTEM_ERROR).setMsg("限流"));
//            return;
//        }
//        redisTemplate.opsForValue().increment(key);
//        filterChain.doFilter(servletRequest, servletResponse);
//    }
//
//    @Override
//    public void destroy() {
//        Filter.super.destroy();
//    }
//
//    //获取真实IP一
//    public String getRemoteIP(HttpServletRequest request) {
//        if (request.getHeader("x-forwarded-for") == null) {
//            return request.getRemoteAddr();
//        }
//        return request.getHeader("x-forwarded-for");
//    }
//}
