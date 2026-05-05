package com.rbac.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.access.AccessDeniedException;

/**
 * 全局异常处理器，将各类异常统一转换为 {@link R} 格式响应。
 * @author Re-zero
 * @version 1.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 自定义业务异常 */
    @ExceptionHandler(BusinessException.class)
    public R<?> handleBusinessException(BusinessException e) {
        log.warn("业务异常拦截: {}", e.getMessage());
        return R.fail(e.getCode(), e.getMessage());
    }

    /** 参数校验异常 */
    @ExceptionHandler(IllegalArgumentException.class)
    public R<?> handleIllegalArgument(IllegalArgumentException e) {
        log.warn("参数非法拦截: {}", e.getMessage());
        return R.fail(400, e.getMessage());
    }

    /** Spring Security 权限不足异常 */
    @ExceptionHandler(AccessDeniedException.class)
    public R<?> handleAccessDeniedException(AccessDeniedException e) {
        log.warn("越权访问拦截: {}", e.getMessage());
        // 使用我们在 R.java 中封装的 forbidden 语义化方法
        return R.forbidden("权限不足，不允许访问此接口");
    }

    /** 兜底：未预期的系统异常 */
    @ExceptionHandler(Exception.class)
    public R<?> handleException(Exception e) {
        log.error("系统内部异常: ", e);
        return R.fail("系统繁忙，请稍后再试");
    }
}
