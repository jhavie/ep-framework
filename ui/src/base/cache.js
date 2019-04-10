/**
 * Cache is Here
 * @author: Merjiezo
 * @since: 2018-07-06
 */
let cache = []
/**
 * menu tab 的简单队列缓存
 * 
 * 业务场景为：
 * 当多编辑页面未加载，写入keep-alive缓存时，此时vue router进行切换时
 * 调用muti-tab-change的时候Vue组件未加载导致内容未被检测到导致加载失败
 * 此队列用来解决第一次加载为空的问题
 * 
 * 注：已通过其他方式解决...
 */
let menuTabQueue = {}

// 弃用
export function addMenuTabQueue (module, mutiId) {
  menuTabQueue[module] = menuTabQueue[module] || ''
  menuTabQueue[module] = mutiId
}
export function getMenuTabQueue (module) {
  let res = menuTabQueue[module]
  clearQueue(module)
  return res
}
export function clearMenuTabQueue (module) {
  delete(menuTabQueue[module])
}
export function clearAllMenuTab () { menuTabQueue = {} }
