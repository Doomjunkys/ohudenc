import router from "../../router";
import { Message } from 'element-ui';

const glob = {
  //token名称
  TOKEN_NAME: 'udf_token',
  //API根路径
  URL_ROOT_WEB: '/api/udf/web',
  //错误消息持续时间
  ERROR_MESSAGE_DURATION: 3,
  //默认提示语
  DEF_ERROR_INFO_MESSAGE: '服务器开小差了 , 请稍后再试',
  //初始化
  inti() {
    //初始化NProgress
    this.initNProgress();
    //初始化Axios
    this.intiAxios();
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
        //添加token到header中
        config.headers.token = sessionStorage.getItem(this.TOKEN_NAME) ? sessionStorage.getItem(this.TOKEN_NAME) : '';
        //返回
        return config;
      },
      error => Promise.reject(error)
    );
    // 添加响应拦截器(状态码不为200默认失败)
    axios.interceptors.response.use(response => {
        if (response.status == 200) {
          return Promise.resolve(response);
        } else {
          if (response.status == 403) {
            //TODO 认证错误,需要跳转到首页
          } else {
            this.message(error);
          }
          return Promise.reject(response);
        }
      }, error => {
        this.message(error);
        return Promise.reject(error.response);
      }
    );
  },
  // 错误面板
  message(error) {
    //弹出消息框
    Message({
      dangerouslyUseHTMLString: false, // 设置HTML显示
      showClose: true, // 显示关闭按钮
      duration: this.ERROR_MESSAGE_DURATION * 1000, // 默认3秒后关闭
      type: error.status === 400 ? 'warning' : 'error',
      message: this.getErrorMessage(error)
    });
  },
  // 3.获取错误详情
  getErrorMessage(error) {
    //默认提示语
    let msg = this.DEF_ERROR_INFO_MESSAGE;
    //判断是否有响应消息体
    if (error.data && rror.data.errorDetail) {
      //判断错误类型
      if (error.data.errorDetail.type === 'org.springframework.web.bind.MethodArgumentNotValidException') {
        //参数校验错误
        let obj = [];
        try {
          const msgData = error.data.errorDetail.message.replace(/＂/g, '"');
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
        msg = error.data.errorDetail.message;
      }
    }
    return msg;
  },
}
Vue.prototype.$glob = glob;
export default glob;
