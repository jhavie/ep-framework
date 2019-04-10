<template>
  <div class="panel-main-content">
    <div style="font-size: 48px">{{id}}</div>
    <div class="contents-card card-margin">
      <div class="ep-detail-title weight">支付订单汇总</div>
      <ep-form name-width="120px" name-position="left">
        <ep-row>
          <ep-col v-for="(value, key, index) in usefeePayRequest" :key="index"
            :col="8">
            <ep-form-item :label="value">
              <ep-input v-model="detailResponse[key]"></ep-input>
            </ep-form-item>
          </ep-col>
        </ep-row>
      </ep-form>
      <div class="ep-line-horizontal"></div>
      <div class="ep-detail-title weight">支付订单汇总</div>
      <ep-form name-width="120px" name-position="left">
        <ep-row>
          <ep-col v-for="(value, key, index) in usefeePayResponse" :key="index"
            :col="8">
            <ep-form-item :label="value">
              <ep-input v-model="detailResponse[key]"></ep-input>
            </ep-form-item>
          </ep-col>
        </ep-row>
      </ep-form>
      <button @click="test">{{test1}}</button>
    </div>
  </div>
</template>

<script>

  export default {

    name: 'maps',

    methods: {
      test () {
        this.$router.push({
          path: 'map',
          query: { id: 123 }
        })
        this.test1 += 'fsdhfjkahklfjasklhfjkhsdjkl'
      }
    },

    mounted() {
      let id = this.$el.dataset.id
      if (id === 'add') {

      } else {
        this.id = id
      }
      
    },

    data () {
      return {
        id: '',
        test1: '123',
        detailResponse: {
          accountBalance: 1024289,
          bizType: "JKFX",
          chargeAccount: "1005#000001",
          checkBalance: "1",
          checkResult: "0",
          createDate: "2018-05-28 09:51:53",
          crtCode: "132208081",
          curr: "CNY",
          dbtCode: "ECS_HD",
          detailCount: 1,
          detailType: "1604",
          flag: "0",
          isDetail: "0",
          loginId: "ecstest",
          onlineOffline: "0",
          orderCode: "ECS_HD#20180528YD38413576",
          paymentCode: "1302",
          serialNo: "EPASS002018052800000314165",
          serialNoDate: "2018-05-28 09:52:41",
          serialNoModifyDate: "2018-05-28 09:52:41",
          serviceCode: "ECS_PAY_FEE_0001",
          systemCode: "ECS",
          totalAmount: 8,
          usefeePayId: 1892
        },
        usefeePayRequest: {
          serialNo: "交易流水号",
          serviceCode: "服务代码",
          loginId: "用户登录名",
          serialNoDate: "流水号创建时间",
          serialNoModifyDate: "流水号更新时间",
          checkBalance: "校验余额",
          flag: "记账标记",
          systemCode: "数据来源",
          isDetail: "是否显示明细数据"
        },
        usefeePayResponse: {
          totalAmount: "扣款总金额",
          detailCount: "明细笔数",
          checkResult: "扣费状态",
          chargeAccount: "费收账号",
          accountBalance: "费收账号账户余额",
          orderCode: "扣费订单号",
          createDate: "扣费订单创建时间",
          curr: "货币",
          desciption: "描述"
        }
      }
    }
  }
</script>
