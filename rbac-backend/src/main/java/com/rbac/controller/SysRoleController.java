package com.rbac.controller;

import com.rbac.common.R;
import com.rbac.dto.RoleMenuAssignRequest;
import com.rbac.entity.SysRole;
import com.rbac.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理接口，提供角色的增删查及菜单权限分配。
 * @author Re-zero
 * @version 1.0
 */
@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class SysRoleController {

    private final SysRoleService roleService;

    /** 查询角色列表 */
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('sys:role:list')")
    public R<List<SysRole>> list() {
        return R.ok(roleService.list());
    }

    /** 新增角色 */
    @PostMapping("/add")
    @PreAuthorize("@ss.hasPermi('sys:role:add')")
    public R<Void> add(@RequestBody SysRole role) {
        roleService.save(role);
        return R.ok();
    }

    /** 为角色分配菜单权限 */
    @PostMapping("/assignMenu")
    @PreAuthorize("@ss.hasPermi('sys:role:assign')")
    public R<Void> assignMenu(@RequestBody RoleMenuAssignRequest request) {
        roleService.assignMenuToRole(request.getRoleId(), request.getMenuIds());
        return R.ok();
    }
}
