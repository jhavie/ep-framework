<template>
  <div>
    <!-- 头部 -->
    <header class="shadow-top panel-menu" :class="'panel-' + theme"
      :style="{ paddingLeft: menuLeft }">
      <frame-menu @change-skin="toggleSkin"></frame-menu>
      <div class="panel-tab">
        <ep-tab ref="frameTab"
          :theme="theme" :tabs="menuTab.items" v-model="menuTab.active"
          @change="handleTabChange" @close="handleClose"></ep-tab>
        <div class="tab-func">
          <ep-dropdown @item-click="tabFuncClick" :label="tabLabel" position="right">
            <span class="panel-tab-drop-func" slot="item">
              标签选项
              <i class="ion-arrow-down-b"></i>
            </span>
          </ep-dropdown>
        </div>
      </div>
    </header>
    <!-- 菜单框架 -->
    <left-menu @change="handleMenuChange" :changeWidth="$store.getters.getChangeWidth"></left-menu>
    <!-- 业务框架 -->
    <div class="panel-main"
      :style="{ marginLeft: menuLeft }">
      <transition name="fade-opacity" mode="out-in">
        <keep-alive :include="realKeepAliveInclude">
          <router-view></router-view>
        </keep-alive>
      </transition>
      <!-- 版权信息 -->
      <footer class="panel-copyright">
        <p>
          Copyright (C) 2001-{{new Date().getFullYear()}} easipass.com. All Rights Reserved Version: 2.0.0.0
        </p>
      </footer>
      <return-top :theme="theme"></return-top>
    </div>
    <change-skin v-model="showRight"></change-skin>
  </div>
</template>

<script>
import frameMenu from "./components/frame-menu"
import epTab from "./components/ep-tab"
import leftMenu from "./components/left-menu"
import changeSkin from "./components/change-skin"
import returnTop from "./components/return-top"
import { cloneObj, resetAuth, getPermission } from "utils"
import tabMixin from 'src/base/mixins/tabMixin'

export default {
  name: 'appMain',

  components: {
    frameMenu, epTab, leftMenu, changeSkin, returnTop
  },

  mixins: [ tabMixin ],

  mounted () { getPermission ('/', _ => {}) },

  data () {
    return {
      showRight: false,
      tabLabel: [
        { key: '1', value: '关闭所有' },
        { key: '2', value: '关闭其他' }
      ]
    }
  },

  computed: {
    menuLeft () { return this.$store.getters.getChangeWidth === 1 ? '60px' : null },
    theme () { return this.$store.getters.getTheme }
  },

  methods: {
    toggleSkin () { this.showRight = !this.showRight },
  },

  beforeDestroy () {
    let time = setTimeout(() => {
      clearTimeout(time)
      resetAuth()
    }, 0)
  }

}
</script>

<style>
  .menu-shadow-left {
    box-shadow: 0 2px 2px 0 rgba(0, 0, 0, 0.14), 
      0 3px 7px -2px rgba(0, 0, 0, 0.4), 
      0 1px 5px 0 rgba(0, 0, 0, 0.12)
  }
  .shadow-right {
    box-shadow: 0 2px 2px 0 rgba(0, 0, 0, 0.14),
      0 3px 1px -2px rgba(0, 0, 0, 0.2),
      0 1px 5px 0 rgba(0, 0, 0, 0.12)
  }
  .shadow-top {
    box-shadow: 0 2px 2px 0 rgba(0, 0, 0, 0.14), 
      0 3px 1px -2px rgba(0, 0, 0, 0.2), 
      0 1px 5px 0 rgba(0, 0, 0, 0.12)
  }
  .shadow-bottom {
    box-shadow: 0 2px 2px 0 rgba(0, 0, 0, 0.14), 0 3px 1px -2px rgba(0, 0, 0, 0.2), 0 1px 5px 0 rgba(0, 0, 0, 0.12)
  }
  .panel-tab {
    position: relative;
  }
  .tab-func {
    cursor: pointer;
    position: absolute;
    top: 0px;
    right: 0px;
    height: 40px;
    line-height: 40px;
    width: 80px;
    background: #FFF;
    font-size: 12px;
    text-align: center;
    border-bottom: 1px solid #AAA;
  }
  .panel-tab-drop-func {
    font-size: 12px;
  }
</style>