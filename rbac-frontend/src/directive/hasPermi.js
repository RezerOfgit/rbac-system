// src/directive/hasPermi.js
/**
 * v-hasPermi 按钮级权限控制指令
 */
export default {
    // inserted 钩子：当被绑定的元素插入到 DOM 中时执行
    inserted(el, binding) {
        // 获取按钮上绑定的权限值，例如 v-hasPermi="['sys:user:add']"
        const { value } = binding;
        // 超级管理员的通配符
        const all_permission = '*:*:*';

        // 从本地存储中获取当前用户的权限数组（注意要反序列化）
        const permissions = JSON.parse(localStorage.getItem('permissions') || '[]');

        // 判断有没有传入权限值
        if (value && value instanceof Array && value.length > 0) {
            const permissionRoles = value;

            // 判断用户是否拥有该权限，或者是否是超级管理员
            const hasPermission = permissions.some(p => {
                return all_permission === p || permissionRoles.includes(p);
            });

            // 如果没有权限，直接把这个 DOM 节点从父元素中移除
            if (!hasPermission) {
                el.parentNode && el.parentNode.removeChild(el);
            }
        } else {
            throw new Error(`请设置操作权限标签值，例如 v-hasPermi="['sys:user:add']"`);
        }
    }
};