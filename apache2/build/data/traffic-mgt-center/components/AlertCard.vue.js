Vue.component('alert-card', {
  props: ['link', 'gateway', 'tag', 'time', 'real_time'],
  template: `
  <div class="alert-card">
  <router-link class="alert-card-link" :to="link" replace exact style="text-decoration: none">
    <div :class="classes">
      <div class="card-body">
        <div class="card-title">
          <span
            style="font-size: 1.5em"
            class="mdi mdi-router-wireless"
          ></span>
          <span style="font-size: 1em"> {{ gateway }} </span>
        </div>
        <p class="card-text">
          <span style="margin-right: 40%" :class="'badge ' + alertBadge">
            {{ getTagName }}
          </span>
          <small>{{ getTimeDiffString }}</small>
        </p>
      </div>
    </div>
  </router-link>
</div>
  `,
  data() {
    return {
      createdTime: this.time,
      timePassed: "",
      interval: null,
      alertType: "",
      alertBadge: null,
      classes: "card card-alerts"
    }
  },
  beforeDestroyed() {
    if (this.real_time)
      clearInterval(this.interval)
  },
  watch: {
    alertType: function (newVal, oldVal) {
      this.alertType = newVal
    },
    classes: function (newVal, oldVal) {
      this.classes = newVal
    },
    alertBadge: function(val){
      this.alertBadge = val
    },
    tag: function (newVal, oldVal) {
      if (val == int(4)) {
        this.alertType = 'accident'
        this.classes += " card-border-full-danger"
      } else {
        this.alertType = 'embouteillage'
        this.classes += " card-border-full-warning"
      }
    }
  },
  created() {
    if (this.real_time)
      this.interval = setInterval(() => this.createdAlertDateDiff(), 5000)
  },
  computed: {
    getTimeDiffString: function () {
      return moment(parseInt(this.createdTime)).fromNow(true)
    },
    getTagName: function () {
      if (this.tag == 4) {
        this.alertType = 'accident'
        this.classes += " card-border-full-danger"
        this.alertBadge = 'bg-light-danger'
      } else {
        this.alertType = 'embouteillage'
        this.classes += " card-border-full-warning"
        this.alertBadge = 'bg-light-warning'
      }
      return this.alertType
    }
  },
  methods: {
    createdAlertDateDiff: function () {
      let diff = moment(parseInt(this.createdTime)).fromNow(true)
      this.timePassed = diff
    }
  }
});