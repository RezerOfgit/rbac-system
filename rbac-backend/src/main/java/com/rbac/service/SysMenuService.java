package com.rbac.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rbac.entity.SysMenu;

import java.util.List;

/**
 * 菜单业务接口。
 * @author Re-zero
 * @version 1.0
 */
public interface SysMenuService extends IService<SysMenu> {

    /** 构建菜单树形结构 */
    List<SysMenu> buildMenuTree();

    /** 根据用户 ID 查询权限标识列表 */
    List<String> getPermsByUserId(Long userId);
}
