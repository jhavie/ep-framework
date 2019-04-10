/**
 * 权限、菜单、登录登出控制
 * 采用低耦合Vuex分发策略
 * 而非高耦合整和Vuex的策略
 * @author: Merjiezo
 * @since: 2017-05-26
 * @version: 1.3.6
 */
import Vue from 'vue'

import { post } from './fetch'
import store from '../store'
import { validatAlphabets } from './validate'
import router from '../router'
import { getMenuAndUserInfo } from '../base/menuAuth'
import settings from '../settings'
import cookie from './cookie'
import localStorage from './localStorage'

const OUT_DATE = 15
const TOKEN_STRICT = settings.tokenStrict || false
const TOKEN_NAME = settings.tokenName || 'eptoken'

/*cookie过期时间——5天*/
const cookieOutDate = 5
/**白名单存储在此 */
let menus = null

/** 白名单列表，适合权限系统和菜单配置对应的系统 */
function menuToArr (menus) {
  let res = [];

  menus.forEach((item) => {
    if (item.router) {
      res.push(item.router)
    }
    if (item.children !== undefined
      && item.children.length > 0) {
      let children = item.children
      children.forEach((item) => {
        if (item.router) {
          res.push(item.router)
        }
      })
    }
  })
  return res
}

// 清空路由，用户信息、无权限页面缓存
function dispatchStore (router, userInfo) {
  store.dispatch('setRouterJson', router)
  store.dispatch('setUserInfo', userInfo)
  store.dispatch('setNoPermissionPage', '')
}

/** 获取菜单和用户信息 **/
function getUserInfo (fn) {
  getMenuAndUserInfo ((router, userInfo) => {
    menus = menuToArr(router)
    dispatchStore(router, userInfo)
    fn()
  })
}

/** 登陆 */
export function login (vm, fn) {
  vm.$refs['form'].validate((valid) => {
    if (valid) {
      vm.isLoading = true
      post('sysLogin', vm.form)
      .then(json => {
        Vue.prototype.$pop({
          type: 'success',
          message: '登陆成功'
        })
        setToken(json.data.rt)
        vm.isLoading = false
        fn()
      }).catch(e => {
        vm.isLoading = false
        if (e.errorCode === '001') {
          vm.loadCaptcha()
        }
      })
    }
  })
}

/** 登出 **/
export function logout () {
  const token = getToken()
  if (token !== '') {
    post('sysLogout', {
      refreshToken: token
    }).then(json => {
      Vue.prototype.$pop({
        type: 'success',
        message: '注销成功'
      })
      delToken()
      router.push('login')
    }).catch(e => {})
  } else {
    router.push('login')
  }
}

export function resetAuth () {
  menus = null
  dispatchStore([], {
    orgName: '',
    orgCode: '',
    userLoginId: '',
    username: '',
    userTele: '',
    draccNo: '',
    roles: ''
  })
}

// router | name | menuCode
export function getFillterMenuList (value) {
  let res = [],
      router = store.getters.getRouterJson,
      key = validatAlphabets(value) ? 'menuCode' : 'name'
  router.forEach((item) => {
    if (Array.isArray(item.children) && item.children.length > 0) {
      item.children.forEach(innerItem => {
        let { router, menuCode, name } = innerItem
        if (innerItem[key].toUpperCase().indexOf(value.toUpperCase()) !== -1)
          res.push({ router, menuCode, name })
      })
    } else {
      let { router, menuCode, name } = item
      if (item[key].toUpperCase().indexOf(value.toUpperCase()) !== -1)
        res.push({ router, menuCode, name })
    }
  })
  return res
}

/***
 * 获取用户权限(Lazy Load)
 * @route    路由路径
 * @callback 回调函数
 */
export function getPermission (route, callback) {
  
  function handleCallback() {
    menus.indexOf(route) === -1
      ? callback.call(this, false)
      : callback.call(this, true)
  }
  if (route.startWith('/')) {
    route = route.substring(1)
  }
  null === menus
    ? getUserInfo(_ => { handleCallback() })
    : handleCallback()
}

/**
 * 获取用户Token
 */
export function getToken () {
  return store.getters.getToken
}

export function setToken (token) {
  TOKEN_STRICT
    ? cookie.setProcessCookie(TOKEN_NAME, token)
    : localStorage.setLocalStorage(TOKEN_NAME, token, OUT_DATE)
  store.dispatch('setToken', token)
}

/**
 * 获取用户Token
 */
export function delToken () {
  TOKEN_STRICT
  ? cookie.delCookie(TOKEN_NAME)
  : localStorage.delLocalStorage(TOKEN_NAME)
  resetAuth()
  store.dispatch('setToken', '')
}
