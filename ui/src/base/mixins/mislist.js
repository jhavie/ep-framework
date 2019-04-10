import { post, get, isEmpty, cloneObj } from "utils"

const utils = {
  merge (target, source) {
    for (let key in source) {
      target[key] = source[key]
    }
  },
  trim: function(str) {
    if (typeof str === 'string') {
      return str.replace(/(^\s*)|(\s*$)/g, "");
    }
    return str
  },
  isEmpty: function (obj) {
    if (!obj && obj !== 0) { return true }
    if (typeof obj === 'string') {
      return obj === ''
    } else if (obj instanceof Array) {
      return obj.length === 0
    }
    return false
  },
  data2Number (form, arr) {
    if (Array.isArray(arr)) {
      arr.forEach((item) =>　{
        if (form.hasOwnProperty(item)) {
          let number = + form[item]
          form[item] = number || + form[item]
        }
      })
    }
  }
}

function obj2Json(data) {
  let res = []
  data.forEach((item) => {
    res.push(JSON.stringify(item))
  })
  return res
}

function keyCollect (data, pk) {
  let res = []

  data.forEach((item) => {
    res.push(item[pk])
  })
  return res
}

/**
 * 管理信息系统list功能封装
 * 所有列表内按钮方法的命名方式为handle+执行的动作
 * 所有按钮区的方法的命名方式为do+执行的动作
 * 如移除就是doRemove
 * 框架自己封装了一些方法如doAdd, doRemove, doChange
 * 这些关键字不能做定义，否则会发生修改错误
 * 框架统一了data的定义
 *  1、MIS模块的配置在open_setting上
 *  2、ep_data为表格内部数据
 *  3、ep_page为分页数据
 *  4、dom_data内部为dom变化所对应的数据
 *  5、重写created方法的时候需要加一句this.refresh，否则列表数据不会请求
 */
export default {

  created () {
    if (this.settings.isGet) {
      this.realRequest = this.searchGet
    } else {
      this.realRequest = this.searchPost
    }
  },

  computed: {
    showTotal () {
      return '此模块一共有共有' + this.ep_page.totalNum + '条数据'
    }
  },

  methods: {
    doAdd () {
      this.$refs.table.addRow()
    },
    doDelete () {
      this.$refs.table.deleteSelectRow()
    },
    doSave () {
      let vm = this
      this.$refs.table.getSaveData(function (data) {
        vm.loading = true
        var reqData = {}
        for (let item in data) {
          if (data.hasOwnProperty(item)) {
            if (item === 'delete') {
              try {
                reqData[item] = keyCollect(data[item], vm.settings.pk)
              } catch (e) {
                this.$pop({
                  type: 'danger',
                  message: "配置项缺失或配置错误的主键！",
                  hasClose: true
                })
                return
              }
            } else {
              reqData[item] = data[item]
            }
          }
        }
        post(vm.saveApi, reqData, true)
        .then((responseData) => {
          vm.refresh()
        }).catch (e => {
          console.error(e)
          vm.loading = false
        })
      })
    },
    doReset () {
      this.$refs.table.reset()
    },
    doRefresh() {
      this.refresh(false)
    },
    reset(formName) {
      this.$refs[formName].reset()
      // utils.merge(this[formName], this.settings.resetDataObj)
    },
    sigclick () {
      this.ep_data.unshift({ ...this.rowObj })
      this.$refs.table.setCurrentRow(this.ep_data[0])
    },
    getFormData () {
      let formData = {}
      for (let variable in this.searchForm) {
        if (this.searchForm.hasOwnProperty(variable)) {
          let innerVal = this.searchForm[variable]
          if (!utils.isEmpty(innerVal)) {
            formData[variable] = utils.trim(innerVal)
          }
        }
      }
      utils.data2Number(formData, this.settings.isNumber)
      return formData
    },
    searchPost (initPage) {
      let body = this.getReqPrepared(initPage)
      post(this.listApi, body)
      .then((responseData) => {
        this.getReqDone(responseData)
      }).catch (e => {
        this.loading = false
      })
    },
    searchGet (initPage) {
      let body = this.getReqPrepared(initPage)
      let listApi = { ...this.listApi }
      listApi.url += this[this.entityKey] || ''
      get(listApi, body)
      .then((responseData) => {
        this.getReqDone(responseData)
      }).catch (e => {
        this.loading = false
      })
    },
    getReqPrepared (initPage) {
      this.loading = true
      let formData = this.getFormData()
      if (initPage) {
        formData.limit = this.ep_page.limit
        formData.offset = 1
        this.ep_page.offset = 1
      } else {
        formData.limit = this.ep_page.limit
        formData.offset = this.ep_page.offset
      }
      return formData
    },
    getReqDone (responseData) {
      let dataRows = this.settings.rowsName || 'rows'
      this.totalcount = responseData.data.total
      this.loading = false
      if (this.searchCallback
        && typeof this.searchCallback === 'function') {
        this.searchCallback(responseData)
      } else {
        this.ep_data = responseData.data[dataRows]
      }
    },
    refresh(initPage) {
      if (this.$refs.searchForm) {
        this.$refs.searchForm.validate((valid) => {
          if (valid) {
            this.realRequest(initPage)
          }
        })
      } else {
        this.realRequest(initPage)
      }
    },

    handleSelectionChange(val) { this.selectLength = val.length },

    handleSizeChange(val) {
      this.ep_page.limit = val
      this.refresh(false)
    },
    handleCurrentChange(val) {
      this.ep_page.offset = val
      this.refresh(false)
    },

    //改变每页显示数量时
    handleChangePageSize (size) { this.open_page.showNum = size; this.refresh() },
  },
  // beforeDestroy () {
  //   let data = this.$store.getters.getPageData;
  //   data[this.ep_config.frontPath] = this.$data;
  //   this.$store.dispatch('changePageData', data);
  // },
  //取消焦点
  deactivated() {
    document.body.click()
  }
}
