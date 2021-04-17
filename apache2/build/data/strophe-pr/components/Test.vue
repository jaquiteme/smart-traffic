<template>
  <div id="viewport-sub">
    <div class="container-alert-card">
      <div class="alert-card-list-header">
        <p>Alertes</p>
      </div>
      <div class="card-items">
        <alert-card
          v-for="alert in alerts"
          :key="alert._id"
          :link="generateAlertUrl(alert._id)"
          :gateway="alert['gateway-id']"
          :tag="alert.tag"
          :time="alert.created_at"
          :real_time="real_time"
        >
        </alert-card>
      </div>
    </div>
    <div class="container-alert-detail">
      <div class="card-items">
        <alert-card-detail :alert="alert_detail"> </alert-card-detail>
      </div>
    </div>
  </div>
</template>