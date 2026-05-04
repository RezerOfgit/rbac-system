package com.rbac.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rbac.entity.SysUser;
import com.rbac.mapper.SysUserMapper;
import com.rbac.service.SysUserService;
import org.springframework.stereotype.Service;

/**
 * @author Re-zero
 * @version 1.0
 * 用户业务实现类
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
}
