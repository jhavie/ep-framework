/**
 * 混杂方法
 */
import { insertDOM, deleteDOM } from 'ep-ui/src/utils/dom'
import { getToken } from './oauth'
import { obj2UrlForm } from './data'
import md5 from 'blueimp-md5'

export function loadWhiteCss () {
  let el = document.createElement('style')
  el.innerHTML = `
    body {
      background: #FFF !important
    }
  `
  let head = document.getElementsByTagName('head')[0]
  head.appendChild(el)
}

// 是否为微信
export function is_weixn() {
  let ua = navigator.userAgent.toLowerCase()
  return ua.indexOf('micromessenger') > 0
}

export function getScript (src, obj, callback) {
  if (!global[obj]) {
    const $script = document.createElement('script')
    document.body.appendChild($script)
    $script.src = src
    $script.onload = function () {
      callback()
    }
  } else {
    callback()
  }
}

/**
 * 接口加密方法
 */
export function calaulatorSign (data, reqParam) {
  // 验签的多种形式
  let signChangeData = '{}'
  let method = reqParam.method
  if (method === 'GET') {
    signChangeData = obj2UrlForm(data) || '{}'
  } else if (method === 'POST') {
    // json删除空白节点
    if (reqParam.dataType === 'json') {
      for (let key in data) {
        if (null === data[key]) {
          delete(data[key])
        }
      }
    }
    signChangeData = data? JSON.stringify(data) : '\{\}'
    // form表单提交，拼装order，后端获取拼接验证
    if (data && reqParam.dataType === 'form') {
      // 规定，去除双引号
      signChangeData = signChangeData.replaceAll("\"", "")
      let orders = []
      for (let key in data) {
        if (data.hasOwnProperty(key)) {
          orders.push(key)
        }
      }
      data.epFrameworkOrder = orders.join(',')
    }
  }
  let today = global.fecha.format(new Date(), 'YYYY-MM-DD')
  return md5(global.MD5_PREFIX + signChangeData + today)
}

const domParams = '<input type="hidden" name="{{name}}" value=\'{{value}}\'>'
/**
 * 发送原生form请求
 */
export function sendOriginFormSubmit (api, data, windowOpen) {
  // sendRequest
  let token    = getToken(),
      jsonData = JSON.stringify(data)
  let reqData = {
    data: jsonData,
    eptoken: token,
    epSign: calaulatorSign({ data: jsonData })
  }
  if (windowOpen) {
    let url = `${global.HOST}${api}?${obj2UrlForm(reqData, true)}`
    return url
  }
  if (document.getElementById('send_request')) {
    deleteDOM('#send_request')
  }
  let domStr = '<form id="send_request" action="" target="_blank" method="post">'
  for (let item in reqData) {
    let domIpt = ''
    if (reqData.hasOwnProperty(item)) {
      domIpt = domParams.replaceAll('{{name}}', item)
      domIpt = domIpt.replaceAll('{{value}}', reqData[item])
      domStr += domIpt
    }
  }
  domStr += '</form>'
  insertDOM('#app', domStr)
  let dom = document.getElementById('send_request')
  dom.action = global.HOST + api
  dom.submit()
}
