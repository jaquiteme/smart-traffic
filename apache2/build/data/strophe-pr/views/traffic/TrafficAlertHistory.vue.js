var TrafficAlertHistory = {
  template: ` 
  <div id="viewport-sub">
    <h5 style="margin-top: 3vw;padding: 1vw 2vw;"> Historique des alertes </h5>
    <div style="display: flex; flex-direction: row; height: 80%">
      <div class="container-alert-card">
        <div class="alert-card-list-header">
          <p>Alertes</p>
        </div>
        <div class="card-items">
          <alert-card
            v-for="alert in alerts"
            :key="alert._id"
            :link="generateAlertUrl(alert._id)"
            :gateway="alert['gateway-id']"
            :tag="alert.tag"
            :time="alert.created_at"
            :real_time="real_time"
          >
          </alert-card>
        </div>
      </div>
      <div class="container-alert-detail">
        <div class="card-items">
          <alert-card-detail :alert="alert_detail"> </alert-card-detail>
        </div>
      </div>
    </div>
</div>
  `,
  created() {
    this.getAlerts()
  },
  data() {
    return {
      alerts: null,
      alert_detail: null,
      real_time: false
    }
  },
  watch: {
    alerts: function (val) {
      this.alerts = val
    },
    alert: function (val) {
      this.alert = val
    },
    $route(to, from) {
      this.alert_detail = this.getOneAlert(to.query.id)[0]
    }
  },
  computed: {
    active_sub_nav() {
      return store.state.active_sub_nav
    },
  },
  methods: {
    formatDate: function () {
      return moment(parseInt(created_at)).format('DD/MM/YYYY HH:mm:ss');
    },
    getAlerts: function () {
      axios
        .get("http://127.0.0.2:8084/api/alert_events")
        .then((response) => {
          this.alerts = response.data.data
        })
        .catch((error) => {
          console.log(error)
        });
    },
    getOneAlert: function (id) {
      return this.alerts.filter((item) => item._id === id)
    },
    getTimeDiffString: function (date) {
      return moment(parseInt(date)).fromNow(true)
    },
    generateAlertUrl: function (id) {
      return { path: this.$router.currentRoute.path, query: { id: id } }
    }
  },
}
