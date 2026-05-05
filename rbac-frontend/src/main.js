// src/main.js
import Vue from 'vue';
import App from './App.vue';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import router from './router';

import hasPermi from './directive/hasPermi';

Vue.config.productionTip = false;
Vue.use(ElementUI);

// 注册按钮级权限指令 v-hasPermi
Vue.directive('hasPermi', hasPermi);

new Vue({
  router, // <-- 挂载路由
  render: h => h(App),
}).$mount('#app');