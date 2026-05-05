package com.rbac.dto;

import lombok.Data;

import java.util.List;

/**
 * @author Re-zero
 * @version 1.0
 * 给用户分配角色的请求对象
 */
@Data
public class UserRoleAssignRequest {
    private Long userId;
    private List<Long> roleIds; // 支持一个用户分配多个角色
}
