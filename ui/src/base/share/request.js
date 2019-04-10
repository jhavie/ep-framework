import { warn, rtnOrigin } from '../base'

export function obj2UrlForm (object) {
  let result = ''
  for (var item in object) {
    if (object.hasOwnProperty(item)) {
      result += `${item}=${encodeURIComponent(object[item])}&`
    }
  }
  return result.substr(0, result.length - 1)
}

// Rtn Promise Object
export const RTN_SETTINGS = {
  json (res) { return res.json() },
  text (res) { return res.text() },
  xml (res) {
    return res.text().then(str => {
      if ( !str || typeof str !== "string" ) {
        return Promise.resolve(null)
      }
      let xml = (new window.DOMParser()).parseFromString(str, "text/xml")
      return Promise.resolve(xml)
    })
  },
  file (res) {
    let headers = res.headers
    return res.blob().then(blob => {
      blob.headers = headers
      return Promise.resolve(blob)
    })
  }
}

export const RTN_SETTINGS_ASYNC = {
  json (text) { return JSON.parse(text) },
  text (text) { return text },
  xml (text) {
    if ( !text || typeof text !== "string" ) { return null }
    try {
      let xml = (new window.DOMParser()).parseFromString(text, "text/xml")
      return xml
    } catch (e) {
      warn('Xml Error!', '/base/reqInterface.js')
      console.error(e)
    }
    return null
  }
}



export const API_SETTINGS = {
  form: {
    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
    factoryBody (data) { return !isEmptyObj(data) ? toForm(data) : undefined }
  },
  blob: {
    contentType: "application/octet-stream",
    factoryBody: rtnOrigin
  },
  file: {
    factoryBody (data) {
      let formData = new FormData()
      if (null !== data && typeof data === 'object') {
        for (let item in data) {
          formData.append(item, data[item])
        }
      }
      return formData
    }
  },
  json: {
    contentType: "application/json; charset=utf-8",
    factoryBody (data) { return !isEmptyObj(data) ? JSON.stringify(data) : "\{\}" }
  },
  html: {
    contentType: "text/html",
    factoryBody: rtnOrigin
  },
  xml: {
    contentType: "application/xml, text/xml",
    factoryBody: rtnOrigin
  }
}

export const isEmptyObj = function (obj) {
  return obj === undefined || obj === null || JSON.stringify(obj) === '{}'
}

export const RTN_KEYS_STR = Object.keys(RTN_SETTINGS).join('|')
export const RTN_ASYNC_KEYS_STR = Object.keys(RTN_SETTINGS_ASYNC).join('|')
export const API_KEYS_STR = Object.keys(API_SETTINGS).join('|')

function toForm (obj) {
  return typeof obj === 'string'? obj: obj2UrlForm(obj)
}
