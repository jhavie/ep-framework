<template>
  <div class="contents-card dashboard-nav" id="dashboard-weather" :class="'panel-' + theme">
    <h5>天气</h5>
    <div class="left-display">
      <i :class="'ion-' + icon"></i>
      <p>{{weather.type}}</p>
    </div>
    <div class="right-display">
      <h1>{{weather.wendu}}℃</h1>
      <p>{{weather.city}}，{{date}}</p>
    </div>
  </div>
</template>

<script>
  import { jsonp } from 'utils/fetch'
  const icon = {
    "晴": "ios-sunny",
    "多云": "ios-cloudy",
    "多云转晴": "ios-partlysunny",
    "小雨": "ios-rainy",
    "中雨": "ios-rainy",
    "大雨": "ios-rainy",
    "阵雨": "ios-thunderstorm",
    "雪": "ios-snowy"
  }
  export default {
    name: 'weather',
    props: {
      theme: String
    },

    data () {
      let d = new Date();
      let str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
      return {
        icon: 'ios-cloudy',
        date: str,
        weather: {
          city: "上海",
          type: '--',
          wendu: '-',
          feng: '-'
        }
      }
    },

    created () {
      let vm = this
      jsonp(1,(json) => {
        if (json.status === 1000) {
          const w = json.data
          vm.weather = {
            city: w.city,
            type: w.forecast[0].type,
            wendu: w.wendu,
            feng: w.forecast[0].fengli
          }
          if (w.forecast[0].type in icon) {
            vm.icon = icon[w.forecast[0].type]
          }
        } else {
          alert ('天气获取失败！')
        }
      })
    }
  }
</script>