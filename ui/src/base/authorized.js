/**
 * Init Router and mutiPage
 * Authorized Router Strategy
 * @author: Merjiezo
 * @since: 2018-06-24
 */
import Vue from 'vue'
import { warn } from './base'
import { delSprit } from './share/utils'
import router from '../router'
import NProgress from 'nprogress' // 页面顶部进度条
import ROUTER_JSON from '../routerJson'
import { getToken, getPermission } from 'utils'
import settings from '../settings'
import store from '../store'

const WHITE_LIST = settings.whiteList             // white list before login
const LOGIN_WHITE_LIST = settings.loginWhiteList  // white list after login

function tokenPassDecorator (to, from, next, callback) {
  NProgress.start()
  if (getToken() !== '') {
    if (to.path === '/login') {
      next('/')
    } else {
      callback(to, from, next)
    }
  } else {
    WHITE_LIST.indexOf(to.path) !== -1
      ? next()
      : next('/login')
  }
}

/**
 * Router beforeEach, whth three strategy
 * @param {VueRoute} to
 * @param {VueRoute} from
 * @param {Function} next
 */
const Strategy = {

  // Render all page, whether you have token or not
  allPass (to, from, next) {
    NProgress.start()
    next()
  },

  /**
   * Token authorized,
   * when you have token,
   * Render all page of the application
   * @param {*} to
   * @param {*} from
   * @param {*} next
   */
  tokenPass (to, from, next) {
    tokenPassDecorator(to, from, next, (to, from, next) => {
      next()
    })
  },

  // Auth Pass, Work whth menu Authorized
  authPass (to, from, next) {
    tokenPassDecorator(to, from, next, (to, from, next) => {
      if (WHITE_LIST.indexOf(to.path) !== -1 ||
        LOGIN_WHITE_LIST.indexOf(to.path) !== -1) {
        next()
      } else {
        getPermission(to.path, canGo => {
          // 404 Not Found
          if (!ROUTER_JSON.biz[to.path]
            && !ROUTER_JSON.sys[to.path]) {
            next()
          } else {
            if (canGo) {
              next()
            } else {
              store.dispatch('setNoPermissionPage', to.name)
              next('/401')
            }
          }
        })
      }
    })
  }

}

const ROUTE_STRATEGY = settings.routeStrategy || 'authPass'
if (process.env.NODE_ENV !== 'production') {
  if ('allPass|tokenPass|authPass'.indexOf(ROUTE_STRATEGY) === -1) {
    warn(ROUTE_STRATEGY + ' routeStrategy not in the list, place check!', '/base/authorized.js')
  }
}
const beforeEachFunc = Strategy[ROUTE_STRATEGY]

router.beforeEach(beforeEachFunc)

router.afterEach(_ => { NProgress.done() })

router.onError(e => {
  console.error(e)
  Vue.prototype.$pop({
    type: 'warning',
    message: '资源获取异常，请刷新再试'
  })
})
