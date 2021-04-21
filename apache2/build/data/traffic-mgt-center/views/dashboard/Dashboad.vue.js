var Dashboard = {
  template: `
  <div id="dashboard-content">
    <div>
        <dashboard-digit-card
          v-for="stat in stats"
          :key="stat.title"
          :stat="stat"
        ></dashboard-digit-card>
    </div>
    <div>
      <div class="card" style="width: 80%;">
        <div class="card-body">
          <h5 class="card-title">Chart</h5>
          <line-chart 
          :labels="lineChartLabels"
          :chartData="datasetsLineChart">
          </line-chart>
        </div>
      </div>
    </div>
  </div>
`,
  data() {
    return {
      stats: [],
      datacollection: null,
      lineChartLabels: null,
      datasetsLineChart: null
    }
  },
  created() {
    this.requestGetAlertEventStat()
    this.getStats()
  },
  watch: {
    lineChartLabels: function (val) {
      this.lineChartLabels = val
    },
    datasetsLineChart: function (val) {
      this.datasetsLineChart = val
    },
    stats: function (val) {
      this.stats = val
    }
  },
  methods: {
    requestGetAlertEventStat: function () {
      let now = new Date().getTime()
      console.log(now)
      axios
        .get("http://127.0.0.2:8084/api/alert_events/stats?gt_date=0&lt_date=" + now)
        .then((response) => {
          this.lineChartLabels = this.getLabels(response.data.data)
          this.datasetsLineChart = this.getLineChartAlertEventStatData(response.data.data)
        })
        .catch((error) => {
          console.log(error)
        });
    },
    getLabels: function (data) {
      var labels = [];
      data.forEach((obj) => {
        obj.stats.forEach((stat) => {
          labels.push(stat.created_at);
        });
      });
      labels.sort()
      return labels.map((x) => x).reduce(this.removeDuplicate, []);
    },
    getLineChartAlertEventStatData: function (data) {
      var values = [];
      data.forEach((obj) => {
        let lines = {};
        let event = this.setChartLabelColor(obj._id.tag)
        lines.label = event.name;
        lines.borderColor = event.color;
        let axes = [];
        //[SORT DATES]
        obj.stats.sort((a, b) => {
          let da = new Date(a.created_at),
            db = new Date(b.created_at);
          return da - db
        })

        obj.stats.forEach((stat) => {
          let axis = {};
          axis.x = stat.created_at;
          axis.y = stat.count;
          axes.push(axis);
        });
        lines.data = axes;
        values.push(lines);
      });
      return values;
    },
    setChartLabelColor: function (tag) {
      if (tag == 4)
        return { name: "Accident", color: "red" }
      return { name: "Embouteillage", color: "yellow" }
    },
    removeDuplicate: function (a, b) {
      if (a.indexOf(b) < 0) {
        a.push(b);
      }
      return a;
    },
    getStats: function () {
      //[NUMBER OF GATEWAYS]
      axios
        .get("http://127.0.0.2:8084/api/road_messages/gateways/length")
        .then((response) => {
          this.stats.push({ title: 'Passerelle', digit: response.data.data, class: 'card wdth-20' })
        })
        .catch((error) => {
          console.log(error)
        });
      //[NUMBER OF CARS]
      axios
        .get("http://127.0.0.2:8084/api/road_messages/cars/length")
        .then((response) => {
          this.stats.push({ title: 'Voitures', digit: response.data.data, class: 'card wdth-20' })
        })
        .catch((error) => {
          console.log(error)
        });
      //[NUMBER OF MESSAGES]
      axios
        .get("http://127.0.0.2:8084/api/road_messages/length")
        .then((response) => {
          this.stats.push({ title: 'Messages', digit: response.data.data, class: 'card wdth-20' })
        })
        .catch((error) => {
          console.log(error)
        });
    }
  }
};

