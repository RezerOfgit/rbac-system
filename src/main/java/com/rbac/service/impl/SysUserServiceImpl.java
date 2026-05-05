package com.rbac.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rbac.entity.SysUser;
import com.rbac.entity.SysUserRole;
import com.rbac.mapper.SysUserMapper;
import com.rbac.mapper.SysUserRoleMapper;
import com.rbac.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Re-zero
 * @version 1.0
 * 用户业务实现类
 */
@Service
@RequiredArgsConstructor // 自动注入带有 final 关键字的 Bean
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final SysUserRoleMapper userRoleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRoleToUser(Long userId, List<Long> roleIds) {
        // 1. 先删除该用户原有的所有角色关联
        QueryWrapper<SysUserRole> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.eq("user_id", userId);
        userRoleMapper.delete(deleteWrapper);

        // 2. 插入前端传来的新角色关联
        if (roleIds != null && !roleIds.isEmpty()) {
            for (Long roleId : roleIds) {
                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                userRoleMapper.insert(userRole);
            }
        }
    }
}