// src/main.js
import Vue from 'vue';
import App from './App.vue';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import router from './router'; // <-- 引入刚才写的路由配置

// 1. 引入刚刚写好的自定义指令
import hasPermi from './directive/hasPermi';

Vue.config.productionTip = false;
Vue.use(ElementUI);

// 2. 全局注册 v-hasPermi 指令
Vue.directive('hasPermi', hasPermi);

new Vue({
  router, // <-- 挂载路由
  render: h => h(App),
}).$mount('#app');