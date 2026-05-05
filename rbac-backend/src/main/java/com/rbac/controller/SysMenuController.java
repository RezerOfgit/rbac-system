package com.rbac.controller;

import com.rbac.common.R;
import com.rbac.entity.SysMenu;
import com.rbac.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Re-zero
 * @version 1.0
 */
@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class SysMenuController {

    private final SysMenuService menuService;

    /**
     * 获取菜单树形结构
     * 这个接口供前端页面渲染左侧菜单栏使用
     */
    @GetMapping("/tree")
    @PreAuthorize("@ss.hasPermi('sys:menu:list')") // 这里依然加锁保护
    public R<List<SysMenu>> getMenuTree() {
        return R.ok(menuService.buildMenuTree());
    }

    /**
     * 新增菜单
     */
    @PostMapping("/add")
    @PreAuthorize("@ss.hasPermi('sys:menu:add')")
    public R<Void> add(@RequestBody SysMenu sysMenu) {
        menuService.save(sysMenu);
        return R.ok();
    }

    // update 和 delete 逻辑与 user 类似，为了聚焦核心，这里暂略，后续可随时补全
}
