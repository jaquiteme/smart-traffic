Vue.component('alert-card-detail', {
  props: ['alert'],
  template: `
    <div class="card">
    <div class="card-body">
      <div v-if="alert != null">
        <h5  v-for="position in positions" key="position.key" class="card-title">{{ alert.gateway }} {{ position }}</h5>
        <div>
          <h5>Passerelle:</h5>
        </div>
        <mapbox-map
        style="height: 40em"
        :access-token="accessToken"
        map-style="mapbox://styles/mapbox/streets-v11"
        :center="[4.107277, 49.282851]"
        :zoom="16" 
        >
        <mapbox-marker v-for="position in positions" key="position.key" :lng-lat="position"></mapbox-marker>
      </mapbox-map>
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
    }
  },
  mounted() {
  },
  watch: {
    alert: function (val) {
      this.alert = val
      this.positions = this.getPosition(val.content)
      //console.log(this.positions)
    },
    positions: function (val) {
      this.positions = val
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
    }
  }
});