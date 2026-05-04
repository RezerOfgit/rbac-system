package com.rbac.controller;

import com.rbac.common.R;
import com.rbac.entity.SysUser;
import com.rbac.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Re-zero
 * @version 1.0
 * 系统用户控制器
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService userService;
    private final PasswordEncoder passwordEncoder; // 用于新增用户时加密密码

    /**
     * 查询用户列表
     * @PreAuthorize("@ss.hasPermi('sys:user:list')") 解析：
     * 1. @ss 代表调用 Spring 容器中名为 ss 的 Bean（即我们的 PermissionService）
     * 2. 执行它的 hasPermi 方法，传入 'sys:user:list'
     * 3. 如果返回 true，放行；返回 false，Spring Security 会抛出 AccessDeniedException，进而被我们封装的 R.forbidden() 拦截返回 403。
     */
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('sys:user:list')")
    public R<List<SysUser>> list() {
        // 调用 MP 自带的 list() 方法查询所有未被逻辑删除的用户
        return R.ok(userService.list());
    }

    /**
     * 新增用户
     */
    @PostMapping("/add")
    @PreAuthorize("@ss.hasPermi('sys:user:add')")
    public R<Void> add(@RequestBody SysUser user) {
        // 实际开发中还需要校验用户名是否重复等逻辑，这里先跑通核心流程
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return R.ok();
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('sys:user:delete')")
    public R<Void> delete(@PathVariable Long id) {
        // MP 会自动将其转化为逻辑删除 UPDATE del_flag = 1
        userService.removeById(id);
        return R.ok();
    }
}