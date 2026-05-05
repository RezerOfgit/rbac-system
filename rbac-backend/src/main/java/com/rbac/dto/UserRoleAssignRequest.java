package com.rbac.dto;

import lombok.Data;

import java.util.List;

/**
 * 用户角色分配请求参数。
 * @author Re-zero
 * @version 1.0
 */
@Data
public class UserRoleAssignRequest {

    private Long userId;
    private List<Long> roleIds;
}
