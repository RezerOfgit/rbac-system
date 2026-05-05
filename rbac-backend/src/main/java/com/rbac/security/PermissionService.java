package com.rbac.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author Re-zero
 * @version 1.0
 * 自定义权限校验服务
 * 面试亮点：摆脱原生硬编码匹配，实现支持 "*:*:*" 超级管理员通配符的动态权限引擎
 * 注意这里的注解 @Service("ss")，"ss" 是我们给它起的名字，方便在注解中简写调用
 */
@Service("ss")
public class PermissionService {

    /** 所有权限标识的通配符 */
    private static final String ALL_PERMISSION = "*:*:*";

    /**
     * 验证用户是否具备某权限
     *
     * @param permission 接口需要的权限标识（例如："sys:user:add"）
     * @return true 代表有权限，false 代表无权限（会被抛出 403 异常）
     */
    public boolean hasPermi(String permission) {
        if (!StringUtils.hasText(permission)) {
            return false;
        }

        // 1. 获取当前登录用户的认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return false;
        }

        // 2. 获取我们在 JwtAuthenticationFilter 中存入的 LoginUser 对象
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        List<String> userPermissions = loginUser.getPermissions();

        if (userPermissions == null || userPermissions.isEmpty()) {
            return false;
        }

        // 3. 判断是否包含超级管理员通配符，或者精确匹配目标权限
        return userPermissions.contains(ALL_PERMISSION) || userPermissions.contains(permission);
    }
}
