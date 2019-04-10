/**
 * [feature request] Combine locate-tab and handleTabChange
 * Menu Tab Mixin
 * 
 * Remind To Dev:
 * Main Framework Use, do not Mixin in Biz Develop!
 * 
 * @author: Merjiezo
 * @since: 2018-07-04
 */
import { delSprit, addSprit } from '../share/utils'
import menuTab from '../share/menuTab'
import settings from 'src/settings'
import { warn } from '../base'

function locateToMuti (vm, module, id, query) {
  let item = vm.menuTab.getTabByLinkAndMutiId(addSprit(module), id)
  // locate / add Muti
  if (item) {
    vm.menuTab.active = item.id
  } else {
    let newTab = {
      name: vm.$route.name,
      link: addSprit(vm.$route.path),
      isMuti: true,
      query: query || {}
    }
    if (id) { newTab.mutiId = '' + id }
    vm.menuTab.addTab(newTab)
    vm.menuTab.setMaxActive()
  }
}

function pathIsMuti(vm, module) {
  let key = delSprit(module)
  return vm.$mutiPageObj.hasOwnProperty(key)
}

export default {

  created() {
    if (!this.$router && !this.$route) {
      warn('VueRouter Not Find, Can Case Error, first install VueRouter, then import and install', '/base/mixins/tabMixin.js')
    }
    this.menuTab.setMaxActive()
    this.initDataBus()
  },

  computed: {
    keepAliveInclude () {
      let menuTab = this.menuTab.items
      let res = []
      menuTab.forEach((item) => {
        let addLink = delSprit(item.link)
        if (res.indexOf(addLink) === -1) {
          res.push(addLink)
        }
      })
      return res
    }
  },

  watch: {
    keepAliveInclude: {
      immediate: true,
      handler (val, oldVal) {
        this.$nextTick(_ => {
          this.realKeepAliveInclude = [ ...val ]
        })
      }
    }
  },

  methods: {
    // When Tab Change
    // already have Tab
    handleTabChange (index, tab) {
      this.canChangeTabFlag = false
      this.menuTab.active = tab.id
      let { link, query } = tab
      let newRouter = {
        path: link,
        query: query
      }
      this.$router.push(newRouter)
    },
    tabFuncClick (item) {
      if (item === '1') { this.$router.push(addSprit(settings.homeRoute.path)) }
      this.menuTab.tabFuncClick(item, this)
    },
    /**
     * When Tab close
     * delete mutiId, then delete tab
     * 
     * Close or not depend on mutiPage
     */
    handleClose (index, closedTab) {
      this.$app.trigger('muti-tab-delete', delSprit(closedTab.link), closedTab.mutiId)
      this.menuTab.delTab(index)
    },
    handleMenuChange (to, from) {
      /***
       * 由于left-menu的实现为$route一发生变化就执行change冒泡
       * 这样的实现会在locate-tab到new route时执行Change会导致Tab被重复添加
       * 标志位的加入会判断确保内部逻辑运行时只有点击左侧菜单栏才会执行以下逻辑变化Tab
       * 因此此逻辑判断标志位返回对应内容
       * 定位到之后的逻辑仅处理侧边栏发生变化后的内容
       */
      if (!this.canChangeTabFlag) {
        this.canChangeTabFlag = true
        return
      }
      if (to.path === from.path) { return }

      const path = to.path
      const menuTab = this.menuTab.getTabItemByLink(path)
      if (undefined !== menuTab) {
        this.menuTab.active = menuTab.id
        // if muti, locate to null page (Usually are add page)
        if (pathIsMuti(this, path)) {
          this.$app.trigger('muti-tab-change', delSprit(path), null)
        }
      } else {
        let newTab = {
          name: to.name,
          link: addSprit(path),
          query: to.query || {}
        }
        if (pathIsMuti(this, path)) { newTab.isMuti = true }
        this.menuTab.addTab(newTab)
        this.active = this.menuTab.setMaxActive()
      }
    },
    initDataBus () {
      // Change / Add Tab
      this.$app.on('active-tab', (link, id, query) => {
        locateToMuti(this, link, id, query)
      })
      // Reset Tab to nowPath
      this.$app.on('reset-tab', _ => {
        let path = this.$route.path
        let id = this.$route.query.id || null
        this.menuTab.active = this.menuTab.getTabByLinkAndMutiId(addSprit(path), id)
      })
      this.$app.on('locate-tab', (module, id) => {
        if (pathIsMuti(this, module)) {
          this.canChangeTabFlag = false
        }
        let query = {}
        if (null !== id && typeof id === 'object') {
          query = id
        } else if (id) {
          query.id = id
        }

        this.$router.push({
          path: addSprit(module),
          query: query
        })
      })
      // callback mode
      this.$app.on('get-tabs', (callback) => {
        callback = callback || noop
        callback(this.menuTab.items)
      })
      /***
       * 关闭Tab（目前仅仅实现了close当前Tab，后期添加上关闭特定Tab）
       * 先定位change，再删除，不然会导致OOM
       */
      this.$app.on('close-tab', () => {
        let oldMenuTabActive = this.menuTab.active
        let nowIndex = this.menuTab.findTabItemIndexById(oldMenuTabActive)
        let oldTabItem = this.menuTab.findTabItemById(oldMenuTabActive)
        let tabItem = this.menuTab.popItemById(nowIndex)
        if (!oldTabItem.canClose) {
          this.$warn('CANNOT CLOSE, OPERATE NOT ALLOW!')
          return
        }
        if (tabItem) {
          this.$app.trigger('muti-tab-delete', delSprit(oldTabItem.link), oldTabItem.mutiId)
          let index = this.menuTab.items.indexOf(tabItem)
          this.handleTabChange(index, tabItem)
        }
        this.menuTab.delTab(nowIndex)
      })
      this.$app.on('replace-tab', (module, id) => {
        let oldMenuTabActive = this.menuTab.active
        let oldTabIndex = this.menuTab.findTabItemIndexById(oldMenuTabActive)
        let oldTabItem = this.menuTab.findTabItemById(oldMenuTabActive)
        if (!oldTabItem.canClose) {
          this.$warn('CANNOT CLOSE, OPERATE NOT ALLOW!')
          return
        }
        // Delete VUEInstance and Tab Render
        this.$app.trigger('muti-tab-delete', delSprit(oldTabItem.link), oldTabItem.mutiId)
        this.menuTab.delTab(oldTabIndex)
        // Locate To Tab
        this.$app.trigger('locate-tab', module, id)
      })
    },
    destoryDataBus () {
      this.$app.off('active-tab')
      this.$app.off('reset-tab')
      this.$app.off('locate-tab')
      this.$app.off('get-tabs')
      this.$app.off('close-tab')
      this.$app.off('replace-tab')
    }
  },

  data () {
    // Home Router
    let menuItems = [ { name: settings.homeRoute.name, link: settings.homeRoute.path, canClose: false } ]
    if (menuItems[0].link[0] !== '/') {
      menuItems[0].link = '/' + menuItems[0].link
    }

    let nowPath = this.$route.path
    let meta = this.$mutiPageObj[delSprit(nowPath)] || {}
    let mutiKey = meta.mutiName || 'id'
    let nowId = this.$route.query[mutiKey] ? this.$route.query[mutiKey] : null
    if (nowPath !== '/' + settings.homeRoute.path) {
      menuItems.push({
        name: this.$route.name,
        link: nowPath,
        isMuti: pathIsMuti(this, nowPath),
        mutiId: nowId,
        query: this.$route.query || {}
      })
    }
    return {
      realKeepAliveInclude: [],
      canChangeTabFlag: true,
      menuTab: new menuTab(menuItems, _ => {
        if (this.$refs.frameTab) {
          this.$refs.frameTab.addProp()
        }
      })
    }
  },

  beforeDestroy() {
    this.destoryDataBus()
  }

}
