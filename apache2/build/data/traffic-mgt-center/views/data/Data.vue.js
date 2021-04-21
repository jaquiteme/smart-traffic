var Data = {
    template: `
    <div id="viewport">
        <h4> Raw Data </h4>
        <div class="card">
            <div class="card-body">
                <div class="accordion accordion-flush" id="accordionFlushExample">
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
                </div>
            </div>
        </div>
    </div>
  `,
    data() {
        return {
            rawData: []
        }
    },
    created() {
        this.getRawData()
    },
    watch: {

    },
    methods: {
        getRawData: function () {
            axios.get("http://127.0.0.1:8084/api/road_messages")
                .then((response) => {
                    this.rawData = response.data.data
                })
                .catch((error) => {
                    console.log(error)
                });
        },
        getDateHour: function(date) {
            return moment(date).format("DD-MM-YYYY HH:mm:ss")
        },
        getPrettyJson(data){
            return JSON.stringify(data, null, 4)
        }
    },
};

