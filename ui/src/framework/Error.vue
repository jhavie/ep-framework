<template>
  <div class="main-col panel-error">
    <div class=" panel-error-pic">
      <img :src="imgs[theme]" alt="error" width="100%">
    </div>
    <div class="panel-error-word">
      <h1 class="panel-error-h1" :class="'panel-' + theme + '-text'">404 - Not Found</h1>
      <p class="panel-error-copyright">版权所有：
        <a href="http://www.easipass.com/cn/index.html" :class="'panel-' + theme + '-text'" target="_blank">上海亿通国际股份有限公司</a>
      </p>
      <p :class="'panel-' + theme + '-text'">此路径 <strong>{{$route.path}}</strong> 上什么都没有...</p>
      <p class="panel-error-more">请检查您输入的网址是否正确，请点击以下按钮返回主页或者发送错误报告</p>
      <!-- <p class="panel-error-more">图版权所有：Sarah Drasner</p> -->
      <router-link class="panel-error-back" :class="'panel-' + theme" to="/">首页</router-link>
    </div>
  </div>
</template>

<script>
  import dark from 'assets/frame/404-dark.png'
  import black from 'assets/frame/404-black.png'
  import blue from 'assets/frame/404-blue.png'
  import darkBlue from  'assets/frame/404-dark-blue.png'
  import green from  'assets/frame/404-green.png'
  import pupple from  'assets/frame/404-pupple.png'
  import orange from  'assets/frame/404-orange.png'

  export default {
    
    name: 'not-found',

    computed: {
      theme () {
        return this.$store.getters.getTheme
      }
    },

    data () {
      let hasSvg = typeof SVGRect != "undefined"
      return {
        hasSvg: hasSvg,
        imgs: {
          dark: dark,
          black: black,
          blue: blue,
          'dark-blue': darkBlue,
          green: green,
          pupple: pupple,
          orange: orange
        }
      }
    }
  }
</script>
