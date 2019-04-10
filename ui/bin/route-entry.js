var path = require('path');
var fs = require('fs');
var render = require('json-templater/string');
const utils = require('../config/utils');
const ENTRY_DIR = utils.getEpEntryDir();
var Components = require(path.join(__dirname, '../', ENTRY_DIR, 'routerJson'));

var OUTPUT_PATH = path.join(__dirname, '../', ENTRY_DIR, '/router/index.js');
/* BIZ 装配 */
var IMPORT_BIZ_TEMPLATE = 'const {{name}} = resolve => require([\'@/biz/{{file}}\'], resolve)'
/* SYS 装配 */
var IMPORT_SYS_TEMPLATE = 'const {{name}} = resolve => require([\'@/sys/{{file}}\'], resolve)'

/* 路由装配 */
var IMPORT_PATH = '{ path: \'{{path}}\', component: {{component}}, name: \'{{name}}\', meta: {{mata}} }'
const MAIN_TEMPLATE = `import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

import Main from '../framework/Home'
import Login from '../framework/Login'

import Err404 from '../framework/Error'
import Err401 from '../framework/401'

{{sys}}

{{biz}}

const routes = [
  { path: '/login', component: Login, name: '登录' },
  {
    path: '/',
    component: Main,
    redirect: '/home',
    name: 'ep',
    children: [
      {{router}}
    ]
  },
  { path: '/401', name: '401 No Permission!', component: Err401 },
  { path: '/*', name: '404 not found!', component: Err404 }
]

export default new Router({
  routes: routes
})
`

var JSON_SYS = Components.sys
var JSON_BIZ = Components.biz
var SysTemplate = []
var BizTemplate = []
var routerTemplate = []

function delSprit (name) {
  let res = name
  let num = name.indexOf('/')
  if (num !== -1) {
    res = name.slice(0, num)
  }
  return res
}

for (var item in JSON_SYS) {
  let name = item.slice(1)
  let nameT = delSprit(name)
  SysTemplate.push(render(IMPORT_SYS_TEMPLATE, {
    name: nameT,
    file: JSON_SYS[item].router
  }))
  routerTemplate.push(render(IMPORT_PATH, {
    path: name,
    component: nameT,
    name: JSON_SYS[item].name,
    mata: JSON_SYS[item].meta ? JSON.stringify(JSON_SYS[item].meta): '{}'
  }))
}

for (var itemBiz in JSON_BIZ) {
  let nameBiz = itemBiz.slice(1)
  let nameBizT = delSprit(nameBiz)
  BizTemplate.push(render(IMPORT_BIZ_TEMPLATE, {
    name: nameBizT,
    file: JSON_BIZ[itemBiz].router
  }))
  routerTemplate.push(render(IMPORT_PATH, {
    path: nameBiz,
    component: nameBizT,
    name: JSON_BIZ[itemBiz].name,
    mata: JSON_BIZ[itemBiz].meta ? JSON.stringify(JSON_BIZ[itemBiz].meta): '{}'
  }))
}


var template = render(MAIN_TEMPLATE, {
  sys: SysTemplate.join('\n'),
  biz: BizTemplate.join('\n'),
  router: routerTemplate.join(',\n      ')
});

fs.writeFileSync(OUTPUT_PATH, template);
console.log('[build router entry] DONE:', OUTPUT_PATH);
