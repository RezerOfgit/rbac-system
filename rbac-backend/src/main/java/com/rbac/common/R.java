package com.rbac.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一 API 响应结果封装。
 * @author Re-zero
 * @version 1.0
 * @param <T> 响应数据类型
 */
@Data
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 业务状态码，200 成功，4xx 客户端异常，5xx 服务端异常 */
    private Integer code;

    /** 提示信息 */
    private String message;

    /** 响应数据 */
    private T data;

    /** 请求时间戳 */
    private long timestamp;

    private R() {
        this.timestamp = System.currentTimeMillis();
    }

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

    public static <T> R<T> fail(int code, String message) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setMessage(message);
        return r;
    }

    public static <T> R<T> fail(String message) {
        return fail(500, message);
    }

    public static <T> R<T> unauthorized(String message) {
        return fail(401, message);
    }

    public static <T> R<T> forbidden(String message) {
        return fail(403, message);
    }
}
