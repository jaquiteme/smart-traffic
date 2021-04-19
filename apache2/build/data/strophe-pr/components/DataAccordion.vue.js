Vue.component('data-accordion', {
    props: ['rawData'],
    template: `
    <div v-for="data in rawData" :key="data._id" class="accordion-item">
      <h4 class="accordion-header" :id="'flush-heading'+data._id">
        <button
          class="accordion-button collapsed"
          type="button"
          data-bs-toggle="collapse"
          :data-bs-target="'#flush-collapse'+data._id"
          aria-expanded="false"
          :aria-controls="'flush-collapse'+data._id"
        >
          {{ data.content['station-id'] }} <small style="padding-left: 150vh"> {{ getDateHour(data.created_at) }} </small>
        </button>
      </h4>
      <div
        :id="'flush-collapse'+data._id"
        class="accordion-collapse collapse"
        :aria-labelledby="'flush-heading'+data._id"
        data-bs-parent="#accordionFlushExample"
      >
        <div class="accordion-body">
             <pre>{{ getPrettyJson(data) }}</pre> 
        </div>
      </div>
    </div>
  `,
    data() {
        return {

        }
    },
    watch: {
    },
    created() {

    },
    computed: {
    },
    methods: {
        getDateHour(date) {
            return moment(date).format("DD-MM-YYYY HH:mm:ss")
        },
        getPrettyJson(data){
            return JSON.stringify(data, null, 4)
        }
    }
});