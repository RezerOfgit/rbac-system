package com.rbac.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 自定义权限校验服务，支持 *:*:* 通配符的动态权限匹配。
 * 注册为 Bean 名称 "ss"，供 @PreAuthorize("@ss.hasPermi(...)") 调用。
 * @author Re-zero
 * @version 1.0
 */
@Service("ss")
public class PermissionService {

    /** 所有权限标识的通配符 */
    private static final String ALL_PERMISSION = "*:*:*";

    /**
     * 校验当前用户是否具备指定权限。
     * @param permission 权限标识，如 sys:user:add
     * @return 有权限返回 true，否则返回 false
     */
    public boolean hasPermi(String permission) {
        if (!StringUtils.hasText(permission)) {
            return false;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return false;
        }

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        List<String> userPermissions = loginUser.getPermissions();

        if (userPermissions == null || userPermissions.isEmpty()) {
            return false;
        }

        return userPermissions.contains(ALL_PERMISSION) || userPermissions.contains(permission);
    }
}
