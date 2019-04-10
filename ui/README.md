# Ep Framework 前端框架
An admin template based on [vuejs2](http://vuejs.org/) and [EP-UI](http://192.168.129.181:81/#/).

### Usage

路由配置：router.json
{
  "icon": "图标",
  "name": "路由名（和菜单对应）",
  "router": "对应的哪个文件"
}
使用警告，非常重要！！！！！！！！加入新的路由后需要重启Dev Server！
重要！！！！后台交互接口统一配置在同一个文件（api.json）内部，方便管理

post、get和jsonp三个方法，下面介绍api.json的配置方法
{
  "dataType": "数据格式（json或者form）",
  "url": "对应的路由",
  "cors": false, //是否跨域
  "oauth": false //带不带Oauth验证
}
因此理论上大部分的请求只需配置url既可

业务界面统一书写在src/views/biz或者src/views/sys下
框架建议使用EP-UI作为首选组件库，用Element做辅助
测试用例不做上传

assets      资源集合
ep-ui       easipass组件库
framework   框架文件
lang        国际化
router      路由，启动和打包时自动生成
store       全局状态基
template    业务组件
utils       工具方法在此
views       业务层在此

``` bash
# install dependencies
npm install / cnpm install

# serve with hot reload at localhost:1234
npm run dev

# build for production with minification
npm run build:dev
npm run build
npm run build:prod
```

### Browser Support

Modern browsers and Internet Explorer 9+.
由于浏览器内核的关系，IE 9不支持动效

### 注，重要！！！！

为了达到和iframe一样的缓存效果，请务必填写业务层的name，并且此name和router.json内的name保持一致！

### URL切换

--EP_ENTRY_DIR=app
