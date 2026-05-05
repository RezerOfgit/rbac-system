package com.rbac.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Re-zero
 * @version 1.0
 * 角色-菜单关联表
 */
@Data
@TableName("sys_role_menu")
public class SysRoleMenu implements Serializable {
    private Long roleId; //角色ID
    private Long menuId; //菜单ID

}
