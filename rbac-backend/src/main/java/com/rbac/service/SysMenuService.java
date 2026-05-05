package com.rbac.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rbac.entity.SysMenu;

import java.util.List;

/**
 * @author Re-zero
 * @version 1.0
 */
public interface SysMenuService extends IService<SysMenu> {
    /**
     * 获取菜单树形列表
     */
    List<SysMenu> buildMenuTree();

    /**
     * 根据用户ID获取权限标识列表
     */
    List<String> getPermsByUserId(Long userId);
}
