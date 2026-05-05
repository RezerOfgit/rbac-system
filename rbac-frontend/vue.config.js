// vue.config.js
const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  transpileDependencies: true,
  // 配置开发服务器
  devServer: {
    port: 8081, // 我们明确指定前端运行在 8081 端口
    proxy: {
      // 拦截所有以 /api 开头的请求
      '/api': {
        target: 'http://localhost:8080', // 转发给你的 Spring Boot 后端地址
        changeOrigin: true,              // 允许跨域（必须开启）
        // pathRewrite: { '^/api': '' }  // 注意：因为我们后端的接口本身就带有 /api (如 /api/auth/login)，所以这里不需要重写路径
      }
    }
  }
})