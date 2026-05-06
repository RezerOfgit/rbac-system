package com.rbac.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rbac.entity.SysUser;
import com.rbac.entity.SysUserRole;
import com.rbac.mapper.SysUserMapper;
import com.rbac.mapper.SysUserRoleMapper;
import com.rbac.service.SysUserService;
import com.rbac.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(SysUser user) {
        Long currentUserId = SecurityUtils.getUserId();

        // 非超级管理员只能修改自己的信息
        if (!SecurityUtils.isAdmin() && !currentUserId.equals(user.getId())) {
            throw new AccessDeniedException("发生平行越权异常：您只能修改属于您自己的数据！");
        }

        return updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUser(Long userId) {
        // 超级管理员账号禁止删除
        if (SecurityUtils.isSuperAdmin(userId)) {
            throw new AccessDeniedException("超级管理员账号不允许删除！");
        }

        Long currentUserId = SecurityUtils.getUserId();

        // 非超级管理员只能删除自己的账号
        if (!SecurityUtils.isAdmin() && !currentUserId.equals(userId)) {
            throw new AccessDeniedException("权限不足：只能删除自己的账号！");
        }

        return removeById(userId);
    }
}