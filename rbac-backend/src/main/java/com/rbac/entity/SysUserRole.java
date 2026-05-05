package com.rbac.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户-角色关联实体，对应 sys_user_role 表。
 * @author Re-zero
 * @version 1.0
 */
@Data
@TableName("sys_user_role")
public class SysUserRole implements Serializable {

    private Long userId;
    private Long roleId;
}