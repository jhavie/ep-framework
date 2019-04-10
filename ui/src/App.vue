<template>
  <div id="app" :class="`theme-${themeName}`">
    <transition name="fade-opacity" mode="out-in">
      <router-view></router-view>
    </transition>
    <!-- iframe 嵌入策略 -->
    <layout-iframe ></layout-iframe>
  </div>
</template>


<script>
  import throttle from 'throttle-debounce/throttle'
  import layoutIframe from './framework/layout-iframe'
  import { on, off } from 'utils'
  import settings from './settings'

	export default {

    name: 'app',
    
    components: { layoutIframe },

    created () {
      document.title = settings.title + this.$route.name
    },

    mounted () {
      this.changeSize = throttle(300, (evt) => {
        const width = this.$store.getters.getChangeWidth
        if (evt.currentTarget.innerWidth <= 1200 && width === 0) {
          this.$store.dispatch('setChangeWidth', 1)
        }
        if (evt.currentTarget.innerWidth > 1200 && width === 1) {
          this.$store.dispatch('setChangeWidth', 0)
        }
      })
      on (window, 'resize', this.changeSize)
    },
    computed: {
      themeName () {
        return this.$store.getters.getTheme
      }
    },
		watch: {
      '$route' (to, from) {
        //监听路由改变
        document.title = settings.title + to.name
      }
    },

    destroyed () {
      off (window, 'resize', this.changeSize)
    }
	}
</script>

<style lang="less">
  @import "./assets/css/main.css";
  @import "./assets/css/panel.less";
  // @import "./assets/css/theme-black-class.less";

  .pull-left {
  float: left
  }
  .pull-right {
    float:right
  }
</style>