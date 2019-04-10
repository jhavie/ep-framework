<template>
  <div class="tab-main">
    <div class="tab-icon tab-icon-left" :class="{ disabled: scrollLeft >= 0 }" @click="upOrDown(0)">
      <ep-icon icon="chevron-left" theme="gray"></ep-icon>
    </div>
    <div class="tab-main-block">
      <div class="tab-scroll" :class="'tab-' + theme" :style="{ left: scrollLeft + 'px' }">
        <template v-for="(tab, index) in tabs">
          <span :class="{ 'active': tab.id === value }" :data-index="tab.id"
            :key="index" @click="itemClick($event, index, tab)">
            <div>
              <span class="tab-icon-dot"></span>
              {{ tab.name }}
              <template v-if="tab.mutiId"> - {{ tab.mutiId }}</template>
              <i v-if="tab.canClose" class="tab-close ion-close-round"></i>
            </div>
          </span>
        </template>
      </div>
    </div>
    <div class="tab-icon tab-icon-right"
      :class="{ disabled: parentWidth - scrollLeft >= scrollWidth }" @click="upOrDown(1)">
      <ep-icon icon="chevron-right" theme="gray"></ep-icon>
    </div>
  </div>
</template>

<script>
  import { on, off } from 'utils'
  import throttle from 'throttle-debounce/throttle'
  

  // 多tab移动距离的计算
  //width为内部滚动元素长度，div为父元素长度
  function toggleRemoveWhenTooManyTab(left, width, div, toggle) {
    let widthIsRolled = Math.abs(left)
    let nowHaveWidthNeedRoll = width - widthIsRolled

    if (toggle === 1) {
      //内部宽度如果小于显示宽度的两倍，移动到底，否则移动一个显示宽度的内容
      if (nowHaveWidthNeedRoll < div * 2) {
        return nowHaveWidthNeedRoll - div
      }
    } else {
      //如果比显示宽度小，移动到最前面，否则移动一个显示宽度
      if (widthIsRolled < div) {
        return widthIsRolled
      }
    }
    return div
  }
  
  export default {
    name: 'ep-tab',

    props: {
      theme: {
        type: String,
        default: 'blue'
      },
      noClose: Boolean,
      value: {
        type: Number,
        default: 0
      },
      tabs: Array
    },

    data () {
      return {
        parentEl: null,
        scrollEl: null,

        parentWidth: 0,
        scrollWidth: 0,

        scrollLeft: 0
      }
    },
    mounted () {
      let vm = this
      if (this.tabs.length < 1) {
        throw new Error('Template Must have one tab!')
      }
      this.initEl()
      this.changeSize = throttle (300, () => {
        vm.reloadEl()
      })
      on (window, 'resize', this.changeSize)
    },

    methods: {
      //样式改进
      addProp () {
        this.$nextTick(() => {
          this.reloadEl()
          let left = this.parentWidth - this.scrollWidth;
          if (left < 0) {
            this.scrollLeft = left
          }
        })
      },
      upOrDown (toggle) {
        const div = this.parentWidth
        const width = this.scrollWidth
        const left = this.scrollLeft

        if (left < 0 && toggle === 0) {
          this.scrollLeft += toggleRemoveWhenTooManyTab(left, width, div, toggle)
        }

        const min = width - div
        if (Math.abs(left) < min && toggle === 1) {
          this.scrollLeft -= toggleRemoveWhenTooManyTab(left, width, div, toggle)
        }
      },

      itemClick (evt, $index, tab) {
        const className = evt.target.className
        // 关闭 / 改变Tabs
        className.indexOf('tab-close') !== -1
          ? this.handleClose(evt, $index, tab)
          : this.handleChange($index, tab)
      },
      //关闭tab
      handleClose (evt, $index, tab) {
        const width = evt.currentTarget.clientWidth
        const left = Math.abs(this.scrollLeft)
        if (left > 0 && left >= width) {
          this.scrollLeft += width
        } else {
          if (this.scrollLeft !== 0) {
            this.scrollLeft = 0
          }
        }

        // At Current Tab
        if (tab.id === this.value) {
          if (this.tabs.length > 0
            && $index >= 1) {
            const nowIndex = $index - 1
            this.$emit('change', nowIndex, this.tabs[nowIndex])
          }
        }
        this.$emit('close', $index, this.tabs[$index])
        this.$nextTick(() => {
          this.reloadEl()
        })
      },
      handleChange ($index, tab) {
        if (this.value !== tab.id) {
          this.$emit('change', $index, tab)
        }
      },
      initEl () {
        this.parentEl = this.$el.querySelector('.tab-main-block')
        this.scrollEl = this.$el.querySelector('.tab-scroll')

        this.parentWidth = this.parentEl.clientWidth
        this.scrollWidth = this.scrollEl.clientWidth
      },
      reloadEl () {
        this.parentWidth = this.parentEl.clientWidth
        this.scrollWidth = this.scrollEl.clientWidth
      }
    },

    destroyed () {
      off (window, 'resize', this.changeSize)
    }
  }
</script>

<style>
  .tab-main-block * {
    -webkit-user-select: none;
    user-select: none
  }
  .tab-main, .tab-icon, .tab-main-block, 
  .tab-scroll{
    height: 40px;
    line-height: 40px
  }
  .tab-scroll > span, .tab-scroll > span > div {
    line-height: 30px    
  }
  .tab-main {
    box-shadow: 0px 0px 4px rgba(0, 0, 0, .12) inset;
    position: relative;
    margin-right: 80px;
    border-bottom: 1px solid #AAA;
    width: auto;
    font-size: 12px;
    background-color: #f0f0f0
  }
  .tab-icon {
    cursor: pointer;
    width: 40px;
    position: absolute;
    font-size: 14px;
    line-height: 40px;
    text-align: center;
  }
  .tab-icon:active {
    opacity: .7
  }
  .tab-icon-left {
    left: 0px;
  }
  .tab-icon-right {
    right: 0px;
  }
  .tab-icon.disabled {
    pointer-events: none;
    color: #AAA;
  }
  .tab-main-block {
    position: absolute;
    left: 40px;
    right: 40px;
    width: auto;
    overflow: hidden
  }
  .tab-scroll {
    position: absolute;
    overflow: hidden;
    white-space: nowrap;
    -webkit-transition: left .5s cubic-bezier(.23,1,.32,1);
    transition: left .5s cubic-bezier(.23,1,.32,1)
  }
  .tab-icon-dot {
    display: inline-block;
    vertical-align: middle;
    margin-right: 8px;
    border-radius: 50%;
    height: 12px;
    width: 12px;
    background: #e9eaec;
  }
  .tab-scroll > span, .tab-scroll > span > div, .tab-close {
    -webkit-transition: all .1s;
    transition: all .1s
  }
  .tab-scroll > span {
    box-sizing: border-box;
    cursor: pointer;
    display: inline-block;
    padding: 0 8px;
    margin: 2px 4px 2px 0;
    border: 1px solid #DDD;
    border-radius: 2px;
    height: 32px;
    min-width: 80px;
    text-align: center;
    vertical-align: middle;
    -webkit-transform: translateY(-1px);
    transform: translateY(-1px);
    background: #FFF;
  }
  .tab-scroll > span:hover {
    background: #FAFAFA;
  }
  .tab-scroll > span i {
    padding: 3px;
  }
  .tab-close {
    vertical-align: baseline;
    font-size: 12px;
    margin-left: 2px
  }
  .tab-scroll > .active .tab-close {
    font-size: 12px;
  }
  .tab-close:hover {
    color: #999
  }
  .tab-close:active {
    opacity: .7
  }
  .tab-span-icon {
    vertical-align: baseline;
    margin-right: 7px
  }
  .tab-dark > .active .tab-icon-dot {
    background: #001529;
  }
  .tab-blue > .active .tab-icon-dot {
    background: #2196f3;
  }
  .tab-dark-blue > .active .tab-icon-dot {
    background: #3498db;
  }
  .tab-green > .active .tab-icon-dot {
    background: #16a085;
  }
  .tab-pupple > .active .tab-icon-dot {
    background: #3F51B5;
  }
  .tab-orange > .active .tab-icon-dot {
    background: #f39c12;
  }
  .tab-pink > .active .tab-icon-dot {
    background: #f7cadb;
  }
  .tab-dark > .active {
    color: #000c17;
  }
  .tab-blue > .active {
    color: #2196f3;
  }
  .tab-dark-blue > .active {
    color: #3498db;
  }
  .tab-green > .active {
    color: #16a085;
  }
  .tab-pupple > .active {
    color: #3F51B5;
  }
  .tab-orange > .active {
    color: #f39c12;
  }
  .tab-pink > .active {
    color: #f7cadb;
  }
</style>