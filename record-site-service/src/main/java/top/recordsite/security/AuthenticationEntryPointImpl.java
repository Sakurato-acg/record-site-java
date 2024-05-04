package top.recordsite.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import top.recordsite.utils.WebUtils;
import top.recordsite.vo.AppHttpCodeEnum;
import top.recordsite.vo.Result;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证过程中出现的异常
 */
@Component
@SuppressWarnings("all")
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
//        authException.printStackTrace();

        Result result = null;
        if (authException instanceof BadCredentialsException) {
            //用户名或密码错误
            result = Result.error(AppHttpCodeEnum.NEED_LOGIN).setMsg("账号或密码错误");

        } else if (authException instanceof InsufficientAuthenticationException) {
            //需要登录后操作
            result = Result.error(AppHttpCodeEnum.NEED_LOGIN, AppHttpCodeEnum.NEED_LOGIN.getMsg());

        } else {
            //出现错误
            result = Result.error(AppHttpCodeEnum.BUSINESS, authException.getMessage());
        }

        //响应给前端
        WebUtils.renderString(response, result);
    }
}