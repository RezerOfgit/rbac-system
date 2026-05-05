// src/utils/request.js
import axios from 'axios';
import { Message } from 'element-ui';

const service = axios.create({
    baseURL: '', // 使用相对路径，触发 devServer 的代理拦截
    timeout: 5000
});

// 请求拦截器：自动在请求头中携带 JWT Token
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

// 响应拦截器：统一处理业务状态码和 HTTP 异常
service.interceptors.response.use(
    response => {
        // 拿到后端返回的封装数据 R
        const res = response.data;

        // code 非 200 视为业务异常
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