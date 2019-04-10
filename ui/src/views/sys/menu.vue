<template>
  <div class="panel-main-content">
    <!--筛选栏组-->
    <div class="search-card contents-card card-margin">
      <div class="panel panel-default">
        <div class="card-title zero-padding"><span class="weight">菜单列表</span></div>
        <ep-form ref="searchForm" :form="searchForm" name-width="90px">
          <ep-row :gutter="7">
            <ep-col :col="6">
              <ep-form-item attr="eq_menuCode" label="菜单编码">
                <ep-input placeholder="菜单编码" v-model="searchForm.eq_menuCode" name="eq_menuCode" :maxlength="20"></ep-input>
              </ep-form-item>
            </ep-col>
            <ep-col :col="6">
              <ep-form-item attr="eq_parentCode" label="父菜单编码">
                <ep-input placeholder="父菜单编码" v-model="searchForm.eq_parentCode" name="eq_parentCode" :maxlength="20"></ep-input>
              </ep-form-item>
            </ep-col>
            <ep-col :col="6">
              <ep-form-item attr="eq_menuName" label="菜单名称">
                <ep-input placeholder="菜单名称" v-model="searchForm.eq_menuName" name="eq_menuName" :maxlength="100"></ep-input>
              </ep-form-item>
            </ep-col>
            <ep-col :col="6">
              <ep-button type="warning" size="small" @click="reset('searchForm')">重置</ep-button>
              <ep-button type="primary" size="small" @click="refresh(true)" icon="search" :loading="loading">查询</ep-button>
            </ep-col>
          </ep-row>
        </ep-form>
      </div>
    </div>
    <!--表格-->
    <div class="ep-card card-margin relative">
      <div v-if="selectLength !== 0" class="ep-table-selected-header">
        选择了 {{ selectLength }} 项
        <span style="text-align: right">
          <ep-button type="text" icon="trash-a" @click="doDelete"></ep-button>
        </span>
      </div>
      <div class="card-body">
        <div class="block">
          <ep-button type="primary" size="small" @click="doAdd" icon="plus">新增</ep-button>
          <ep-button type="success" size="small" @click="doSave" icon="edit">保存</ep-button>
          <ep-button type="warning" size="small" @click="doReset" icon="pricetag">重置</ep-button>
          <ep-button type="primary" size="small" @click="doRefresh" icon="ios-refresh">刷新</ep-button>
        </div>
        <div class="block">
          <ep-table ref="table" :data="ep_data" :height="700"
            @selection-change="handleSelectionChange" can-edit :loading="loading">
            <ep-table-item type="select"></ep-table-item>
            <ep-table-item column="id" title="id" width="120" :is-edit="false"></ep-table-item>
            <ep-table-item column="parentCode" title="parentCode" width="140"></ep-table-item>
            <ep-table-item column="menuCode" title="menuCode" width="140"></ep-table-item>
            <!--<ep-table-item column="menuIcon" title="menuIcon" width="180"></ep-table-item>-->
            <ep-table-item column="menuName" title="menuName" width="140"></ep-table-item>
            <ep-table-item column="menuNameEn" title="menuNameEn" width="140"></ep-table-item> 
            <ep-table-item column="menuUrl" title="menuUrl" width="180"></ep-table-item>
            <ep-table-item column="menuAlter" title="menuAlter" width="180"></ep-table-item>
            <ep-table-item column="menuOrder" title="menuOrder"></ep-table-item>
            <ep-table-item column="notes" title="notes" width="150"></ep-table-item>
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
    name: 'menu',

    extends: misList,

    created () {
      this.refresh(true)
    },

    mounted () {
      
    },

    data () {
      return {
        loading: false,
        listApi: 'menusSearch',
        saveApi: 'menuSave',
        settings: {
          pk: 'id'
        },
        searchForm: {
          eq_menuCode: '',
          eq_parentCode: '',
          eq_menuName: '',
          orderBy: 'id,asc'
        },
        selectLength: 0,
        totalcount: 0,
        ep_page: {
          limit: 10,
          offset: 1
        },
        ep_data: []
      }
    }
  }
</script>
