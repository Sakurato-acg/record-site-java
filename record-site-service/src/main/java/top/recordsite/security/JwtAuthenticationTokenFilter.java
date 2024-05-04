package top.recordsite.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import top.recordsite.enums.system.UserDictionary;
import top.recordsite.utils.JwtUtils;
import top.recordsite.utils.RedisUtlis.RedisUtils;
import top.recordsite.utils.WebUtils;
import top.recordsite.vo.AppHttpCodeEnum;
import top.recordsite.vo.Result;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@SuppressWarnings("all")
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisUtils redisUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            //放行
            filterChain.doFilter(request, response);
            return;
        }
        //解析token，token是永不更新的
        String userId = null;
        try {
            Claims claims = JwtUtils.parseJWT(token);
            userId = claims.getSubject();
        } catch (Exception e) {
            log.error("token解析失败");
            //token 超时 token非法
            //throw new RuntimeException("token非法");
            Result result = null;
            if (e instanceof ExpiredJwtException) {
                result = Result.error(AppHttpCodeEnum.NO_OPERATOR_AUTH, "token过期");
            } else {
                result = Result.error(AppHttpCodeEnum.NEED_LOGIN, AppHttpCodeEnum.NEED_LOGIN.getMsg());
            }
            WebUtils.renderString(response, result);
            return;
        }


        //从redis中获取用户信息
        String redisKey = UserDictionary.user_prefix + userId;
        LoginUserDetail loginUser = (LoginUserDetail) redisUtils.getRedisString().get(redisKey);
        if (Objects.isNull(loginUser)) {
            //token过期了
//            throw new BusinessException(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, Result.error(AppHttpCodeEnum.NEED_LOGIN));
            return;
        } else if (loginUser.getUser().getStatus() == UserDictionary.status_disabled || loginUser.getUser().getDelFlag() == UserDictionary.del_disabled) {
            // TODO: 2023/11/10 不需要登录的接口，不用判断
            // TODO: 2023/10/30 redis 与 mysql数据不一致 , 系统对用户封禁后，缓存并没有更新
            WebUtils.renderString(response, Result.error(AppHttpCodeEnum.NEED_LOGIN).setMsg("用户封禁或已删除"));
            return;
        } else {
            //快过期了则刷新
            long ttl = redisUtils.ttl(redisKey);
            ttl = TimeUnit.SECONDS.toDays(ttl);
            if (ttl <= 1) {
                redisUtils.expire(redisKey, 7, TimeUnit.DAYS);
            }
        }

        //存入SecurityContextHolder
        //TODO 获取权限信息封装到Authentication中
        //authoriteis参数hasRole || hasAuthority用得到
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser.getUser(), null, loginUser.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行
        filterChain.doFilter(request, response);
    }
}