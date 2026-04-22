package com.rbac.common;

import lombok.Getter;

/**
 * @author Re-zero
 * @version 1.0
 * 自定义业务异常
 * 继承 RuntimeException，确保在 Spring 事务管理中抛出时能触发事务回滚
 */
@Getter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final int code; //使用 final 保证不可变性

    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public static BusinessException unauthorized(String msg) {
        return new BusinessException(401, msg);
    }

    public static BusinessException forbidden(String msg) {
        return new BusinessException(403, msg);
    }

    public static BusinessException badRequest(String msg) {
        return new BusinessException(400, msg);
    }
}