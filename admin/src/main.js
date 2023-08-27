// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui';
import axios from 'axios';
import VueAxios from 'vue-axios';
import './assets/css/reset.css';
import './assets/css/bootstrap.css';
import './assets/scss/glob.scss';
import 'element-ui/lib/theme-chalk/index.css';
import './assets/scss/element-variables.scss';
import Glob from './assets/js/glob.js';

//use
Vue.config.productionTip = false;
Vue.use(ElementUI);
Vue.use(VueAxios, axios);
//init
Glob.inti();
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: {App},
  template: '<App/>'
})
