<template>
  <div v-show="iframe" class="panel-frame"
    :style="{ left: menuLeft }">
    <!-- <iframe src="http://192.168.129.181/imgd" frameborder="0" width="100%" height="100%"></iframe> -->
  </div>
</template>

<script>
  export default {
    data () {
      return {
        iframe: false
      }
    },
    
    computed: {
      menuLeft () {
        if (this.$store.getters.getChangeWidth === 1) {
          return '60px'
        } else {
          return '220px'
        }
      }
    }
  }
</script>

<style>
  .panel-frame {
    position: absolute;
    top: 98px;
    right: 0px;
    bottom: 0px;
    overflow: hidden;
  }
</style>
