package com.rbac.utils;

import com.rbac.security.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Spring Security 上下文工具类，提供快捷获取当前登录用户信息的方法。
 * @author Re-zero
 * @version 1.0
 */
public class SecurityUtils {

    /** 超级管理员用户 ID，与初始化脚本保持一致 */
    private static final Long SUPER_ADMIN_ID = 1L;

    public static boolean isSuperAdmin(Long userId) {
        return SUPER_ADMIN_ID.equals(userId);
    }

    /**
     * 获取当前登录用户。
     * @return 登录用户对象，未登录时抛出异常
     */
    public static LoginUser getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof LoginUser) {
            return (LoginUser) authentication.getPrincipal();
        }
        throw new RuntimeException("获取用户信息异常：当前未登录或认证信息无效");
    }

    /**
     * 获取当前登录用户ID。
     */
    public static Long getUserId() {
        return getLoginUser().getUser().getId();
    }

    /**
     * 判断当前用户是否为超级管理员（拥有通配符权限 *:*:*）。
     */
    public static boolean isAdmin() {
        return getLoginUser().getPermissions().contains("*:*:*");
    }
}
