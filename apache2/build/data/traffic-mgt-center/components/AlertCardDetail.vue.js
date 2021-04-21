Vue.component('alert-card-detail', {
  props: ['alert'],
  template: `
    <div class="card">
    <div class="card-body">
      <div v-if="alert != null">
        <div>
          <small> {{ alert['gateway-id'] }} </small>
          <span style="float: right;" :class="'badge '+ alertBadge">
            {{ getTagName(alert.tag) }}
          </span>
        </div>
        <hr/>
        <div style="display: flex; flex-direction: column;">
          <div style="display: flex; flex-direction: row; margin-bottom: 2vh;">
            <div class="card">
              <div class="card-header">
                Positions
              </div>
              <ul class="list-group list-group-flush">
                <li v-for="car in alert.content" key="car['station-id']" class="list-group-item">
                    {{ car['station-id'] }} 
                  <ul>
                    <li> 
                      {{ car.position.latitude }} , {{ car.position.longitude }}
                    </li>
                  </ul>
                </li>
              </ul>
            </div>
          </div>
          <mapbox-map
          style="height: 40em"
          :access-token="accessToken"
          map-style="mapbox://styles/mapbox/streets-v11"
          :center="positions[0]"
          :zoom="16" 
          >
            <mapbox-marker v-for="position in positions" key="position.key" :lng-lat="position"></mapbox-marker>
          </mapbox-map>
        </div>
      </div>
      <div v-else>
        <p class="card-text">Please pick something.</p>
      </div>
    </div>
  </div>
    `,
  data() {
    return {
      accessToken: 'pk.eyJ1Ijoiam9rZXRoZSIsImEiOiJja211c2tnaGoxNHZrMm9wOW9uYTV2aWt0In0.-MGGhhymF373h_67LqfELw',
      positions: [],
      alertType: null,
      alertBadge: null,

    }
  },
  mounted() {
  },
  watch: {
    alert: function (val) {
      this.alert = val
      this.positions = this.getPosition(val.content)
      console.log(this.alert.content)
    },
    positions: function (val) {
      this.positions = val
    },
    alertBadge: function(val){
      this.alertBadge = val
    }
  },
  methods: {
    getPosition(content) {
      let positions = []
      content.forEach((element) => {
        let latLng = []
        //console.log(element)
        latLng.push(element.position.longitude)
        latLng.push(element.position.latitude)
        positions.push(latLng)
      })
      return positions
    },
    getTagName: function (tag) {
      if (tag == 4) {
        this.alertType = 'accident'
        this.alertBadge = 'bg-light-danger'
      } else {
        this.alertType = 'embouteillage'
        this.alertBadge = 'bg-light-warning'
      }
      return this.alertType
    }
  }
});