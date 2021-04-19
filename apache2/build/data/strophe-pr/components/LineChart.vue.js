Vue.component("line-chart", {
  extends: VueChartJs.Line,
  props: ['labels', 'chartData'],
  mounted() {
    this.renderChart({ labels: this.labels, datasets: this.chartData },
      this.options)
  },
  watch: {
    chartData: {
      handler: function () {
        this.renderChart(
          { labels: this.labels, datasets: this.chartData },
          this.options
        );
      },
    },
  },
  data: () => ({
    options: {
      legend: {
        display: true
      },
      responsive: true,
      maintainAspectRatio: false
    },
  }),
});