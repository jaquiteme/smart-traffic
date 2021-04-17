Vue.component('dashboard-digit-card', {
    props: ['stat'],
    template: `
    <div :class="stat.class">
      <div class="card-body">
        <h5 class="card-title"># {{ stat.title }}</h5>
        <span class="digit">{{ stat.digit }}</span>
      </div>
    </div>
    `,
    data() {
        return {
        }
    },
});