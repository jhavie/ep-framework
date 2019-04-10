<template>
<div class="panel-main-content" style="padding-bottom:0px;">
  <!--筛选栏组-->
  <div class="search-card contents-card card-margin">
    <div class="panel panel-default ">
      <div class="panel-heading" style="margin-bottom: 20px;">查询条件</div>
      <div class="panel-body">
        <ep-form inline ref="searchForm" :form="searchForm">
          <ep-form-item attr="like_paraName">
            <ep-input v-model="searchForm.like_paraName" placeholder="参数名"></ep-input>
          </ep-form-item>
          <ep-form-item>
            <ep-button type="warning" size="small" @click="reset('searchForm')">重置</ep-button>
            <ep-button type="primary" size="small" icon="search" @click="refresh(true)">查询</ep-button>
          </ep-form-item>
        </ep-form>
      </div>
    </div>
  </div>
  <div class="ep-card card-margin" style="position: relative">
    <div v-if="selectLength !== 0" class="ep-table-selected-header">
      选择了 {{ selectLength }} 项
      <span style="text-align: right">
        <ep-button type="text" icon="trash-a" @click="doDelete"></ep-button>
      </span>
    </div>
    <div class="card-body">
      <div class="block" :style="{ opacity: selectLength === 0? 1: 0 }">
        <ep-button type="primary" size="small" @click="doAdd" icon="plus">新增</ep-button>
        <ep-button type="success" size="small" @click="doSave" icon="edit">保存</ep-button>
        <ep-button type="warning" size="small" @click="doReset" icon="pricetag">重置</ep-button>
        <ep-button type="info" size="small" @click="doRefresh" icon="ios-refresh">刷新</ep-button>
      </div>
      <div class="block">
        <ep-table ref="table" :data="ep_data" :height="700" can-edit
          @selection-change="handleSelectionChange" :loading="loading">
          <ep-table-item type="select"></ep-table-item>
          <ep-table-item column="paraName" title="参数名" width="230"></ep-table-item>
          <ep-table-item column="paraValue" title="参数值" ></ep-table-item>
          <ep-table-item column="paraDesc" title="参数说明"></ep-table-item>
        </ep-table>
      </div>
      <div class="block">
        <ep-pager right @size-change="handleSizeChange" @change="handleCurrentChange"
          :now-page="ep_page.offset" :page-size="ep_page.limit" :total-num="totalcount"></ep-pager>
      </div>
    </div>
  </div>
</div>  
</template>

<script>
  import misList from 'src/base/mixins/mislist'

  export default {

    extends: misList,

    name: 'commConfig',

    created () {
      this.refresh(true)
    },
    
    data () {
      return {
        loading: false,
        listApi: 'commConfigSearch',
        saveApi: 'commConfigSave',
        settings: {
          pk: 'paraName'
        },
        searchForm: {
          like_paraName: '',
          // order_paraName: 'desc',
          // sort_desc: 'paraName'
        },
        selectLength: 0,
        totalcount: 0,
        ep_page: {
          limit: 10,
          offset: 1
        },
        ep_data: []
      }
    },
    methods: {


    },
    computed: {
        totalShow() {
            return '此模块一共有' + this.totalcount + '条数据'
        }
    },
  }
</script>
