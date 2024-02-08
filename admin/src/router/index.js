export default new VueRouter({
  mode: 'history',
  base: `${process.env.VUE_APP_ADMIN_BASE_URL}`,
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('../components/home'),
    },
    {
      path: '/a/b/c',
      name: 'homeabc',
      component: () => import('../components/home'),
    }
  ]
});
