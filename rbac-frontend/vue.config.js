// vue.config.js
const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  transpileDependencies: true,
  // 配置开发服务器
  devServer: {
    port: 8081,
    proxy: {
      // 拦截所有以 /api 开头的请求
      '/api': {
        target: 'http://localhost:8080', // 转发给 Spring Boot 后端地址
        changeOrigin: true,              // 允许跨域（必须开启）
      }
    }
  }
})