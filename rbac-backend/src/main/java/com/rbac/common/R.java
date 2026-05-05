package com.rbac.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Re-zero
 * @version 1.0
 * 统一 API 响应结果封装
 * @param <T>
 */
@Data
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer code; //业务状态码 (200 成功, 4xx 客户端异常, 5xx 服务端异常)
    private String message; //提示信息
    private T data; //响应数据
    private long timestamp; //请求时间戳，方便追溯和调试

    // 私有构造器，强制使用静态工厂方法
    private R() {
        this.timestamp = System.currentTimeMillis();
    }

    // 成功响应
    public static <T> R<T> ok() {
        return ok(null);
    }

    public static <T> R<T> ok(T data) {
        R<T> r = new R<>();
        r.setCode(200);
        r.setMessage("操作成功");
        r.setData(data);
        return r;
    }

    // 失败响应
    public static <T> R<T> fail(int code, String message) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setMessage(message);
        return r;
    }

    public static <T> R<T> fail(String message) {
        return fail(500, message);
    }

    // 针对 Security 权限校验的专属快捷方法
    public static <T> R<T> unauthorized(String message) {
        return fail(401, message);
    }

    public static <T> R<T> forbidden(String message) {
        return fail(403, message);
    }
}
