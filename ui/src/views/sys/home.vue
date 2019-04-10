<template>
	<div class="panel-main-content">
    <h3 style="margin: 0; padding-left: 10px;">欢迎您，EASIPASS，您正在使用EP Framework 1.7.0版本</h3>
    <ep-row :gutter="10" style="padding-top: 20px; margin-bottom: 0px">
      <ep-col :col="6" class="dashboard-block dashboard-pv">
        <dashboard :show="showNum" :dashboard="dashboard1"
          color="#3498db"></dashboard>
      </ep-col>
      <ep-col :col="6" class="dashboard-block">
        <dashboard :show="showNum" :dashboard="dashboard2"
          color="#1abc9c"></dashboard>
      </ep-col>
      <ep-col :col="6" class="dashboard-block">
        <dashboard :show="showNum" :dashboard="dashboard3"
          color="#8e44ad"></dashboard>
      </ep-col>
      <ep-col :col="6" class="dashboard-block">
        <dashboard :show="showNum" :dashboard="dashboard4"
          color="#f39c12"></dashboard>
      </ep-col>
    </ep-row>
    <ep-row :gutter="10">
      <ep-col :col="16" class="dashboard-md">
        <div class="contents-card dashboard-link">
          <h5>登陆人数</h5>
          <div id="dashboard-link"></div>
        </div>
      </ep-col>
      <ep-col :col="8" class="dashboard-md">
        <div class="dashboard-md-left main-col">
          <weather :theme="$store.getters.getTheme"></weather>
        </div>
        <div class="dashboard-md-right main-col">
          <div class="contents-card dashboard-nav">
            <ep-badge is-dot>
              <h5>通知</h5>
            </ep-badge>
            <p class="no-data">暂无通知</p>
          </div>
        </div>
      </ep-col>
      <ep-col :col="24">
        <div class="contents-card dashboard-news">
          <h5>新闻动态</h5>
          <div class="no-data" v-if="news.length === 0">
            加载中...
          </div>
          <ul>
            <li class="dashboard-news-item dashboard-has-time" v-for="item in news" :key="item.id">
              <a :class="'panel-' + $store.getters.getTheme + '-text'"
                href="javascript:void(0)" @click="handleNewsClick(item.id)">
                <template v-if="item.memos && item.memos.indexOf('red') !== -1">
                  <font color="#E74C3C">{{item.title}}</font>
                </template>
                <template v-else>{{item.title}}</template>
              </a>
              <small><i class="ion-ios-clock"></i>{{item.pubTime}}</small>
            </li>
          </ul>
        </div>
      </ep-col>
    </ep-row>
    <ep-model :title="newsDetail.title" v-model="newsModel" width="800px">
      <div v-html="newsDetail.content"></div>
    </ep-model>
  </div>
</template>

<script>
  import { post, get, jsonp } from "utils"
  import weather from "../../framework/widgets/weather"
  import dashboard from "../../framework/widgets/dashboard"
  import echarts from 'echarts';
  const color = ['#0099FF', '#00CC99', '#63B3FC', '#99CCCC', '#9999FF', '#FA5793', '#FF9966', '#FF6600', '#996633', '#666']
  const optionLight = {
    color: color,
    tooltip: {
        trigger: 'axis'
    },
    legend: {
        data:['报关模块', '亿加付费模块', '核放单模块', '应用总数']
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
    },
    xAxis: {
        type: 'category',
        boundaryGap: false,
        data: ['周一','周二','周三','周四','周五','周六','周日']
    },
    yAxis: {
        type: 'value'
    },
    series: [
        {
          symbolSize: 10,
          smooth: true,
          lineStyle: {
            normal: {
              width: 5
            }
          },
          name:'报关模块',
          type:'line',
          stack: '总量',
          data:[120, 132, 101, 134, 90, 230, 210]
        },
        {
          symbolSize: 10,
          smooth: true,
          lineStyle: {
            normal: {
              width: 5
            }
          },
          name:'亿加付费模块',
          type:'line',
          stack: '总量',
          data:[220, 182, 191, 234, 290, 330, 310]
        },
        {
          symbolSize: 10,
          smooth: true,
          lineStyle: {
            normal: {
              width: 5
            }
          },
          name:'核放单模块',
          type:'line',
          stack: '总量',
          data:[150, 232, 201, 154, 190, 330, 410]
        },
        {
          symbolSize: 10,
          smooth: true,
          lineStyle: {
            normal: {
              width: 5
            }
          },
          name:'回收模块',
          type:'line',
          stack: '总量',
          data:[320, 332, 301, 334, 390, 330, 320]
        },
        {
          symbolSize: 10,
          smooth: true,
          lineStyle: {
            normal: {
              width: 5
            }
          },
          name:'应用总数',
          type:'line',
          stack: '总量',
          data:[820, 932, 901, 934, 1290, 1330, 1320]
        }
    ]
  };
  const optionBlack = {
    color: color,
    backgroundColor: '#353b47',
    tooltip: {
        trigger: 'axis'
    },
    legend: {
        data:['报关模块', '亿加付费模块', '核放单模块', '应用总数'],
        textStyle: {
          color: '#eaeaea',
          // fontSize: 16
        }
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
    },
    xAxis: {
        type: 'category',
        boundaryGap: false,
        data: ['周一','周二','周三','周四','周五','周六','周日'],
        axisLine: {
          lineStyle: {
            color: '#97a2b6'
          }
        }
    },
    yAxis: {
        type: 'value',
        // axisLine: {
        //   lineStyle: {
        //     color: '#eaeaea'
        //   }
        // },
        axisLine: {
          show: false,
          lineStyle: {
            color: '#97a2b6'
          }
        },
        splitLine: {
          lineStyle: {
            color: '#3c4451'
          }
        }
    },
    series: [
        {
          symbolSize: 7,
          smooth: true,
          symbol: 'circle',
          lineStyle: {
            normal: {
              width: 3
            }
          },
          name:'报关模块',
          type:'line',
          stack: '总量',
          data:[120, 132, 101, 134, 90, 230, 210],
          itemStyle : {
            normal : {
              color: '#01c0c8',
              lineStyle:{
                color:'#01c0c8'
              }
            }
          }
        },
        {
          symbolSize: 7,
          smooth: true,
          symbol: 'circle',
          lineStyle: {
            normal: {
              width: 3
            }
          },
          name:'亿加付费模块',
          type:'line',
          stack: '总量',
          data:[220, 182, 191, 234, 290, 330, 310],
          itemStyle : {
            normal : {
              color: '#01c293',
              lineStyle:{
                color:'#01c293'
              }
            }
          }
        },
        {
          symbolSize: 7,
          smooth: true,
          symbol: 'circle',
          lineStyle: {
            normal: {
              width: 3
            }
          },
          name:'核放单模块',
          type:'line',
          stack: '总量',
          data:[150, 232, 201, 154, 190, 330, 410],
          itemStyle : {
            normal : {
              color: '#9775cc',
              lineStyle:{
                color:'#9775cc'
              }
            }
          }
        },
        {
          symbolSize: 7,
          smooth: true,
          symbol: 'circle',
          lineStyle: {
            normal: {
              width: 3
            }
          },
          name:'应用总数',
          type:'line',
          stack: '总量',
          data:[820, 932, 901, 934, 1290, 1330, 1320],
          itemStyle : {
            normal : {
              color: '#fb9678',
              lineStyle:{
                color:'#fb9678'
              }
            }
          }
        }
    ]
  };
  export default {
    name: 'home',

    components: { weather, dashboard },

    created () {
      let vm = this
      let obj = {
        Cols: "WINXP_TP",
        Div: "OLS",
        Tags: "",
        ShowTime: true
      }
      //数据操作方法测试
      jsonp(0, (data) => {
        vm.news = data.cmsinfosjsobject
      }, obj)
    },
    computed: {
      themeName () {
        return this.$store.getters.getTheme
      }
    },
    mounted () {
      //模拟数据
      setTimeout(() => {
        this.dashboard1 = {
          icon: 'android-compass',
          describe: '日均PV',
          value: 23402
        }
        this.dashboard2 = {
          icon: 'android-contacts',
          describe: '来访人数',
          value: 5685
        }
        this.dashboard3 = {
          icon: 'calendar',
          describe: '当前登陆人数',
          value: 763
        }
        this.dashboard4 = {
          icon: 'ios-paperplane',
          describe: '待办事项',
          value: 62
        }
        this.showNum = true
      }, 2000)
      this.charts = echarts.init(document.querySelector('#dashboard-link'))
      let chartOp = this.themeName === 'black' ? optionBlack : optionLight
      this.charts.setOption(chartOp)

    },
    data () {
      return {
        dashboard1: {},
        dashboard2: {},
        dashboard3: {},
        dashboard4: {},
        charts: null,
        news: [],
        newsModel: false,
        // 新闻动态
        newsDetail: {
          title: '新闻详细',
          content: '',
          attachs: []
        },
        showNum: false
      }
    },

    methods: {
      handleNewsClick (id) {
        jsonp(2, (data) => {
          this.newsDetail = data
          this.newsModel = true
        }, {
          Id: id
        })
      }
    }
  }
</script>

<style>
  @media only screen and (min-width: 768px) and (max-width: 1200px) {
    .dashboard-block {
      width: 50%
    }
    .dashboard-md {
      width: 100%
    }
    .dashboard-md-left, .dashboard-md-right {
      box-sizing: border-box;
      float: left;
      width: 50%
    }
    .dashboard-md-left {
      padding-right: 10px
    }
    .dashboard-md-right {
      padding-left: 10px
    }    
  }
</style>