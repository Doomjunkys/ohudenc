import router from "../../router";
import {Message} from 'element-ui';

const glob = {
  //初始化
  inti() {
    //初始化NProgress
    this.initNProgress();
    //初始化Axios
    this.intiAxios();
  },
  //返回token
  getToken() {
    //token名称
    const TOKEN_NAME = 'token';
    //返回TOKEN
    return $cookies.get(TOKEN_NAME);
  },
  //检查是否登陆
  checkLogin() {
    //检查是否有token
    if (!this.getToken()) {
      //跳转到登陆页
      this.jumpLoginPage();
    }
  },
  //跳转到登陆页
  jumpLoginPage() {
    const redirectUrl = escape(window.location.href);
    window.location = process.env.VUE_APP_BASE_URL + 'login.html?redirectUrl=' + redirectUrl;
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
  },
  //初始化Axios
  intiAxios() {
    // 添加请求拦截器(从session存储中拿token)
    axios.interceptors.request.use(config => {
        //设置请求根路径
        config.baseURL = process.env.VUE_APP_BASE_URL + 'api/udf/web';
        //添加token到header中
        config.headers.token = this.getToken() ? this.getToken() : '';
        //设置请求超时时间
        config.timeout = 5000;
        //返回
        return config;
      },
      error => Promise.reject(error)
    );
    // 添加响应拦截器(状态码不为200默认失败)
    axios.interceptors.response.use(response => {
        if (response.status == 200) {
          return Promise.resolve(response);
        }
        return this.errorHandle(response);
      }, error => {
        return this.errorHandle(error.response);
      }
    );
  },
  //展示错误信息
  errorHandle(response) {
    if (response.status == 403) {
      this.jumpLoginPage();
    } else {
      this.message(response);
    }
    return Promise.reject(response);
  },
  // 错误面板
  message(response) {
    //弹出消息框
    Message({
      dangerouslyUseHTMLString: false, // 设置HTML显示
      showClose: true, // 显示关闭按钮
      duration: 3000, // 默认3秒后关闭
      type: response.status === 400 ? 'warning' : 'error',
      message: this.getErrorMessage(response)
    });
  },
  // 3.获取错误详情
  getErrorMessage(response) {
    //默认提示语
    let msg = '服务器开小差了 , 请稍后再试';
    //判断是否有响应消息体
    if (response.data && response.data.errorDetail) {
      //判断错误类型
      if (response.data.errorDetail.type === 'org.springframework.web.bind.MethodArgumentNotValidException') {
        //参数校验错误
        let obj = [];
        try {
          const msgData = response.data.errorDetail.message.replace(/＂/g, '"');
          obj = JSON.parse(msgData);
        } catch (e) {
          obj = [];
        }
        let message = '';
        if (obj instanceof Array) {
          obj.forEach(item => {
            message = message ? `${message}，${item.defaultMessage}` : item.defaultMessage;
          })
        }
        msg = message;
      } else {
        //其他错误
        msg = response.data.errorDetail.message;
      }
    }
    return msg;
  },
}
Vue.prototype.$glob = glob;
export default glob;
