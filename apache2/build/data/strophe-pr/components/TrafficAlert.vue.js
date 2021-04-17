Vue.component('traffic-alert', {
  props: ['title', 'type', 'time', 'message'],
  template: `
    <div :class="type" role="alert">
    <h5 class="alert-heading">{{ title }} - </h5> <small> {{ getTimeDiffString }} </small>
    <p>
      {{ message }}
    </p>
  </div>
  `,
  data() {
    return {
      createdTime: this.time,
      timePassed: "",
      interval: null
    }
  },
  beforeDestroyed() {
    clearInterval(this.interval)
  },
  created() {
    this.interval = setInterval(() => this.createdAlertDateDiff(), 50000)
  },
  computed: {
    getTimeDiffString: function () {
      return moment(parseInt(this.createdTime)).fromNow(true)
    }
  },
  methods: {
    createdAlertDateDiff: function () {
      let diff = moment(parseInt(this.createdTime)).fromNow(true)
      this.timePassed = diff
    }
  }
});