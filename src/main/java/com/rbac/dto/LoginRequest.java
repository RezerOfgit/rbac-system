package com.rbac.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Re-zero
 * @version 1.0
 * 登录请求对象
 */
@Data
public class LoginRequest {

    // 使用 validation 注解确保参数不为空
    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}
