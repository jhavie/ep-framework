<template>
  <div class="muti-file">
    <!-- <transition-group name="fade-opacity" mode="out-in"> -->
      <component v-for="item in idList"
        v-show="active === item"
        :key="item"
        :is="mapIpt"
        :data-id="item"></component>
    <!-- </transition-group> -->
  </div>
  
</template>

<script>
  import mapIpt from './mapIpt'
  import mutiPage from 'src/base/mixins/mutiPage'

  export default {

    name: 'map',

    mixins: [ mutiPage ],

    data () {
      return {
        mapIpt: mapIpt
      }
    }
  }
</script>
