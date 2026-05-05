// src/router/index.js
import Vue from 'vue';
import VueRouter from 'vue-router';
import { Message } from 'element-ui';

Vue.use(VueRouter);

// 定义路由规则
const routes = [
    {
        path: '/login',
        name: 'Login',
        // 路由懒加载，优化首屏加载速度
        component: () => import('@/views/Login.vue')
    },
    {
        path: '/',
        name: 'Home',
        component: () => import('@/views/Home.vue')
    }
];

const router = new VueRouter({
    mode: 'history', // 去掉 URL 中的 # 号
    routes
});

// 全局前置守卫：未登录时强制跳转至登录页
router.beforeEach((to, from, next) => {
    // 尝试获取本地存储的 token
    const token = localStorage.getItem('jwt_token');

    // 如果用户要去的页面不是登录页，且没有 token
    if (to.path !== '/login' && !token) {
        Message.warning('请先登录');
        // 强制跳转到登录页
        next('/login');
    } else {
        // 其他情况（已经登录，或者要去的就是登录页），直接放行
        next();
    }
});

export default router;