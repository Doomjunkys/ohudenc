export default [
  {
    path: '/article',
    name: '/article',
    component: () => import('../components/artice/main'),
  },
  {
    path: '/article/add',
    name: '/article/add',
    component: () => import('../components/artice/add'),
  }
];
