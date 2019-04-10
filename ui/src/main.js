/**
 * 主框架从此开始
 * @author: Merjiezo
 * @since: 2017-05-20
 */
import "ionicons-npm/css/ionicons.css"  // 图标CSS资源
import "ep-ui/theme/lib/epui.min.css"   // ep-ui样式
// import "ep-ui/theme/lib/epui-black.min.css"   // ep-ui 黑色样式 如果报错 npm update ep-ui
// import "./assets/css/epui-black.min.css"   // ep-ui 黑色样式 （还没发布 临时）
if (process.env.NODE_ENV === 'development') {
  require('./config')                   // 全局配置
}
import 'src/base/string'                // String操作方法
/***
 * 应用初始化
 * 初始化路由、状态基、权限系统等
 */
import { initApp } from './base/init'
initApp()
