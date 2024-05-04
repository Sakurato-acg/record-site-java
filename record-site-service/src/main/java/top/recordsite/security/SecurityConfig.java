package top.recordsite.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//开启权限校验注解
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationEntryPointImpl authenticationEntryPoint;

    @Autowired
    private AccessDeniedHandlerImpl accessDeniedHandler;

    @Autowired
    private JwtAuthenticationTokenFilter tokenFilter;

//    @Resource
//    private LimitFilter limitFilter;

    public static void main(String[] args) {
        String encode = new BCryptPasswordEncoder().encode("$2a$10$T.TlgkSw0arGg5OH95j9VO7tLj4l7jlh/n6q66vnHaY29WDBebmeK");
        System.out.println("encode = " + encode);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //关闭防范csrf攻击
                .csrf().disable()
                //不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 对于登录接口 允许匿名访问
                // 对于需要认证的接口,若未在SecurityContext中设置，则会在Security中捕捉
                .antMatchers("/user/logout").authenticated()
                .antMatchers("/user/admin/list").authenticated()
                .antMatchers("/anime/admin/list").authenticated()
                .antMatchers("/comment/add").authenticated()
                .antMatchers("/admin/**").authenticated()
                // 除上面外的所有请求全部不需要鉴权认证
                .anyRequest().permitAll();

//        //把token校验过滤器添加到过滤器链中
        http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
//        http.addFilterBefore(limitFilter, ChannelProcessingFilter.class);
        //
        //配置异常处理器
        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);
        http.logout().disable();

        http.cors();

    }

}
