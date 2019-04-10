/**
 * [feature request] add cache
 * Menu tab Class
 * manage, store menu data
 * 
 * @author: Merjiezo
 * @since: 2018-07-02
 */
import { merge, next, delSprit, addSprit, findIndexInArrayByParams, findInArrayByParamArr } from './utils'
import { noop } from '../base'

const MENU_TAB_ITEM = {
  id: 0,
  name: '',
  link: '',
  canClose: true,
  mutiId: null,
  isMuti: false,
  meta: {},
  query: {}
}

class MenuTab {

  constructor (def, addCallback) {
    // Menu Tab Item
    this.items = []
    // Active ItemID
    this.active = 0
    this.addCallback = addCallback || noop
    this.addTab(def)
  }

  addTab (tabItem) {
    if (tabItem) {
      if (Array.isArray(tabItem)) {
        tabItem.forEach(item => {
          this.addTab(item)
        })
      } else {
        let item = { ...tabItem }
        merge(item, MENU_TAB_ITEM)
        item.id = next()
        this.items.push(item)
        this.addCallback()
      }
    }
  }

  popItemById (index) {
    return this.items[index - 1]
  }

  findTabItemIndexById (id) {
    return findIndexInArrayByParams(this.items, 'id', id)
  }

  findTabItemById (id) {
    let index = this.findTabItemIndexById(id)
    if (undefined !== index) {
      return this.items[index]
    }
  }

  getTabByLinkAndMutiId (link, mutiId) {
    return findInArrayByParamArr(this.items, ['link', 'mutiId'], [link, mutiId])
  }

  // Only apply mutiId is null
  getTabItemByLink (link) {
    let res
    this.items.every(item => {
      let continueLoop = item.link === link && item.mutiId === null
      if (continueLoop) {
        res = item
      }
      return !continueLoop
    })
    return res
  }

  delTab (index) {
    this.items.splice(index, 1)
  }

  setMaxActive () {
    if (this.items.length > 0) {
      let items = this.items[this.items.length - 1]
      this.active = items.id
    }
  }

  setCurrentActiveById (id) {
    let menuIndex = this.findTabItemIndexById(id)
    if (undefined !== menuIndex) {
      this.active = this.items[menuIndex].id
    }
  }

  tabFuncClick (item, vm) {
    let items = this.items

    if (item === '1') {
      // Close allTabs
      items.splice(2, items.length - 1)
      this.active = this.items[0].id
    } else if (item === '2') {
      // Close otherTabs
      let activeIndex = this.findTabItemIndexById(this.active),
          mlen        = items.length
      
      this.items.forEach((item, index) => {
        if (item.isMuti
          && index !== activeIndex) {
          vm.$app.trigger('muti-tab-delete', delSprit(item.link), item.mutiId)
        }
      })
      if (activeIndex + 1 !== mlen) {
        items.splice(activeIndex + 1, mlen - activeIndex)
      }
      if (activeIndex > 2) {
        items.splice(2, activeIndex - 2)
      }
    }
  }

  getMutiIdListByModule (module) {
    let idList = []
    module = addSprit(module)
    this.items.forEach(item => {
      if (item.link === module) {
        idList.push(item.mutiId)
      }
    })
    return idList
  }
  
}

export default MenuTab
