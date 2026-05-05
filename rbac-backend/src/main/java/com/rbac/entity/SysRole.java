package com.rbac.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统角色实体，对应 sys_role 表。
 * @author Re-zero
 * @version 1.0
 */
@Data
@TableName("sys_role")
public class SysRole implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String roleName;
    private String roleKey;
    /** 状态：1-正常 0-停用 */
    private Integer status;
    private String createBy;
    private Date createTime;
    private Date updateTime;
    /** 逻辑删除标志：0-正常 1-已删除 */
    @TableLogic
    private Integer delFlag;
}
