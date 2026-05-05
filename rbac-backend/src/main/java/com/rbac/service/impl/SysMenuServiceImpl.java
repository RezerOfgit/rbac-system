package com.rbac.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rbac.entity.SysMenu;
import com.rbac.mapper.SysMenuMapper;
import com.rbac.service.SysMenuService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单业务实现。
 * @author Re-zero
 * @version 1.0
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Override
    public List<SysMenu> buildMenuTree() {

        List<SysMenu> allMenus = this.list();

        // 筛选顶级节点（parentId == 0），递归填充子节点
        return allMenus.stream()
                .filter(menu -> menu.getParentId() == 0L)
                .map(menu -> {
                    menu.setChildren(getChildren(menu, allMenus));
                    return menu;
                })
                .collect(Collectors.toList());
    }

    /**
     * 递归查找当前节点的子菜单。
     * @param root     当前父节点
     * @param allMenus 全量菜单列表
     * @return 子节点列表
     */
    private List<SysMenu> getChildren(SysMenu root, List<SysMenu> allMenus) {
        return allMenus.stream()
                .filter(menu -> menu.getParentId().equals(root.getId()))
                .map(menu -> {
                    menu.setChildren(getChildren(menu, allMenus));
                    return menu;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getPermsByUserId(Long userId) {
        return baseMapper.selectPermsByUserId(userId);
    }
}
