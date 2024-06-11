import article from "./article";
import home from "./home";

const routes = (() => {
  let routes = [];
  [
    home,
    article
  ].forEach(item => {
    if (Array.isArray(item)) {
      routes = routes.concat(item);
    }
  });
  return routes;
})();

export default new VueRouter({
  base: `${process.env.VUE_APP_ADMIN_BASE_URL}`,
  routes: routes
});
