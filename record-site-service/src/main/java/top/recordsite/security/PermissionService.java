//package top.recordsite.security;
//
//import org.springframework.stereotype.Service;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Objects;
//
//@Service("ps")
//public class PermissionService {
//
//    /**
//     * @param strs 要求的权限
//     * @return 判断值
//     */
//    public boolean hasAnyRole(String... strs) {
//        List<String> need = Arrays.stream(strs).toList();
//
//        if (SecurityUtils.isAdmin()) {
//            return true;
//        }
//
//        //用户拥有的权限
////        List<String> permissions = Objects.requireNonNull(SecurityUtils.getLoginUser()).getPermissions();
//
//        //用户所需权限满足strs中的一个
//        for (String permission : permissions) {
//            if (need.contains(permission)) {
//                return true;
//            }
//        }
//
//        return false;
//    }
//}
