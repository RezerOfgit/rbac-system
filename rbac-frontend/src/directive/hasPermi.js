// src/directive/hasPermi.js
/**
 * v-hasPermi 按钮级权限控制指令。
 * 用法：v-hasPermi="['sys:user:add']"
 */
export default {
    inserted(el, binding) {
        const { value } = binding;
        const all_permission = '*:*:*';

        // 从 localStorage 读取当前用户的权限列表
        const permissions = JSON.parse(localStorage.getItem('permissions') || '[]');

        if (value && value instanceof Array && value.length > 0) {
            const permissionRoles = value;

            // 超级管理员放行，或精确匹配权限标识
            const hasPermission = permissions.some(p => {
                return all_permission === p || permissionRoles.includes(p);
            });

            // 无权限则移除该 DOM 元素
            if (!hasPermission) {
                el.parentNode && el.parentNode.removeChild(el);
            }
        } else {
            throw new Error(`请设置操作权限标签值，例如 v-hasPermi="['sys:user:add']"`);
        }
    }
};