let uuid = 0
export const next = _ => ++uuid
export const delSprit = path => path[0] === '/'? path.replace('/', ''): path
export const addSprit = path => path[0] === '/'? path : '/' + path

export function merge (obj, def) {
  for (let key in def) {
    if (!obj.hasOwnProperty(key)) {
      obj[key] = def[key]
    }
  }
}

export function findInArrayByParamArr (arr, keyArr, valueArr) {
  if (Array.isArray(arr)) {
    let len = keyArr.length, currentItem, res
    arr.every((item) => {
      let currentNum = 0
      keyArr.forEach((compareKey, index) => {
        if (item[compareKey] === valueArr[index]) {
          currentNum += 1
        }
      })
      res = currentNum === len
      currentItem = res ? item : undefined
      return !res
    })
    return currentItem
  }
}

export function findIndexInArrayByParams (arr, key, value) {
  if (Array.isArray(arr)) {
    let currentIndex
    arr.every((item, index) => {
      let res = item[key] === value
      currentIndex = res ? index : undefined
      return !res
    })
    return currentIndex
  }
}
