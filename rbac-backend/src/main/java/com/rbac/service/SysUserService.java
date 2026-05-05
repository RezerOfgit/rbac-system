package com.rbac.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rbac.entity.SysUser;

import java.util.List;

/**
 * 用户业务接口。
 * @author Re-zero
 * @version 1.0
 */
public interface SysUserService extends IService<SysUser> {

    /** 为用户分配角色 */
    void assignRoleToUser(Long userId, List<Long> roleIds);
}