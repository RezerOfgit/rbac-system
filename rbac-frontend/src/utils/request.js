// src/utils/request.js
import axios from 'axios';
import { Message } from 'element-ui';

// 1. 创建一个 axios 实例
const service = axios.create({
    // 配置你的后端 API 基础路径，假设后端运行在 8080 端口
    baseURL: 'http://localhost:8080',
    timeout: 5000 // 请求超时时间
});

// 2. 请求拦截器 (Request Interceptor) - 核心考点
service.interceptors.request.use(
    config => {
        // 尝试从本地存储中获取我们在登录成功后保存的 Token
        const token = localStorage.getItem('jwt_token');
        if (token) {
            // 如果存在 Token，则自动放入请求头中。注意 'Bearer ' 后面的空格！
            config.headers['Authorization'] = 'Bearer ' + token;
        }
        return config;
    },
    error => {
        console.error('请求发送失败:', error);
        return Promise.reject(error);
    }
);

// 3. 响应拦截器 (Response Interceptor)
service.interceptors.response.use(
    response => {
        // 拿到后端返回的封装数据 R
        const res = response.data;

        // 如果后端的 code 不是 200，说明业务线报错或权限不足
        if (res.code !== 200) {
            Message({
                message: res.message || '系统错误',
                type: 'error',
                duration: 5 * 1000
            });
            return Promise.reject(new Error(res.message || 'Error'));
        } else {
            // 正常成功，直接返回内部的 data
            return res;
        }
    },
    error => {
        // 处理 HTTP 状态码层面的报错 (如 403, 500)
        let errorMsg = error.message;
        if (error.response && error.response.status === 403) {
            errorMsg = '权限不足，拒绝访问';
        }
        Message({
            message: errorMsg,
            type: 'error',
            duration: 5 * 1000
        });
        return Promise.reject(error);
    }
);

export default service;