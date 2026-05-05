// src/main.js
import Vue from 'vue';
import App from './App.vue';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import router from './router'; // <-- 引入刚才写的路由配置

Vue.config.productionTip = false;
Vue.use(ElementUI);

new Vue({
  router, // <-- 挂载路由
  render: h => h(App),
}).$mount('#app');