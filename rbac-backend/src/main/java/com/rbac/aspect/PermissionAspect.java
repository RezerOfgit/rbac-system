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
 * @author Re-zero
 * @version 1.0
 * 自定义权限校验切面
 * 面试重点：使用 AOP 拦截自定义注解，实现无侵入式的权限校验
 */
@Aspect
@Component
public class PermissionAspect {

    private static final String ALL_PERMISSION = "*:*:*";

    // @Before 表示在目标方法执行之前拦截
    // "@annotation(com.rbac.annotation.RequirePermi)" 表示拦截所有标注了此注解的方法
    @Before("@annotation(com.rbac.annotation.RequirePermi)")
    public void checkPermission(JoinPoint joinPoint) {
        // 1. 从当前 Security 上下文中获取已登录的用户信息（包含权限列表）
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<String> permissions = loginUser.getPermissions();

        // 2. 如果是超级管理员，直接放行
        if (permissions.contains(ALL_PERMISSION)) {
            return;
        }

        // 3. 通过反射，获取当前被拦截的方法上的注解值
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RequirePermi requirePermi = method.getAnnotation(RequirePermi.class);

        String requiredPermission = requirePermi.value();

        // 4. 核心比对逻辑：如果用户的权限列表不包含该接口所需的权限，则拦截并抛出异常
        if (!permissions.contains(requiredPermission)) {
            throw new AccessDeniedException("权限不足，不允许访问此接口");
        }
    }
}