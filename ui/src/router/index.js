import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

import Main from '../framework/Home'
import Login from '../framework/Login'

import Err404 from '../framework/Error'
import Err401 from '../framework/401'

const home = resolve => require(['@/sys/home'], resolve)
const menu = resolve => require(['@/sys/menu'], resolve)
const commConfig = resolve => require(['@/sys/commConfig'], resolve)

const map = resolve => require(['@/biz/map'], resolve)
const widgets = resolve => require(['@/biz/widgets'], resolve)
const excel = resolve => require(['@/biz/excel'], resolve)
const permission = resolve => require(['@/biz/permission'], resolve)

const routes = [
  { path: '/login', component: Login, name: '登录' },
  {
    path: '/',
    component: Main,
    redirect: '/home',
    name: 'ep',
    children: [
      { path: 'home', component: home, name: '首页', meta: {} },
      { path: 'menu', component: menu, name: '菜单管理', meta: {} },
      { path: 'commConfig', component: commConfig, name: '通用表格', meta: {} },
      { path: 'map', component: map, name: '输入', meta: {"muti":true,"mutiName":"orderId"} },
      { path: 'widgets', component: widgets, name: 'Widgets', meta: {} },
      { path: 'excel', component: excel, name: '权限测试页', meta: {} },
      { path: 'permission', component: permission, name: '权限测试页', meta: {} }
    ]
  },
  { path: '/401', name: '401 No Permission!', component: Err401 },
  { path: '/*', name: '404 not found!', component: Err404 }
]

export default new Router({
  routes: routes
})
