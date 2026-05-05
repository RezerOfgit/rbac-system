<!-- src/views/Login.vue -->
<template>
  <div class="login-container">
    <el-card class="login-card">
      <div slot="header" class="clearfix">
        <span>RBAC 权限管理系统</span>
      </div>

      <el-form :model="loginForm" :rules="rules" ref="loginForm" label-width="60px">
        <el-form-item label="账号" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入账号"></el-input>
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <!-- type="password" 隐藏密码，@keyup.enter.native 支持回车登录 -->
          <el-input type="password" v-model="loginForm.password" placeholder="请输入密码" @keyup.enter.native="handleLogin"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleLogin" style="width: 100%;">登 录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
// 引入已封装的 axios 实例
import request from '@/utils/request';

export default {
  name: 'Login',
  data() {
    return {
      loginForm: {
        username: 'admin', // 默认填入我们在数据库的超级管理员账号，方便测试
        password: '123456'
      },
      rules: {
        username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      }
    };
  },
  methods: {
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          // 调用后端登录接口
          request.post('/api/auth/login', this.loginForm).then(res => {
            if (res.code === 200) {
              this.$message.success('登录成功');

              // 持久化 Token 和权限列表，供请求拦截器和 v-hasPermi 指令使用
              localStorage.setItem('jwt_token', res.data.token);
              localStorage.setItem('permissions', JSON.stringify(res.data.permissions));

              // 跳转到系统首页
              this.$router.push('/');
            }
          });
        } else {
          return false;
        }
      });
    }
  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f2f5;
}
.login-card {
  width: 400px;
}
</style>