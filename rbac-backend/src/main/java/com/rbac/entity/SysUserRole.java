package com.rbac.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Re-zero
 * @version 1.0
 * 用户-角色关联表
 */
@Data
@TableName("sys_user_role")
public class SysUserRole implements Serializable {
    private Long userId; //用户ID
    private Long roleId; //角色ID

}