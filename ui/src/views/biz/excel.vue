<template>
  <div class="panel-main-content">
    <div class="contents-card card-margin main-col">
      <h3 style="margin: 0 0 20px 0; color: #123">测试用户权限</h3>
      <p style="margin: 0 0 10px 0; color: #123">
        EXCEL页面为测试权限页面，应跳转到401页面，页面权限全部通过oauth的rt进行动态获取，并且保存在内部只读参数内部，大大提高了安全性
      </p>
      <p style="margin: 0 0 10px 0; color: #123">
        框架的安全级别分为未登录白名单，登陆白名单，以及权限匹配页面三层，基本满足日常开发所需
      </p>
      <p style="margin: 0 0 10px 0; color: #123">
        框架使用人员需要至少具有一个需要登陆匹配的页面，否则默认会不获取权限，也失去了动态WEB的意义
      </p>
      <p style="margin: 0 0 10px 0; color: #123">
        框架在路由层获取菜单信息
      </p>
      <p style="margin: 0 0 10px 0; color: #123">
        如有任何问题，请及时联系开发商解决
      </p>
      <router-link to="/excel">跳转到测试无权限页面/excel</router-link>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'excel',


  }
</script>