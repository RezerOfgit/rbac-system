package com.rbac.controller;

import com.rbac.annotation.RequirePermi;
import com.rbac.common.R;
import com.rbac.dto.UserRoleAssignRequest;
import com.rbac.entity.SysUser;
import com.rbac.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理接口，提供用户的增删查改及角色分配。
 * @author Re-zero
 * @version 1.0
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService userService;
    private final PasswordEncoder passwordEncoder;

    /** 查询用户列表（使用自定义 @RequirePermi 注解校验权限） */
    @GetMapping("/list")
    @RequirePermi("sys:user:list")
    public R<List<SysUser>> list() {
        return R.ok(userService.list());
    }

    /** 新增用户 */
    @PostMapping("/add")
    @PreAuthorize("@ss.hasPermi('sys:user:add')")
    public R<Void> add(@RequestBody SysUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return R.ok();
    }

    /** 修改用户（含平行越权校验） */
    @PutMapping("/update")
    @PreAuthorize("@ss.hasPermi('sys:user:update')")
    public R<Void> update(@RequestBody SysUser user) {
        userService.updateUser(user);
        return R.ok();
    }

    /** 删除用户（含平行越权校验） */
    @DeleteMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('sys:user:delete')")
    public R<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return R.ok();
    }

    /** 为用户分配角色 */
    @PostMapping("/assignRole")
    @PreAuthorize("@ss.hasPermi('sys:user:assign')")
    public R<Void> assignRole(@RequestBody UserRoleAssignRequest request) {
        userService.assignRoleToUser(request.getUserId(), request.getRoleIds());
        return R.ok();
    }
}