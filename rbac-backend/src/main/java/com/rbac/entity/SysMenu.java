package com.rbac.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 系统菜单/权限实体，对应 sys_menu 表。
 * menuType 取值：M-目录 C-菜单 F-按钮
 * @author Re-zero
 * @version 1.0
 */
@Data
@TableName("sys_menu")
public class SysMenu implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String menuName;
    private Long parentId;
    private Integer orderNum;
    private String path;
    /** 菜单类型：M-目录 C-菜单 F-按钮 */
    private String menuType;
    /** 权限标识，如 sys:user:add */
    private String perms;
    private String createBy;
    private Date createTime;
    private Date updateTime;

    /** 子菜单列表，非数据库字段 */
    @TableField(exist = false)
    private List<SysMenu> children = new ArrayList<>();
}
