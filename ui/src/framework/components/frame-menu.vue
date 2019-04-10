<template>
  <div class="panel-menu-padding">
    <ep-icon icon="navicon-round" theme="light" @click="changeWidth"></ep-icon>
    <ep-dropdown ref="dropdown" class="menu-item-input" type="none" @item-click="menuSearchChange">
      <template slot="item">
        <input type="text" placeholder="搜索" v-model="menuSearchVal"
          @focus="menuSearchOnFocus" @blur="menuSearchClose" @keyup.enter="menuSearch"
          :style="{ width: widthInput + 'px' }">
        <button class="search-btn" @click="menuSearch">
          <i class="ion-search"></i>
        </button>
      </template>
      <ep-dropdown-item v-for="item in menuSearchArr"
        class="menu-item-detail"
        :key="item.code" :item-key="item.router">
        <span class="menu-item-detail-name">{{ item.name }}</span>
        <span class="menu-item-detail-code">{{ item.menuCode }}</span>
      </ep-dropdown-item>
      <div class="menu-item-none" v-if="menuSearchArr.length === 0">暂无菜单项</div>
    </ep-dropdown>
    <div class="ep-icon-group panel-menu-right">
      <span class="panel-menu-right-user">{{ $store.getters.getOrgName }}</span>
      <ep-icon icon="android-settings" theme="light" @click="resetPassword" tooltip-content="重置密码" v-if="$store.getters.getShowMenu"></ep-icon>
      <ep-icon icon="ios-trash" theme="light" @click="clearCache" tooltip-content="清空缓存" v-if="$store.getters.getShowMenu"></ep-icon>
      <ep-icon icon="power" theme="light" @click="handleLogout" tooltip-content="注销" v-if="$store.getters.getShowMenu"></ep-icon>
      <ep-icon v-show="$store.getters.getTheme !== 'black'" icon="tshirt" theme="light" @click="$emit('change-skin')" tooltip-content="换肤"></ep-icon>
    </div>
    <ep-model title="修改密码" v-model="resetPassModel" width="480px">
      <ep-form ref="resetPass" :form="resetForm"
        name-width="90px" :rules="resetPassRule">
        <ep-form-item attr="oldPass" label="旧密码">
          <ep-input v-model="resetForm.oldPass" type="password"></ep-input>
        </ep-form-item>
        <ep-form-item attr="password" label="新密码">
          <ep-input v-model="resetForm.password" type="password"></ep-input>
        </ep-form-item>
        <ep-form-item attr="confirmPass" label="确认密码">
          <ep-input v-model="resetForm.confirmPass" type="password"></ep-input>
        </ep-form-item>
      </ep-form>
      <div slot="footer">
        <ep-button type="text" @click="resetPassCancel">取消</ep-button>
        <ep-button type="primary" @click="resetPassConfirm">确定</ep-button>
      </div>
    </ep-model>
  </div>
</template>

<script>
  import { logout, getFillterMenuList } from 'utils'
  const maxInput = 420
  const originInput = 280

  export default {
    name: 'frame-menu',

    data () {
      return {
        menuSearchArr: [],
        menuSearchVal: '',
        resetPassModel: false,
        widthInput: originInput,
        resetForm: {
          oldPass: '',
          password: '',
          confirmPass: ''
        },
        resetPassRule: {
          oldPass: { required: true, min: 3, max: 50, message: '长度在3到50个字符', trigger: 'blur' },
          password: { required: true, min: 3, max: 50, message: '长度在3到50个字符', trigger: 'blur' },
          confirmPass: { required: true, validator: this.validatorPass, trigger: 'blur, change' },
        }
      }
    },

    watch: {
      menuSearchVal (val) {
        this.setDropdownVisible(true)
        this.menuSearchArr = val === '' ? [] : getFillterMenuList(val)
      },
      resetPassModel (val) {
        if (!val) {
          this.$refs.resetPass.clearError()
        }
      }
    },

    methods: {
      validatorPass (rule, value, callback) {
        if (value.length === 0) {
          callback('不能为空')
        } else if (value.length >= 3 && value.length <= 50) {
          if (value !== this.resetForm.password) {
            callback('密码与确认密码不匹配，请重新输入')
          } else {
            callback()
          }
        } else {
          callback('密码必须为3到50个字符')
        }
      },
      resetPassword () {
        this.resetPassModel = true
        this.resetModel()
      },
      resetModel () {
        this.resetForm = {
          oldPass: '',
          password: '',
          confirmPass: ''
        }
        this.$nextTick(() => {
          this.$refs['resetPass'] && this.$refs['resetPass'].reset()
        })
      },
      resetPassCancel () { this.resetPassModel = false },
      resetPassConfirm () {
        this.$refs['resetPass'].validate((valid) => {
          if (valid) {
            this.$post('resetPass', {
              oldPass: this.resetForm.oldPass,
              password: this.resetForm.password
            }).then(json => {
              this.resetPassModel = false
              this.$refs.resetPass.clearError()
            }).catch(e => {})
          }
        })
      },
      changeWidth () {
        const temp = this.$store.getters.getChangeWidth
        if (temp === 0) {
          this.$store.dispatch('setChangeWidth', 1)
        } else {
          this.$store.dispatch('setChangeWidth', 0)
        }
      },
      setDropdownVisible (bool) { this.$refs.dropdown.setVisible(bool) },
      menuSearchOnFocus () {
        this.widthInput = maxInput
        this.setDropdownVisible(true)
      },
      menuSearchClose () {
        this.widthInput = originInput
        this.setDropdownVisible(false)
      },
      menuSearch () {
        // 搜索自定义在此
        this.close()
      },
      menuSearchChange (item) {
        this.$router.push({ path: '/' + item })
      },
      clearCache () {
        // TODO Clear Cache
      },
      handleLogout () {
        this.$confirm({
          word: '是否注销？',
          callback (errorInfo) {
            if (errorInfo) {
              logout()
            }
          }
        })
      }
    }
  }
</script>

<style>
  .menu-item-input {
    display: inline-block;
    padding-left: 20px;
    padding-top: 2px;
    position: relative;
    line-height: 32px;
  }
  .menu-item-input input {
    will-change: width;
    box-sizing: border-box;
    outline: none;
    border: none;
    padding: 5px 30px 5px 15px;
    height: 32px;
    border-radius: 2px;
    background-color: rgba(255, 3255, 255, .33);
    -webkit-transition: width .5s;
    transition: width .5s;
    color: #FFF;
  }
  .menu-item-input input::-moz-placeholder {
    color: #FFF
  }
  .menu-item-input input::-webkit-input-placeholder {
    color: #FFF
  }
  .menu-item-input input:focus {
    box-shadow: inset 0 1px 2px rgba(0, 0, 0, .33)
  }
  .menu-item-detail {
    border-bottom: 1px dashed #AAA;
    padding: 12px 16px;
    min-width: 180px;
  }
  .menu-item-detail:last-of-type {
    border-bottom: 0;
  }
  .menu-item-detail-name {
    font-size: 14px;
  }
  .menu-item-detail-code {
    padding-left: 5px;
    font-size: 12px;
    color: #AAA;
    vertical-align: bottom;
  }
  .menu-item-none {
    padding: 12px 16px;
    width: 200px;
    text-align: center;
    color: #AAA;
  }
  .search-btn {
    cursor: pointer;
    outline: none;
    position: absolute;
    top: 50%;
    right: 2px;
    width: 30px;
    height: 28px;
    border: none;
    padding: 0;
    border-radius: 2px;
    color: #FFF;
    background-color: transparent;
    text-align: center;
    line-height: 26px;
    transform: translateY(-13px)
  }
  .search-btn > i {
    display: inline-block;
    width: 100%
  }
  .panel-menu-right > span {
    margin-right: 4px;
    font-size: 14px;
    color: rgba(250,250,250,.92);
  }
</style>