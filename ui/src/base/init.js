/**
 * Init Application
 * @author: Merjiezo
 * @since: 2018-03-27
 */
import Vue from 'vue'
import App from '../App'
import epui from 'ep-ui'  // EP UI 组件库
import router from '../router'
import store from '../store'
import { post, get } from 'utils'
import settings from '../settings'
import cookie from 'utils/cookie'
import localStorage from 'utils/localStorage'
import Bus from './share/simpleBus'
import { delSprit } from './share/utils'

import './authorized'  // Init VueRouter

let epSettings = {}
if (global.innerWidth <= 1280) {
  epSettings.size = 'small'
}
Vue.use(epui, epSettings)

// 嵌入iframe策略，详见Vuex逻辑
function initFrame () { store.dispatch('setNowShowPath') }

Vue.prototype.$env = process.env.NODE_ENV

/**
 * 兼容iframe方法
 * 策略：URL的RT权限 > 本地缓存
 * 本地缓存Token不做覆盖
 */
const initToken = (function () {
  'use strict'

  const TOKEN_STRICT = settings.tokenStrict || false
  const TOKEN_NAME = settings.tokenName || 'eptoken'
  // make it private
  function getRt (url, index) {
    let fateRt     = url.slice(index),
        equalIndex = fateRt.indexOf('=')
    fateRt = fateRt.slice(equalIndex + 1)
    let $index = fateRt.indexOf('&')
    return $index === -1
      ? fateRt
      : fateRt.slice(0, $index)
  }
  function getLocalRt () {
    return TOKEN_STRICT
      ? cookie.getCookie(TOKEN_NAME)
      : localStorage.getLocalStorage(TOKEN_NAME)
  }

  return _ => {
    let token = ''
    let url = window.location.href, index = url.indexOf('refresh_token=')
    token = index !== -1
      ? getRt(url, index)
      : getLocalRt()
    store.dispatch('setToken', token)
  }
})()

function initVue () {
  Vue.prototype.$err = function (info) {
    Vue.prototype.$pop({ type: 'danger', message: info })
  }
  Vue.prototype.$warn = function (info) {
    Vue.prototype.$pop({ type: 'warning', message: info })
  }
  Vue.prototype.$post = post
  Vue.prototype.$get = get

  /**
   * App Event Bus
   * Do not delete!
   * Evt list: [ active-tab|reset-tab|locate-tab|muti-tab-change|muti-tab-delete ]
   *  
   * 1: menuTab evt trigger
   * Can trigger in every Children of Home.vue
   * Event (active-tab | reset-tab | locate-tab)
   * @param [active-tab] change tab to active (Api)
   * @param [reset-tab] reset to tab of nowPath (Api)
   * @param [locate-tab] add tab methods (Api)
   * 
   * 2: MutiPage Mixins
   * @param [muti-tab-change] change muti tab (System method, do not call)
   * @param [muti-tab-delete] delete muti tab (System method, do not call)
   */
  Vue.prototype.$app = new Bus()
  // MutiPage List Cache
  Vue.prototype.$mutiPageObj = mutiPageCollect(router.options.routes)
}

function mutiPageCollect (routeArr) {
  let mutiPageCache = {}
  if (Array.isArray(routeArr)
    && routeArr.length > 0) {
    routeArr.forEach(item => {
      if (item.meta && item.meta.muti) {
        let key = delSprit(item.path)
        mutiPageCache[key] = item.meta
      }
      let obj = mutiPageCollect(item.children)
      for (let objKey in obj) {
        mutiPageCache[objKey] = obj[objKey]
      }
    })
  }
  return mutiPageCache
}

/**
 * Init Application
 * Your can add or delete if you want
 */
export const initApp = () => {
  initVue()
  initFrame()
  initToken()

  new Vue({
    router,
    store,
    render: h => h(App)
  }).$mount('#app')
}
