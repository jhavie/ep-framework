/**
 * Ajax Operate
 * Using Fetch API
 * Only Can Handle GET and POST
 * @author: Merjiezo
 * @since: 2018-06-23
 * @version 1.4.2
 */
import fetchJsonp from 'fetch-jsonp'
import {
  API_SETTINGS, RTN_SETTINGS,
  obj2UrlForm, isEmptyObj, RTN_SETTINGS_ASYNC
} from './share/request'

/**
 * Request Instance
 * @param {Object} settings Request Settings
 * 
 * Settings:
 * @param {String} [settings.method] [GET|POST] Request
 * @param {Object} [settings.headers] Headers To Request
 * @param {Boolean} [settings.cors] Cross Domain
 * @param {String} [settings.dataType] Request type [form|json|file|html|xml]
 * @param {String} [settings.rtnType] Response type [json|text|xml|file]
 * @param {Number} [settings.timeout] Timeout to end request
 * @param {Number} [settings.cache] Cache request or not
 * @param {String} [settings.url] Url To Request
 * @param {Object} [settings.data] Data To Request, In Fetch Body
 */
export function request (settings) {
  return settings.timeout
    ? Promise.race([ timeout(settings.timeout), _fetch(settings) ])
    : _fetch(settings)
}

// Same Api as request
// throw Error when network error
export function asyncReq (settings) {
  let result,
      xhr = global.XMLHttpRequest
        ? new XMLHttpRequest()
        : new ActiveXObject("Microsoft.XMLHTTP")

  let params = setParams(settings)
  let xhrObj = params.obj

  xhr.open(settings.method, params.url, false)
  xhr.setRequestHeader('X-Cross-Domain-Ip', global.location.origin)
  for (let key in xhrObj.headers) {
    xhr.setRequestHeader(key, xhrObj.headers[key])
  }
  xhr.onreadystatechange = function () {
    if (xhr.readyState === 4) {
      checkStatus(xhr)
      result = typeof RTN_SETTINGS_ASYNC[settings.rtnType] === 'function'
        ? RTN_SETTINGS_ASYNC[settings.rtnType].call(this, xhr.responseText)
        : xhr.responseText
    }
  }
  xhr.send(xhrObj.body)
  return result
}

/***
 * Jsonp操作类
 * @url    对应的url
 * @fn     成功回调
 * @params 参数obj（可不传）
 * @err    错误函数（可不传）
 */
export function baseJsonp (url, fn, params, err) {
  if (!isEmptyObj(params)) {
    url += '?' + obj2UrlForm(params)
  }

  fetchJsonp(url)
  .then((response) => {
    return response.json()
  }).then((json) => {
    if (fn) { fn.call(this, json) }
  }).catch((e) => {
    if (err) { err.call(this, e) }
  })
}

function setParams (settings) {
  let data = settings.data,
      url = settings.url,
      obj = {
        method: settings.method,
        headers: settings.headers ? { ...settings.headers } : {}
      }
  // Request Header
  if (settings.dataType && undefined !== API_SETTINGS[settings.dataType].contentType) {
    obj.headers['Content-type'] = API_SETTINGS[settings.dataType].contentType
  }
  // Cors
  if (settings.cors) {
    obj.mode = 'cors'
  }
  // Handle Data
  if (obj.method.toUpperCase() !== 'GET'
    && undefined !== data) {
    obj.body = API_SETTINGS[settings.dataType].factoryBody(data)
  } else {
    if (!isEmptyObj(data)) {
      let prefix = url.indexOf('?') === -1 ? '?': '&'
      url += prefix + obj2UrlForm(data)
    }
  }

  return { url, obj }
}

function checkStatus (response) {
  if (response.status >= 200 && response.status < 300) {
    return response
  } else {
    let errorMsg = '网页错误，请稍后再试！'
    if (response.status === 404) {
      errorMsg = '资源不存在！'
    } else if (response.status === 403) {
      errorMsg = '无权访问！'
    } else if (response.status >= 500) {
      errorMsg = '服务器错误，请稍后再试！'
    }
    const error = new Error(errorMsg)
    error.response = response
    throw error
  }
}

function handleResponseData (res, settings) {
  if (typeof RTN_SETTINGS[settings.rtnType] === 'function') {
    return RTN_SETTINGS[settings.rtnType].call(this, res)
  }
  return res.json()
}

function timeout (time) {
  return new Promise((resolve, reject) => {
    let t1 = setTimeout(() => {
      clearTimeout(t1)
      reject(new Error('请求超时！'))
    }, time * 1000)
  })
}

function _fetch (settings) {
  let fetchParams = setParams(settings)
  return fetch(fetchParams.url, fetchParams.obj)
    .then(checkStatus)
    .then(res => handleResponseData(res, settings))
}
