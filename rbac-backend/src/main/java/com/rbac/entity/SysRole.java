package com.rbac.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Re-zero
 * @version 1.0
 * 系统角色表
 */
@Data
@TableName("sys_role")
public class SysRole implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id; //角色ID
    private String roleName; //角色名称 (如：管理员)
    private String roleKey; //角色权限字符串 (如：admin)
    private Integer status; //角色状态 (1正常 0停用)
    private String createBy; //创建人
    private Date createTime; //创建时间
    private Date updateTime; //更新时间
    @TableLogic
    private Integer delFlag; //逻辑删除标志 (0正常 1删除)

}
