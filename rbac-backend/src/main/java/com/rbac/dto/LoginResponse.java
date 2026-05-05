package com.rbac.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 登录响应参数。
 * @author Re-zero
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    /** JWT Token */
    private String token;

    /** 权限标识列表，用于前端动态渲染菜单和按钮 */
    private List<String> permissions;
}