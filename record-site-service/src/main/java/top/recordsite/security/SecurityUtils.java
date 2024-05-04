package top.recordsite.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import top.recordsite.entity.User;
import top.recordsite.exception.BusinessException;
import top.recordsite.vo.AppHttpCodeEnum;

import java.util.Objects;

/**
 * @Author 三更  B站： https://space.bilibili.com/663528522
 */
public class SecurityUtils {

    /**
     * 获取用户
     **/
    public static User getLoginUser() {
        Object principal = getAuthentication().getPrincipal();
        if (principal!="anonymousUser"){
            return (User)principal ;
        }else {
            throw new BusinessException(AppHttpCodeEnum.NEED_LOGIN);
        }
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static Boolean isAdmin() {
        Integer id = Objects.requireNonNull(getLoginUser()).getId();
        return (id != null && id.equals(1) )|| getAuthentication().getAuthorities().contains("admin");
    }

    public static Integer getUserId() {
        return Objects.requireNonNull(getLoginUser()).getId();
    }
}