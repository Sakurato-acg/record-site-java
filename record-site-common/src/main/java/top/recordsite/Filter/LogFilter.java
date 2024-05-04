//package top.recordsite.Filter;
//
//import cn.hutool.core.util.IdUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.slf4j.MDC;
//import org.springframework.core.Ordered;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Slf4j
//@Component
//public class LogFilter extends OncePerRequestFilter implements Ordered {
//
//    private final static String MDC_TRACE_ID = "traceId";
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String mdcTraceId = getMdcTraceId();
//        MDC.put(MDC_TRACE_ID, mdcTraceId);
//        log.info("纯字符串信息的info级别日志");
//        filterChain.doFilter(request, response);
//        MDC.clear();
//    }
//
//    private String getMdcTraceId() {
//        String idStr = IdUtil.getSnowflakeNextIdStr();
//        return String.join("_", MDC_TRACE_ID, idStr);
//    }
//
//    @Override
//    public int getOrder() {
//        return Ordered.HIGHEST_PRECEDENCE;
//    }
//}
