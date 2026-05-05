package com.rbac.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rbac.entity.SysRole;

import java.util.List;

/**
 * @author Re-zero
 * @version 1.0
 * 角色业务接口
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 根据角色ID获取其对应的所有菜单ID
     */
    List<Long> getRoleMenuIds(Long roleId);

    /**
     * 给角色分配菜单权限
     */
    void assignMenuToRole(Long roleId, List<Long> menuIds);
}
