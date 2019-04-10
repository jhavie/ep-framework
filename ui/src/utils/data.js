/**
 * 自定义和数据库操作混合类
 * 解决一些框架耦合
 * 框架生命周期钩子的问题
 * 整理table对外暴露的内容为全新的数据
 */
import settings from 'src/settings'

/**
 * 对象变fetch的form类型传参
 */
export const obj2UrlForm = function (object, escape) {
  let result = ''
  for (var item in object) {
    if (object.hasOwnProperty(item)) {
      escape
        ? result += `${item}=${encodeURIComponent(object[item])}&`
        : result += `${item}=${object[item]}&`
    }
  }
  return result.substr(0, result.length - 1)
}


/**
 * 接口错误方法
 * 可跟根据接口模式覆盖
 * @param {*接口obj} object
 */
export const dataErr = function (object) {
  // return object.flag === "T"
  if (object.flag) {
    return object.flag === 'T'
  }
  // 兼容之前的接口
  if (object.code) {
    return object.code === '1'
  }
}

/**
 * 
 * @param {*} filterVal
 * @param {*} jsonData
 */
export const formatJson = function (filterVal, jsonData) {
  return jsonData.map(v => filterVal.map(j => v[j]))
}

/**
 * 深拷贝对象或数组
 * @param {*要拷贝的Object或者Array} obj
 */
export function cloneObj (obj) {
  let str, newobj = obj.constructor === Array ? [] : {}
  if (typeof obj !== 'object') {
    return
  } else if(window.JSON) {
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

/**
 * 为object添加属性
 * @param {*待添加的data} data
 * @param {*需要添加的属性} props
 */
export function addProps (data, props) {
  let length = data.length
  for (let i = 0; i < length; i++) {
    for (let key in props) {
      data[i][key] = props[key]
    }
  }
}

/**
 * 合并object
 * @param {*目标对象} target
 * @param {*合并的对象} source
 */
export function merge (target, source) {
  let res = { ...target }
	for (var key in source) {
		res[key] = source[key]
	}
  return res
}

// 合并对象，如果target内没有对象结构则不合并
export function mergeNotInObject (target, source) {
  let res = { ...target }
  for (var key in source) {
    if (target.hasOwnProperty(key)) {
		  res[key] = source[key]
    }
	}
  return res
}

/**
 * Object是否为空
 * @param {*对象} object
 */
export function objIsEmpty (object) {
  return object === undefined ||
         object === null ||
         JSON.stringify(object) === '{}'
}

/**
 * 是否为空
 * @param {*参数项} item 
 */
export function isEmpty (item) {
  return item === undefined || item === null || item === ''
}

export function isArray (v) {
  return v instanceof Array
}

/**
 * $ref去重
 */
export function refRemove (key, json) {
  let keyArr = key.split('.')
  keyArr.splice(0, 1)
  let obj = { ...json }
  if (keyArr.length > 0) {
    keyArr.forEach((item) => {
      if (item.indexOf('[') !== -1) {
        let itemStr = item.replace(']', '')
        let itemArr = itemStr.split('[')
        obj = obj[itemArr[0]][itemArr[1]]
      } else {
        obj = obj[item]
      }
    })
  }
  return obj
}
