package com.rbac.dto;

import lombok.Data;

import java.util.List;

/**
 * @author Re-zero
 * @version 1.0
 */
@Data
public class RoleMenuAssignRequest {
    private Long roleId;
    private List<Long> menuIds;
}
