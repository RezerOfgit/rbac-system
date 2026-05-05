package com.rbac.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义权限校验注解，标注在需要鉴权的方法上。
 * @author Re-zero
 * @version 1.0
 */
@Target(ElementType.METHOD)  // 仅限方法级别使用
@Retention(RetentionPolicy.RUNTIME) // 运行时保留，支持反射读取
public @interface RequirePermi {

    /**
     * 需要校验的权限标识，例如 "sys:user:add"
     */
    String value();
}
