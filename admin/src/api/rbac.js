const api_rbac = {
  logout: () => axios.delete('/private/rbac/user/logout'),
  save: (data = {}) => axios.put('/private/rbac/user/save', data),
  infoByToken: (token = '') => axios.get('/private/rbac/user/info/by/token/' + token),
  menu: () => axios.get('/private/rbac/menu')
};
export default api_rbac;
