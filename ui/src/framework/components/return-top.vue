<template>
  <div class="card-shadow panel-return-top" :class="'panel-' + theme"
    v-show="showToTop" @click.stop="handleClick">
    <i class="ion-arrow-up-b"></i>
  </div>
</template>

<script>
  import { on, off } from 'utils'
  let $el = null

  export default {
    name: 'returnTop',

    mounted () {
      $el = document.querySelector('.panel-main')
      
      on ($el, 'scroll', this.isShow)
    },

    props: {
      dom: {},
      theme: {
        type: String,
        default: 'blue'
      }
    },

    data () {
      return {
        mode: 'normal',
        showToTop: false
      }
    },

    methods: {
      isShow (evt) {
        if (evt.target.scrollTop > 20) {
          this.showToTop = true
        } else {
          this.showToTop = false
        }
      },

      handleClick () {
        if (this.mode !== 'scrolling') {
          this.mode = 'scrolling'
          this.toTop()
        }
      },

      toTop () {
        this.mode = 'normal'
        $el.scrollTop = 0
      }
    },

    destroyed () {
      off ($el, 'scroll', this.isShow)
    }
  }
</script>
