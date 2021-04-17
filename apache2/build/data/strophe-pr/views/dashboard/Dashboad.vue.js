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
          <line-chart></line-chart>
        </div>
      </div>
    </div>
  </div>
`,
  data() {
    return {
      stats: [
        { title: 'Passerelle', digit: '01', class: 'card wdth-20' },
        { title: 'Voitures', digit: '01', class: 'card wdth-20' },
        { title: 'Messages', digit: '01', class: 'card wdth-20' }
      ],
      datacollection: null
    }
  },
  mounted() {
    this.fillData()
  },
  methods: {
    fillData () {
      this.datacollection = {
        labels: [this.getRandomInt(), this.getRandomInt()],
        datasets: [
          {
            label: 'Data One',
            backgroundColor: '#f87979',
            data: [this.getRandomInt(), this.getRandomInt()]
          }, {
            label: 'Data One',
            backgroundColor: '#f87979',
            data: [this.getRandomInt(), this.getRandomInt()]
          }
        ]
      }
    },
    getRandomInt () {
      return Math.floor(Math.random() * (50 - 5 + 1)) + 5
    }
  }
};

