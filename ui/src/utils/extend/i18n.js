/**
 * 多语言支撑方法
 * @author: Merjiezo
 * @since: 2016-05-27
 */
import cookie from '../cookie'
import Vue from 'vue'


function getLanguage () {
  const lang = cookie.getCookie('lang')
  if (lang === '') {
    cookie.setCookie('lang', 'zh-CN')
    return 'zh-CN'
  } else {
    return lang
  }
}

let lang = getLanguage()
const json = require(`../../lang/${lang}.json`)
function t(obj) {
  return json[this.$options.name][obj]
}

export default {
  setLang (lang) {
    cookie.setCookie('lang', lang)
    window.location.reload()
  }
}

Vue.prototype.$i18n = t
