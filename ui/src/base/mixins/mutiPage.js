/**
 * Muti Page Cache Mixin
 * @author: Merjiezo
 * @since: 2018-07-04
 */
import { delSprit } from '../share/utils'

export default {

  created() {
    this.$app.on('muti-tab-change', this.mutiTabChange)
    this.$app.on('muti-tab-delete', this.mutiTabDelete)
  },

  mounted() {
    let menuTab, parent = this.$parent
    while (parent) {
      if (parent.menuTab) {
        menuTab = parent.menuTab
        break
      } else {
        parent = parent.$parent
      }
    }
    let key = this.$route.meta.mutiName || 'id'
    let id = this.$route.query[key]? '' + this.$route.query[key]: null
    this.idList.push(id)
    this.active = id

    this.$app.trigger('active-tab', this.$options.name, id, this.$route.query)
  },

  watch: {
    '$route' (to, from) {
      let module = delSprit(to.path)
      if (this.$options.name === delSprit(to.path) && 
        to.meta.muti) {
        let key = to.meta.mutiName || 'id'
        let id = to.query[key]? '' + to.query[key]: null
        this.mutiTabChange (to.path, id, to.query)
      }
    }
  },

  data () {
    return {
      idList: [],
      active: null
    }
  },

  methods: {
    mutiTabChange (module, id, query) {
      let setId = id ? '' + id: null
      if (this.idList.indexOf(id) === -1) {
        this.idList.push(setId)
      }
      this.active = setId
      this.$app.trigger('active-tab', module, id, query)
    },
    mutiTabDelete (module, id) {
      if (this.$options.name === module) {
        let index = this.idList.indexOf(id)
        if (index !== -1) {
          this.idList.splice(index, 1)
        }
      }
    }
  },

  // 开启后直接关闭不调用Destory
  beforeDestroy() {
    this.$app.off('muti-tab-change', this.mutiTabChange)
    this.$app.off('muti-tab-delete', this.mutiTabDelete)
  }

}
