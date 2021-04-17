
const Foo = { template: '<div>foo</div>' }
const Bar = { template: '<div>bar</div>' }

Vue.component("Home", Home);
Vue.component("Traffic", Traffic);
Vue.component("TrafficAlertRealTime", TrafficAlertRealTime);
Vue.component("TrafficAlertHistory", TrafficAlertHistory);
Vue.component("Dashboard", Dashboard);

Vue.use(VueMapboxGl);

const routes = [
  { path: '/', component: Home },
  { path: '/dashboard', component: Dashboard },
  {
    path: '/traffic', component: Traffic,
    children: [
      { path: '/traffic/alerts/real-time', component: TrafficAlertRealTime },
      { path: '/traffic/alerts/history',  component: TrafficAlertHistory },
      { path: '/traffic/alerts/history/:id', component: TrafficAlertHistory }
    ]
  },
  { path: '/foo', component: Foo },
  { path: '/bar', component: Bar }
]

const router = new VueRouter({
  mode: 'history',
  routes: routes,
  base: '/strophe-pr'
})

router.beforeEach((to, from, next) => {
  console.log("TO" + to.path)
  console.log("FROM" + from.path)
  next();
})

const app = new Vue({
  router,
  store: store,
  data() {
    return {
    }
  },
  computed: Vuex.mapState([
    'connection',
    'navigation',
    'sub_navigation',
    'active_sub_nav'
  ]),
  methods: {
    disconnect(event) {
      if (event) {
        event.preventDefault();
      }
      this.$store.dispatch("DECO_XMPP_SRV_CONNECTION");
    },
    navigate(e) {
      e.stopPropagation()
    }
  }
}).$mount('#app')


