package com.rbac.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Re-zero
 * @version 1.0
 * 登录响应对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    // JWT Token，前端需要将其保存在 localStorage 或 sessionStorage 中
    private String token;

    // 用户的权限标识列表，前端可根据此列表动态渲染菜单和按钮
    private List<String> permissions;
}