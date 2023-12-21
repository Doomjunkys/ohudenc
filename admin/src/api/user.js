const api_user = {
  //登出
  logout: () => axios.delete('/private/rbac/user/logout')
};
export default api_user;
