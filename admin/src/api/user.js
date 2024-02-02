const api_user = {
  logout: () => axios.delete('/private/rbac/user/logout'),
  infoByToken: (token = '') => axios.get('/private/rbac/user/info/by/token/' + token)
};
export default api_user;
