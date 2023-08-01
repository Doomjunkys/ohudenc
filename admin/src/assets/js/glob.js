import NProgress from 'nprogress';
import 'nprogress/nprogress.css';
import router from "../../router";

export default {
  //初始化
  inti() {
    //初始化NProgress
    this.initNProgress();
  },
  //初始化NProgress
  initNProgress() {
    //配置
    NProgress.configure({easing: 'ease', speed: 500, showSpinner: false});
    NProgress.inc(0.2);

    //开始遍历
    router.beforeEach((to, from, next) => {
      NProgress.start();
      next();
    });

    //结束遍历
    router.afterEach(() => {
      NProgress.done();
    });
  }
}
