Vue.component("line-chart", {
  extends: VueChartJs.Bar,
  mounted() {
    this.renderChart(
      {
        labels: [
          "2021-04-12",
        ],
        datasets: [
          {
            label: "Accident",
            backgroundColor: "#f87979",
            data: [2],
          },
          {
            label: "Embouteillage",
            backgroundColor: "#4cb53f",
            data: [86],
          },
        ],
      },
      { responsive: true, maintainAspectRatio: false }
    );
  },
});