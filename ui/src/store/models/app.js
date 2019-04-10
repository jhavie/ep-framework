import cookie from 'utils/cookie'
import settings from 'src/settings'

const SETTING_THEME = settings.theme
const color = 'blue,black,dark-blue,green,pupple,orange'

function getTheme () {
  if (SETTING_THEME === 'black') {
    cookie.setCookie('theme', 'black')
  } else if (cookie.getCookie('theme') === 'black') {
    cookie.delCookie('theme', 'black')
  }
  const theme = cookie.getCookie('theme')
  if (theme !== '' && color.indexOf(theme) !== -1) {
    return theme
  } else {
    cookie.setCookie('theme', 'dark')
    return 'dark'
  }
}

function getDark () {
  const dark = cookie.getCookie('dark')
  if (dark !== '') {
    return dark
  } else {
    cookie.setCookie('dark', '1')
    return '1'
  }
}

function urlFindExist () {
  let url = global.location.href
  let res = false
  let notHideMenu = global.NOT_HIDE_MENU || []
  notHideMenu.every((item) => {
    let $index = url.indexOf(item)
    if ($index !== -1) {
      res = true
      return false
    }
    return true
  })
  return res
}

const initWidthType = document.body.clientWidth > 1024 ? 0: 1

const app = {
  state: {
    routerJson: [],
    changeWidth: initWidthType,
    theme: getTheme(),
    dark: getDark(),
    nowPath: '/',
    showMenu: true,
    noPermissionPage: ''
  },

  mutations: {
    SETROUTERJSON (state, newRouterJson) {
      state.routerJson = newRouterJson
    },
    SETCHANGEWIDTH (state, newChangeWidth) {
      state.changeWidth = newChangeWidth
    },
    SETTHENE (state, newTheme) {
      state.theme = newTheme
    },
    SETDARK (state, newDark) {
      state.dark = newDark
    },
    SETNOWPATH (state, nowPath) {
      state.nowPath = nowPath
    },
    SETSHOWMENU (state, nowMenu) {
      state.showMenu = nowMenu
    },
    SETNOPERMISSIONPAGE (state, newPageName) {
      state.noPermissionPage = newPageName
    }
  },

  actions: {
    setRouterJson ({commit}, routerJson) {
      commit('SETROUTERJSON', routerJson)
    },
    setChangeWidth ({commit}, changeWidth) {
      commit('SETCHANGEWIDTH', changeWidth)
    },
    setTheme ({commit}, theme) {
      let style = ''
      if (color.indexOf(theme) !== -1) {
        style = theme
      } else {
        style = 'blue'
      }
      cookie.setCookie('theme', style)
      commit('SETTHENE', style)
    },
    setDark ({commit}, dark) {
      if (dark === '1' || dark === '0') {
        cookie.setCookie('dark', dark)
        commit('SETDARK', dark)
      }
    },
    setNowPath ({commit}, nowPath) {
      commit('SETNOWPATH', nowPath)
    },
    /**
     * 嵌套iframe策略
     */
    setNowShowPath ({commit}) {
      if (global.parent !== global &&
        !urlFindExist()) {
        commit('SETSHOWMENU', false)
      }
    },
    setNoPermissionPage ({commit}, pageName) {
      commit('SETNOPERMISSIONPAGE', pageName)
    }
  }
}

export default app
