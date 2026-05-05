package com.rbac.config;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.util.regex.Pattern;

/**
 * Logback 日志脱敏转换器，通过重写 {@link MessageConverter} 实现日志内容的流式脱敏。
 * 配合 logback-spring.xml 中的 {@code <converter>} 声明使用。
 * @author Re-zero
 * @version 1.0
 */
public class SensitiveConverter extends MessageConverter {

    /** BCrypt 密文特征：$2a$10$... */
    private static final Pattern BCRYPT_PATTERN = Pattern.compile("\\$2a\\$\\d+\\$[A-Za-z0-9./]{53}");

    /** 邮箱格式（预留） */
    private static final Pattern EMAIL_PATTERN = Pattern.compile("[\\w.-]+@[\\w-]+\\.[\\w.]+");

    @Override
    public String convert(ILoggingEvent event) {
        String message = super.convert(event);
        if (message == null) {
            return null;
        }

        message = BCRYPT_PATTERN.matcher(message).replaceAll("******[SECURED_PASSWORD]******");

        // message = EMAIL_PATTERN.matcher(message).replaceAll("******[SECURED_EMAIL]******");

        return message;
    }
}
