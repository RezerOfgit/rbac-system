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
 * @author Re-zero
 * @version 1.0
 * 系统角色控制器
 */
@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class SysRoleController {

    private final SysRoleService roleService;

    /**
     * 查询角色列表
     */
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('sys:role:list')")
    public R<List<SysRole>> list() {
        return R.ok(roleService.list());
    }

    /**
     * 新增角色
     */
    @PostMapping("/add")
    @PreAuthorize("@ss.hasPermi('sys:role:add')")
    public R<Void> add(@RequestBody SysRole role) {
        roleService.save(role);
        return R.ok();
    }

    /**
     * 给角色分配菜单权限（极其关键！）
     */
    @PostMapping("/assignMenu")
    @PreAuthorize("@ss.hasPermi('sys:role:assign')")
    public R<Void> assignMenu(@RequestBody RoleMenuAssignRequest request) {
        roleService.assignMenuToRole(request.getRoleId(), request.getMenuIds());
        return R.ok();
    }
}
