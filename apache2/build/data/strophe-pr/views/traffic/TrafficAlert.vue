<template>
  <div id="viewport-sub">
    <div class="row col-md-6 justify-content-md-center">
      <div class="row col-md-12">
        <table class="table table-borderless table-hover">
          <thead>
            <tr>
              <th scope="col">Evênement</th>
              <th scope="col">Passerelle</th>
              <th scope="col">Il y a</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="alert in alerts" :key="alert._id">
              <td>
                <router-link :to="generateAlertUrl(alert._id)" replace exact>
                  <span class="badge bg-light-danger">{{ alert.tag }}</span>
                </router-link>
              </td>
              <td>{{ alert["gateway-id"] }}</td>
              <td>{{ getTimeDiffString(alert.created_at) }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    <br />
    <div class="row">
      <div class="notifications">
        <h6>Détails :</h6>
      </div>
    </div>
  </div>
</template>