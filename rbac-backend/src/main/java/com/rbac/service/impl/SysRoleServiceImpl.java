package com.rbac.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rbac.entity.SysRole;
import com.rbac.entity.SysRoleMenu;
import com.rbac.mapper.SysRoleMapper;
import com.rbac.mapper.SysRoleMenuMapper;
import com.rbac.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色业务实现。
 * @author Re-zero
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final SysRoleMenuMapper roleMenuMapper;

    @Override
    public List<Long> getRoleMenuIds(Long roleId) {
        QueryWrapper<SysRoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        List<SysRoleMenu> roleMenus = roleMenuMapper.selectList(queryWrapper);

        return roleMenus.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignMenuToRole(Long roleId, List<Long> menuIds) {
        // 先清除原有权限，再重新写入
        QueryWrapper<SysRoleMenu> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.eq("role_id", roleId);
        roleMenuMapper.delete(deleteWrapper);

        if (menuIds != null && !menuIds.isEmpty()) {
            for (Long menuId : menuIds) {
                SysRoleMenu roleMenu = new SysRoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menuId);
                roleMenuMapper.insert(roleMenu);
            }
        }
    }
}
