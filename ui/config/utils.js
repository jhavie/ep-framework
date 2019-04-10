/**
 * EP Vue 基础配置工具方法集
 * @author: Merjiezo
 * @since: 2018-09-28
 * @version: 1.0.0
 */
const webpack = require('webpack')
const path = require('path')
const config = require('../config')
const DEF = require('./def')

let CACHE_EP_APP = null

function cloneObj (obj) {
  let str, newobj = obj.constructor === Array ? [] : {}
  if (typeof obj !== 'object') {
    return
  } else if(global.JSON) {
    str = JSON.stringify(obj)
    newobj = JSON.parse(str)
  } else {
    for (var i in obj) {
      newobj[i] = typeof obj[i] === 'object'
        ? cloneObj(obj[i])
        : obj[i]
    }
  }
  return newobj
}

// Cache EPApp
function cacheEPApp (env) {
  if (null === CACHE_EP_APP) {
    CACHE_EP_APP = {}
    for (let key in config) {
      if (config.hasOwnProperty(key)) {
        CACHE_EP_APP[env] = cloneObj(config[env].EPApp)
      }
    }
  }
}

function getEPApp () {
  let epBuildEnv = DEF.BUILD_ENV
  if (CACHE_EP_APP) {
    return CACHE_EP_APP[epBuildEnv] || {}
  } else {
    return config[epBuildEnv].EPApp || {}
  }
}

exports.defPlugin = function () {
  const EPApp = getEPApp()
  const webConf = cloneObj(EPApp.webConf || {})
  if (!webConf.serverUrl) {
    serverUrl = '"./"'
  }
  return new webpack.DefinePlugin({
    webConf
  })
}

exports.cloneObj = cloneObj

// 获取应用EPApp配置
exports.getEPApp = getEPApp;

exports.resolve = function (dir) {
  return path.join(__dirname, '..', dir)
}

// 根据不同环境获取Vue Config
exports.getConfig = function (isDel) {
  const epBuildEnv = DEF.BUILD_ENV
  
  cacheEPApp(epBuildEnv)
  let vueConfig = config[epBuildEnv] || config.dev
  // 删除自定义配置节点
  if (isDel) {
    delete(vueConfig.EPApp)
  }
  return vueConfig
}

exports.getEpEntryDir = function () {
  let epEntryDir = process.env.npm_config_EP_ENTRY_DIR
  if (!epEntryDir || epEntryDir === '$npm_config_EP_ENTRY_DIR') {
    epEntryDir = 'src'
  }
  return epEntryDir
}
