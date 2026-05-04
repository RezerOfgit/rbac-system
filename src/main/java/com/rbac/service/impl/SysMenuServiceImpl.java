package com.rbac.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rbac.entity.SysMenu;
import com.rbac.mapper.SysMenuMapper;
import com.rbac.service.SysMenuService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Re-zero
 * @version 1.0
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Override
    public List<SysMenu> buildMenuTree() {
        // 1. 查询出数据库中所有的菜单数据（平时开发中这里可能需要加上按 order_num 排序）
        List<SysMenu> allMenus = this.list();

        // 2. 找到所有的顶级节点（parent_id 为 0 的通常是顶级目录）
        return allMenus.stream()
                .filter(menu -> menu.getParentId() == 0L)
                // 3. 针对每一个顶级节点，去寻找它的子节点
                .map(menu -> {
                    menu.setChildren(getChildren(menu, allMenus));
                    return menu;
                })
                .collect(Collectors.toList());
    }

    /**
     * 递归寻找子节点
     *
     * @param root 当前的父节点
     * @param allMenus 所有的菜单集合
     * @return 属于该父节点的子节点集合
     */
    private List<SysMenu> getChildren(SysMenu root, List<SysMenu> allMenus) {
        return allMenus.stream()
                // 过滤出父ID等于当前节点ID的菜单
                .filter(menu -> menu.getParentId().equals(root.getId()))
                // 递归调用，继续寻找子节点的子节点
                .map(menu -> {
                    menu.setChildren(getChildren(menu, allMenus));
                    return menu;
                })
                .collect(Collectors.toList());
    }
}
