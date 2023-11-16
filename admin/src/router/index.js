export default new VueRouter({
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('../components/home'),
    }
  ]
})
