package com.rbac.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 登录请求参数。
 * @author Re-zero
 * @version 1.0
 */
@Data
public class LoginRequest {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}
