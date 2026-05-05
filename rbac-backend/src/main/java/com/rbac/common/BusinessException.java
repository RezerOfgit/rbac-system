package com.rbac.common;

import lombok.Getter;

/**
 * 自定义业务异常，继承 RuntimeException 以支持 Spring 事务自动回滚。
 * @author Re-zero
 * @version 1.0
 */
@Getter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final int code;

    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    /** 返回 401 未授权异常 */
    public static BusinessException unauthorized(String msg) {
        return new BusinessException(401, msg);
    }

    /** 返回 403 禁止访问异常 */
    public static BusinessException forbidden(String msg) {
        return new BusinessException(403, msg);
    }

    /** 返回 400 请求参数异常 */
    public static BusinessException badRequest(String msg) {
        return new BusinessException(400, msg);
    }
}