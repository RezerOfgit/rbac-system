package com.rbac.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rbac.entity.SysRole;

import java.util.List;

/**
 * 角色业务接口。
 * @author Re-zero
 * @version 1.0
 */
public interface SysRoleService extends IService<SysRole> {

    /** 根据角色 ID 查询关联的菜单 ID 列表 */
    List<Long> getRoleMenuIds(Long roleId);

    /** 为角色分配菜单权限 */
    void assignMenuToRole(Long roleId, List<Long> menuIds);
}
