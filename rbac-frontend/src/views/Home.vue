<!-- src/views/Home.vue -->
<template>
  <div style="padding: 50px;">
    <h1>欢迎来到 RBAC 内部系统</h1>
    <el-button type="danger" @click="handleLogout">退出登录</el-button>
  </div>
</template>

<script>
export default {
  name: 'Home',
  methods: {
    handleLogout() {
      // 退出登录其实就是清空本地缓存的凭证，然后回到登录页
      localStorage.removeItem('jwt_token');
      localStorage.removeItem('permissions');
      this.$message.success('已退出登录');
      this.$router.push('/login');
    }
  }
};
</script>