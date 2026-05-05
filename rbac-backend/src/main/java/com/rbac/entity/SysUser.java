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
 * 系统用户表
 */
@Data //Lombok 注解：自动生成 getter/setter、toString 等方法
@TableName("sys_user") //MyBatis-Plus 这个类对应数据库里的哪张表
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * @TableId 说明这是主键，IdType.AUTO 表示让数据库自增（对应我们在 SQL 里写的 AUTO_INCREMENT）
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id; //用户ID
    private String username; //用户名
    private String password; //密码
    private String email; //邮箱
    private Integer status; //账号状态 (1正常 0停用)
    private String createBy; //创建人
    private Date createTime; //创建时间
    private Date updateTime; //更新时间

    /**
     * 逻辑删除标志 (0正常 1删除)
     * @TableLogic 极其重要！加上它之后，你调用 mp 的 deleteById 方法，
     * 底层不会执行 DELETE SQL，而是执行 UPDATE sys_user SET del_flag = 1 WHERE id = ?
     * 并且以后所有的 SELECT 查询，都会自动带上 WHERE del_flag = 0。
     */
    @TableLogic
    private Integer delFlag;
}
