package com.rbac.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色-菜单关联实体，对应 sys_role_menu 表。
 * @author Re-zero
 * @version 1.0
 */
@Data
@TableName("sys_role_menu")
public class SysRoleMenu implements Serializable {

    private Long roleId;
    private Long menuId;
}
