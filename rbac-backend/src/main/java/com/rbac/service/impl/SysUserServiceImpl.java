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
 * 用户业务实现。
 * @author Re-zero
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final SysUserRoleMapper userRoleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRoleToUser(Long userId, List<Long> roleIds) {
        // 先清除原有角色关联，再重新写入
        QueryWrapper<SysUserRole> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.eq("user_id", userId);
        userRoleMapper.delete(deleteWrapper);

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