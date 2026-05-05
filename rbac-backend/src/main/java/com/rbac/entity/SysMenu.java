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
 * @author Re-zero
 * @version 1.0
 * 系统菜单/权限表
 */
@Data
@TableName("sys_menu")
public class SysMenu implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id; //菜单/权限ID
    private String menuName; //菜单名称
    private Long parentId; //父菜单ID
    private Integer orderNum; //显示顺序
    private String path; //路由地址
    private String menuType; //菜单类型 (M目录 C菜单 F按钮 )
    private String perms; //权限标识 (如：sys:user:add)
    private String createBy; //创建人
    private Date createTime; //创建时间
    private Date updateTime; //更新时间

    /**
     * 子菜单列表（非数据库字段）
     * @TableField(exist = false) 告诉 MyBatis-Plus 插入/查询时忽略这个字段
     */
    @TableField(exist = false)
    private List<SysMenu> children = new ArrayList<>();
}
