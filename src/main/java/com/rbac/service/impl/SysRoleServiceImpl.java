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
 * @author Re-zero
 * @version 1.0
 * 角色业务实现类
 */
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final SysRoleMenuMapper roleMenuMapper;

    @Override
    public List<Long> getRoleMenuIds(Long roleId) {
        // 使用 QueryWrapper 查询该角色的所有关联记录
        QueryWrapper<SysRoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        List<SysRoleMenu> roleMenus = roleMenuMapper.selectList(queryWrapper);

        // 提取出 menu_id 列表
        return roleMenus.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // 面试考点：涉及到先删后插的多表操作，必须加事务
    public void assignMenuToRole(Long roleId, List<Long> menuIds) {
        // 1. 先清空该角色原有的所有菜单权限
        QueryWrapper<SysRoleMenu> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.eq("role_id", roleId);
        roleMenuMapper.delete(deleteWrapper);

        // 2. 如果前端传来了新的菜单ID列表，则批量插入
        if (menuIds != null && !menuIds.isEmpty()) {
            for (Long menuId : menuIds) {
                SysRoleMenu roleMenu = new SysRoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menuId);
                roleMenuMapper.insert(roleMenu); // 实际企业开发中这里推荐使用批量插入，这里为了演示逻辑使用单次插入
            }
        }
    }
}
