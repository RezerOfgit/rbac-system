package com.rbac.config;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.util.regex.Pattern;

/**
 * @author Re-zero
 * @version 1.0
 * Logback 自定义日志脱敏转换器
 * 面试亮点：通过重写 Logback 底层渲染逻辑，实现在不影响正常业务逻辑的前提下，对控制台/文件日志进行流式脱敏。
 */
public class SensitiveConverter extends MessageConverter {

    // 预编译正则表达式，提高匹配性能。匹配 BCrypt 密文特征 ($2a$10$...)
    private static final Pattern BCRYPT_PATTERN = Pattern.compile("\\$2a\\$\\d+\\$[A-Za-z0-9./]{53}");

    // 如果后续有邮箱脱敏需求，也可以加在这里
    private static final Pattern EMAIL_PATTERN = Pattern.compile("[\\w.-]+@[\\w-]+\\.[\\w.]+");

    @Override
    public String convert(ILoggingEvent event) {
        // 获取原始的日志信息
        String message = super.convert(event);
        if (message == null) {
            return null;
        }

        // 1. 脱敏 BCrypt 密码
        message = BCRYPT_PATTERN.matcher(message).replaceAll("******[SECURED_PASSWORD]******");

        // 2. 脱敏邮箱 (预留功能)
        // message = EMAIL_PATTERN.matcher(message).replaceAll("******[SECURED_EMAIL]******");

        return message;
    }
}
