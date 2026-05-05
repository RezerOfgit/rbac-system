<!-- src/App.vue -->
<template>
  <div id="app">
    <!-- 所有的页面都会根据路由，渲染在这里 -->
    <router-view></router-view>
  </div>
</template>

<style>
/* 全局去除默认边距 */
body { margin: 0; padding: 0; }
</style>