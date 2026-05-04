package com.rbac.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rbac.entity.SysUser;

/**
 * @author Re-zero
 * @version 1.0
 * 用户业务接口
 */
public interface SysUserService extends IService<SysUser> {
    // MyBatis-Plus 已经为我们提供了 save, removeById, updateById, list 等标准方法
    // 后续如果有复杂的联表查询，我们再在这里补充自定义方法
}