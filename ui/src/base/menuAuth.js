import Vue from 'vue'
import { warn } from './base'
import { post } from 'utils'
import settings from '../settings'
import routerMenu from '../routerMenu'

const Strategy = {
  
  static (fn) {
    post('getUserInfo').then (json => {
      let { data } = json
      fn(routerMenu.static, data.userInfo)
    }).catch (e => {
      if (!e.errorCode) {
        Vue.prototype.$pop({ type: 'danger', message: '用户信息获取失败，请稍后刷新再试！' })
      }
    })
  },

  role (fn) {
    post('getUserInfo').then (json => {
      let { data } = json
      let roles = data.userInfo.roles
      if (!roles) { warn('roles not in return api!', '/base/menuAuth.js') }
      let jsonRole = ''
      jsonRole = Array.isArray(roles) ? jsonRole = roles.join('|') : roles
      let router = routerMenu[jsonRole] || []
      fn(router, data.userInfo)
    }).catch (e => {
      if (!e.errorCode) {
        Vue.prototype.$pop({ type: 'danger', message: '用户信息获取失败，请稍后刷新再试！' })
      }
    })
  },

  api (fn) {
    post('getUserInfo').then (json => {
      let { data } = json
      fn(data.sysMenu, data.userInfo)
    }).catch (e => {
      console.error(e)
      if (!e.errorCode) {
        Vue.prototype.$pop({ type: 'danger', message: '用户信息获取失败，请稍后刷新再试！' })
      }
    })
  }

}

const MENU_STRATEGY = settings.menuStrategy || 'api'
if (process.env.NODE_ENV !== 'production') {
  if ('static|role|api'.indexOf(MENU_STRATEGY) === -1) {
    warn(MENU_STRATEGY + ' menuStrategy not in the list, place check!', '/base/menuAuth.js')
  }
}

export const getMenuAndUserInfo = Strategy[MENU_STRATEGY]
