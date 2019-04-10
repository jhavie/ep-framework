<template>
  <transition name="fade-right-in">
    <div class="shadow-right panel-change-skin" v-show="value" @click.stop>
      <h2><i style="padding-right: 10px" class="ion-tshirt"></i>换肤</h2>
      <i class="change-skin-close ion-close" @click="this.$parent.toggleSkin"></i>
      <div class="change-skin-switch">
          <span>深色菜单</span><ep-switch v-model="darkMenu"></ep-switch>
      </div>
      <div class="change-skin-now" :class="'panel-' + theme">
        <p>{{ nowTheme }}</p>
      </div>
      <div v-for="(color, index) in colors" :key="index"
        class="change-skin-main" :class="{ active: theme === color.enName }"
        @click="handleSkin(color.enName, index)">
        <span class="card-shadow change-color-dot"
          :style="{ background: '#' + color.theme }"></span>
        <p>{{ color.name }}</p>
      </div>
      <div class="change-skin-main">
        <p>敬请期待...</p>
      </div>
    </div>
  </transition>
</template>

<script>
  import { on, off } from 'utils'
  const colors = [
    { theme: '001529', name: '深色', enName: 'dark'  },
    { theme: '2196f3', name: '主题色', enName: 'blue' },
    { theme: '3498db', name: '扁平蓝', enName: 'dark-blue' },
    { theme: '1abc9c', name: '扁平绿', enName: 'green'  },
    { theme: '8e44ad', name: '扁平紫', enName: 'pupple'  },
    { theme: 'f39c12', name: '扁平橙', enName: 'orange'  }
  ]
  export default {
    name: 'change-skin',
    props: {
      value: Boolean
    },

    data () {
      return {
        darkMenu: this.$store.getters.getDark === '1'? true: false,
        colors: colors
      }
    },

    watch: {
      darkMenu (val) {
        this.$store.dispatch('setDark', val? '1': '0')
      },
      value (val) {
        if (val) {
          //此处绑定事件冒泡赶不上下列代码执行，故通过timeout修改执行顺序
          setTimeout(() => {
            on (document, 'click', this.$parent.toggleSkin)
          }, 0)
        } else {
          off (document, 'click', this.$parent.toggleSkin)
        }
      }
    },

    computed: {
      theme () {
        return this.$store.getters.getTheme
      },
      nowTheme () {
        const theme = this.$store.getters.getTheme
        for (let i = 0; i < colors.length; i++) {
          if (colors[i].enName === theme) {
            return colors[i].name
          }
        }
      }
    },

    methods: {
      handleSkin (theme) {
        this.$store.dispatch('setTheme', theme)
      }
    }
  }
</script>

<style>
  .change-skin-switch {
    margin-bottom: 10px;
    box-sizing: border-box;
    padding: 14px 0;
    text-align: center
  }
  .change-skin-now {
    position: relative;
    margin: 0 20px 10px 20px;
    padding: 10px 0;
    line-height: 30px;
    height: 30px;
    font-size: 16px;
    color: #FFF;
    border-radius: 25px;
    text-align: center;
    -webkit-transition: background 1s;
    transition: background 1s;
  }
  .change-skin-now > p {
    cursor: default;
    margin: 0;
    vertical-align: middle;
    text-shadow: 0 1px 1px rgba(0, 0, 0, .5)
  }
  .change-skin-main {
    cursor: pointer;
    position: relative;
    padding: 10px 0;
    line-height: 30px;
    height: 30px;
    font-size: 16px;
    text-align: center;
    -webkit-transition: background .2s;
    transition: background .2s;
  }
  .change-skin-main.active, .change-skin-main:hover {
    background: #EAF0F7
  }
  .change-color-dot, .change-skin-main > p {
    display: inline-block;
    margin: 0;
    vertical-align: middle;
    text-align: center
  }
  .change-color-dot {
    margin-right: 10px;
    width: 18px;
    height: 18px;
    border-radius: 50%
  }
  .change-skin-close {
    cursor: pointer;
    position: absolute;
    top: 0px;
    right: 0px;
    padding: 10px;
  }
</style>