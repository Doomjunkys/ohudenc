// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import App from './App'
import router from './router/index'
import "babel-polyfill";
import './assets/scss/glob.scss';
import glob from './assets/js/glob.js';

//use
Vue.config.productionTip = false;
//init
glob.inti();
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: {App},
  template: '<App/>'
})
