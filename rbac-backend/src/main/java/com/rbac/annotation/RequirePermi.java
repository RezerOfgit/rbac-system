package com.rbac.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Re-zero
 * @version 1.0
 * 自定义权限校验注解
 */
@Target(ElementType.METHOD) // 表明该注解只能用于方法上
@Retention(RetentionPolicy.RUNTIME) // 表明该注解在运行时有效，可以通过反射获取
public @interface RequirePermi {

    /**
     * 需要校验的权限标识，例如 "sys:user:add"
     */
    String value();
}
