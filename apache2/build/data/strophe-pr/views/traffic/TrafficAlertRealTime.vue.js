var TrafficAlertRealTime = {
  template: ` 
  <div id="viewport-sub">
  <div class="container-alert-card">
     <div class="alert-card-list-header">
         <p> Alertes </p>
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
     <div class="alert-card-list-header">
       <p>
         {{ alert_detail != null ? alert_detail["gateway-id"] : "" }}
       </p>
     </div>
     <div class="card-items">
          <alert-card-detail
             :alert="alert_detail"
           >
           </alert-card-detail>
     </div>
   </div>
 </div>
  `,
  mounted() {
    // var toastElList = [].slice.call(document.querySelectorAll('.toast'))
    // var toastList = toastElList.map(function (toastEl) {
    //   return new bootstrap.Toast(toastEl)
    // })
    this.subscribeToAlerts()
  },
  data() {
    return {
      alerts: [],
      alert_detail: null,
      real_time: true
    }
  },
  watch: {
    alerts: function (val) {
      this.alerts = val
    },
    alert: function (val) {
      this.alert = val
      console.log(val)
    },
    $route(to, from) {
      this.alert_detail = this.getOneAlert(to.query.id)[0]
    }
  },
  computed: {
    /*active_sub_nav() {
      return store.state.active_sub_nav
    },
    getAlerts: function () {
      return store.state.alerts
    },*/
  },
  methods: {
   /* on_success: function (message) {
      console.log(message)
      let xmlDoc = new DOMParser().parseFromString(message.innerHTML, 'application/xml');
      let numberOfItem = xmlDoc.getElementsByTagName('item').length
      for (let i = 0; i < numberOfItem; i++) {
        let tag = xmlDoc.getElementsByTagName('item')[i].getElementsByTagName('alert')[0].getAttribute('type')
        let gateway = xmlDoc.getElementsByTagName('item')[i].getElementsByTagName('alert')[0].getAttribute('gateway-id')
        let content = xmlDoc.getElementsByTagName('item')[i].getElementsByTagName('alert')[0].getElementsByTagName('content')[0].innerHTML
        let created_at = xmlDoc.getElementsByTagName('item')[i].getElementsByTagName('alert')[0].getElementsByTagName('created_at')[0].innerHTML

        //let formatedDateCreatedAt = moment(parseInt(created_at)).format('DD/MM/YYYY HH:mm:ss')

        this.alerts.unshift({ tag: tag, gateway: gateway, content: content, created_at: created_at })
      }
      return true
    },*/
    subscribeToAlerts: async function () {
      await store.state.connection.pubsub.subscribe(
        "central01@example.com",
        'pubsub.example.com',
        'denm_events',
        [],
        this.on_event,
        this.on_subscribe
      )
    },
    on_subscribe: function (sub) {
      this.subscribed = true;
      console.log("Now awaiting messages...");
      return true;
    },
    on_event: function (message) {
      if (!this.subscribed) {
        return true;
      }

      console.log(message.innerHTML)
      // this.messages.push({ id: message.id, message: message.textContent })
      this.alerts.unshift(this.formatEventXmlToJson(message.innerHTML))

      /*if ($(message).attr('from').match(re) && $(message).attr('type') == 'headline') {
        var _data = $(message).children('event')
          .children('items')
          .children('item')
          .children('entry').text();

        if (_data) {
          console.log(_data)
        }
      }*/
      return true
    },
    formatEventXmlToJson: function (xmppEvent) {
      let xmlDoc = new DOMParser().parseFromString(xmppEvent, 'application/xml');
      let id = xmlDoc.getElementsByTagName('item')[0].getAttribute('id')
      let tag = xmlDoc.getElementsByTagName('item')[0].getElementsByTagName('alert')[0].getAttribute('tag')
      let gateway = xmlDoc.getElementsByTagName('item')[0].getElementsByTagName('alert')[0].getElementsByTagName('gateway-id')[0].innerHTML
      let content = xmlDoc.getElementsByTagName('item')[0].getElementsByTagName('alert')[0].getElementsByTagName('content')[0].innerHTML
      let created_at = xmlDoc.getElementsByTagName('item')[0].getElementsByTagName('alert')[0].getElementsByTagName('created_at')[0].innerHTML

      /*let type = (eventType !== 'accident') ? 'alert alert-warning' : 'alert alert-danger'
      let currentTimeMillis = new Date().getTime()*/

      return { _id: id, 'gateway-id': gateway, tag: tag, content: content, created_at: created_at }
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
