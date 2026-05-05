package com.rbac.aspect;

import com.rbac.annotation.RequirePermi;
import com.rbac.security.LoginUser;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 权限校验切面，拦截 {@link RequirePermi} 注解标注的方法，
 * 从 SecurityContextHolder 中取出当前用户的权限列表进行比对。
 * @author Re-zero
 * @version 1.0
 */
@Aspect
@Component
public class PermissionAspect {

    /** 超级管理员通配权限标识 */
    private static final String ALL_PERMISSION = "*:*:*";

    @Before("@annotation(com.rbac.annotation.RequirePermi)")
    public void checkPermission(JoinPoint joinPoint) {
        // 从 Security 上下文获取当前已认证用户
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<String> permissions = loginUser.getPermissions();

        // 超级管理员直接放行
        if (permissions.contains(ALL_PERMISSION)) {
            return;
        }

        // 反射获取目标方法上的注解值
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RequirePermi requirePermi = method.getAnnotation(RequirePermi.class);

        String requiredPermission = requirePermi.value();

        if (!permissions.contains(requiredPermission)) {
            throw new AccessDeniedException("权限不足，不允许访问此接口");
        }
    }
}