/**
 * 打包配置常量
 * 开头为命令配置项
 * @author: Merjiezo
 * @since: 2018-09-29
 * @version: 1.0.0
 */
const BUILD_ENV = process.env.NODE_ENV || 'development'
function getEpEntryDir () {
  let epEntryDir = process.env.npm_config_EP_ENTRY_DIR
  if (!epEntryDir || epEntryDir === '$npm_config_EP_ENTRY_DIR') {
    epEntryDir = 'src'
  }
  return epEntryDir
}

module.exports = {

  // EP APP编译 [开发(development)、测试(test)、生产(production)标志位]
  BUILD_ENV,

  // 获取应用入口，默认为src，支持多项目一个依赖
  ENTRY_DIR: getEpEntryDir(),

  // 获取应用编译路径，默认为 [build/ui]
  OUTPUT_DIR: 'build/' + ( getEpEntryDir() === 'src' ? 'ui': getEpEntryDir()  ),

}
