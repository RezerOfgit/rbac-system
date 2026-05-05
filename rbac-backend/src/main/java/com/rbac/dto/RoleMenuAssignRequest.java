package com.rbac.dto;

import lombok.Data;

import java.util.List;

/**
 * 角色菜单分配请求参数。
 * @author Re-zero
 * @version 1.0
 */
@Data
public class RoleMenuAssignRequest {

    private Long roleId;
    private List<Long> menuIds;
}
