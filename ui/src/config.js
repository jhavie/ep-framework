// 全局模块单独打包
// 禁止使用babel和ES6代码在此，使用这些技术框架对此概不负责
import "babel-polyfill"
import fecha from 'fecha'
import '../ieChack'

function changeProtocol (url) {
  var res = url
  if (url.indexOf('http:') !== -1) {
    if (global.location.protocol === 'https:') {
      res = url.replace('http:', 'https:')
    }
  } else {
    res = global.location.protocol + '//' + url
  }
  return res
}

// 时间
global.fecha = fecha
// 参数，webConf为参数定义
global.HOST = changeProtocol(webConf.serverUrl)
global.NOT_HIDE_MENU = webConf.notHideMenu
global.MD5_PREFIX = webConf.md5Prefix
