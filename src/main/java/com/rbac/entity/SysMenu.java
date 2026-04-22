package com.rbac.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

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

}
