<template>
  <div style="height: 100%;" @keyup.enter="handleLogin">
    <div class="login-pic">
      <img src="../assets/frame/login.jpg" height="100%"/>
      <div class="login-pic-block">
        <div class="login-pic-word">
          <h1>EP - Framework</h1>
          <p>全新的页面，扁平化的设计</p >
          <p>直观的数据展示，积聚您航运的智慧</p>
        </div>
      </div>
    </div>
    <div class="login-right">
      <!-- <div class="login-triangle"></div>-->
      <div class="card card-shadow login-block">
        <div class="login-title">
          <p>登陆</p >
        </div>
        <div class="login-main">
          <div class="login-main-block">
            <ep-form ref="form" :form="form" :rules="rules"  name-width="80px" name-position="top">
              <ep-form-item label="用户名" attr="id">
                <ep-input v-focus v-model="form.id" :minlength="6" :maxlength="20"></ep-input>
              </ep-form-item>
              <br/>
              <ep-form-item label="密码" attr="password">
                <ep-input v-model="form.password" type="password" :minlength="6" :maxlength="20"></ep-input>
              </ep-form-item>
              <ep-form-item label="验证码" attr="captchaVal">
                 <ep-row>
                  <ep-col :col="12">
                    <ep-input style="width: 100px;" v-model="form.captchaVal" :maxlength="4"></ep-input>
                  </ep-col>
                  <ep-col :col="12">
                    <img align="yzm" class="login-yzm"
                      v-if="form.captchaUUid" @click="loadCaptcha"
                      :src="host + '/pcrimg/' + form.captchaUUid"/>
                  </ep-col>
                </ep-row>
              </ep-form-item>
            </ep-form>
            <div style="padding-top: 14px; text-align: right">
              <ep-button type="primary" :loading="isLoading" class="login-btn" @click="handleLogin">登陆</ep-button>
            </div>
          </div>
          <div class="login-copyright">
            <p>Copyright (C) 2001-2017 easipass.com. All Rights Reserved</p>
          </div>
        </div>
      </div>
    </div>
    
  </div>
</template>
<script>
import { login } from 'utils'

export default {

  name: "Login",

  created () {
    this.loadCaptcha()
  },

  data () {
    return {
      host: global.HOST,
      isLoading: false,
      form: {
        id: '',
        password: '',
        captchaVal: '',
        captchaUUid: ''
      },
      rules: {
        id: { min: 3, max: 20, required: true, message: '用户名必须为3-20个字符', trigger: 'blur' },
        password: { min: 6, max: 20, required: true, message: '密码必须为6-20个字符', trigger: 'blur' },
        captchaVal: { max: 4, required: true, message: '验证码不能为空' }
      }
    }
  },

  methods: {
    loadCaptcha () {
      this.$get('captchaUuid').then(json => {
        this.form.captchaUUid = json.data.captchaUuid
      }).catch(e => { })
    },
    handleLogin () {
      login(this, () => {
        this.$router.push('home')
      })
    }
  },

  beforeDestroy () {
    
  }

}
</script>

<style scoped>
  .login-pic {
    box-sizing: border-box;
    box-sizing: border-box;
    position: relative;
    width: 55%;
    height: 100%;
    overflow: hidden;
    float: left;
    background: #44798F;
  }
  .login-pic > img {
    position: relative;
    left: -20%;
  }
  .login-pic-block {
    position: absolute;
    top: 0px;
    left: 0px;
    bottom: 0px;
    right: 0px;
    padding: 48px;
    background: rgba(33, 150, 243, .12);
    color: #DDD;
    line-height: 1.5;
  }
  .login-pic-block h1 {
    margin: 0 0 20px 0;
  }
  .login-pic-block  p {
    margin: 0 0 7px 0;
  }
  .login-pic-word {
    position: relative;
    width: 100%;
    height: 100%;
    border: 7px solid #EEE;
    padding: 24px;
  }
  .login-right {
    box-sizing: border-box;
    position: absolute;
    z-index: 10;
    right: 0px;
    width: 45%;
    height: 100%;
    background: #67a0f0;
    box-shadow: -2px 2px 4px 0 rgba(0, 0, 0, 0.22);
  }
  .login-triangle {
    position: relative;
    left: 0%;
    top: -60%;
    width: 150%;
    height: 200%;
    background: #0174FF;
    box-shadow: -2px 2px 4px 0 rgba(0, 0, 0, 0.14);
    -webkit-transform: rotate(5deg);
    -moz-transform: rotate(5deg);
    -ms-transform: rotate(5deg);
    -o-transform: rotate(5deg);
    transform: rotate(5deg);
    -webkit-transform-origin: 0 100%;
    -moz-transform-origin: 0 100%;
    -ms-transform-origin: 0 100%;
    -o-transform-origin: 0 100%;
    transform-origin: 0 100%;
  }
  .login-back>img {
    position: relative;
    top: -49%;
    width: 100%;
  }
  .login-block {
    z-index: 10;
    position: absolute;
    top: 50%;
    left: -30px;
    right: 0;
    margin: auto;
    width: 380px;
    border-radius: 2px;
    overflow: visible;
    -webkit-transform: translateY(-50%);
    transform: translateY(-50%);
    background: #0174FF;
  }
  .login-title {
    height: 80px;
    overflow: hidden;
    text-align: left;
    color: #FFF;
  }
  .login-title>p {
    margin: 0 auto;
    padding-top: 32px;
    width: 320px;
    font-size: 18px;
    line-height: 30px;
  }
  .login-main label {
    font-size: 12px;
  }
  .login-main {
    margin: 0 auto;
    padding-bottom: 30px;
    width: 320px;
  }
  .login-main-block {
    padding: 24px;
    background: #FFF;
    width: 380px;
    box-shadow: 0 2px 2px 0 rgba(0, 0, 0, 0.14), 0 3px 1px -2px rgba(0, 0, 0, 0.2), 0 1px 5px 0 rgba(0, 0, 0, 0.12);
  }
  .login-yzm {
    cursor: pointer;
    display: inline-block;
    margin: 0 0 10px 14px;
    height: 32px;
    vertical-align: middle;
  }
  .login-btn {
    width: 100%;
  }
  .login-copyright {
    padding-top: 14px;
    text-align: center;
    font-size: 14px;
    color: #FFF;
  }
  .login-copyright>p {
    font-size: 12px;
    margin: 0
  }
  .fade-down-enter-active, .fade-down-leave-active, .fade-up-enter-active, .fade-up-leave-active {
    transition: all .5s .5s cubic-bezier(.23,1,.32,1)
  }
  .fade-down-enter, .fade-down-leave-active {
    opacity: 0;
    -webkit-transform: translateY(-100%);
    transform: translateY(-100%)
  }
  .fade-up-enter, .fade-up-leave-active {
    opacity: 0;
    top: 100%
  }
</style>
