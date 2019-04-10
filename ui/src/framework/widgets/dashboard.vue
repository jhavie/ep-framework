<template>
  <div class="contents-card dashboard-home">
    <div v-if="!show" class="dashboard-icon">
      <div class="no-data-div-circle"></div>
    </div>
    <div v-if="show" class="dashboard-icon" :style="{ color: color }">
      <i :class="'ion-' + dashboard.icon"></i>
    </div>
    <div class="dashboard-words" v-if="!show">
      <div class="no-data-div div-right"></div>
      <div class="no-data-div-mini div-right"></div>
    </div>
    <transition name="fade-in" mode="out-in">
      <div class="dashboard-words" v-if="show">
        <h3>{{dashboard.value}}</h3>
        <p>{{dashboard.describe}}</p>
      </div>
    </transition>
  </div>
</template>

<script>
  export default {
    name: 'dashboard',

    props: {
      show: Boolean,
      color: {
        type: String,
        default: '#3498db'
      },
      dashboard: Object
    }
  }
</script>