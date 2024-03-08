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
      path: '/article',
      name: 'article-main',
      component: () => import('../components/artice/main'),
    },
    {
      path: '/article/add',
      name: 'article-add',
      component: () => import('../components/artice/add'),
    }
  ]
});
