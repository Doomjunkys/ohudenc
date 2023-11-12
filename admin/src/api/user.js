import axios from 'axios'
import glob from '../assets/js/glob.js';

const api_user = {
  //登出
  logout: () => axios.delete(glob.URL_ROOT_WEB + '/private/rbac/user/logout')
};
export default api_user;
