<!-- src/views/Home.vue -->
<template>
  <div style="padding: 50px;">
    <h1>欢迎来到 RBAC 内部系统</h1>

    <div style="margin: 20px 0; padding: 20px; border: 1px solid #ccc;">
      <h3>按钮级权限测试区</h3>
      <!-- 这个按钮只有拥有 sys:user:add 权限（或超级管理员）才能看到 -->
      <el-button type="primary" v-hasPermi="['sys:user:add']">新增用户</el-button>

      <!-- 这个按钮只有拥有 sys:role:delete 权限才能看到 -->
      <el-button type="warning" v-hasPermi="['sys:role:delete']">删除角色</el-button>

      <!-- 这是一个故意设置的、目前任何人都没有的假权限 -->
      <el-button type="danger" v-hasPermi="['sys:not:exist']">绝密按钮</el-button>
    </div>

    <el-button type="info" @click="handleLogout">退出登录</el-button>
  </div>
</template>

<script>
export default {
  name: 'Home',
  methods: {
    handleLogout() {
      localStorage.removeItem('jwt_token');
      localStorage.removeItem('permissions');
      this.$message.success('已退出登录');
      this.$router.push('/login');
    }
  }
};
</script>