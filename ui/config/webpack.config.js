/**
 * Webpack Vue 增强配置
 * @author: Merjiezo
 * @since: 2018-09-28
 * @version: 1.0.0
 */
const HtmlWebpackPlugin = require('html-webpack-plugin')
const utils = require('./utils')
const DEF = require('./def')
const fecha = require('fecha')

module.exports = (config) => {

  const EPApp = utils.getEPApp()

  config.entry = {
    app: EPApp.entry
  }

  // webpack resolve.alias
  config.resolve.alias = {
    'vue$': 'vue/dist/vue.common.js',
    /**
     * 此处有可能会有歧义
     * 因此此处统一规范定义src为应用的工程目录的根目录
     * 可以代表src文件夹，也可以代表app等文件夹
     */
    'src': utils.resolve(DEF.ENTRY_DIR),
    'assets': utils.resolve(`${DEF.ENTRY_DIR}/assets`),
    'temp': utils.resolve(`${DEF.ENTRY_DIR}/template`),
    '@': utils.resolve(`${DEF.ENTRY_DIR}/views`),
    'utils': utils.resolve(`${DEF.ENTRY_DIR}/utils`),
    // 全球化配置
    'lang': utils.resolve('lang'),
    // 外部不安装依赖专用
    'lib': utils.resolve('lib'),
  }

  // webpack DefinePlugin
  let definePlugin = utils.defPlugin()
  config.plugins.push(definePlugin)

  /**
   * webpack HtmlWebpackPlugin
   * Learn more in: https://github.com/jantimon/html-webpack-plugin
   * 包装参数注入
   */
  config.plugins.forEach(item => {
    if (item instanceof HtmlWebpackPlugin) {
      item.options.EP_BUILD_DATE = fecha.format(new Date(), 'YYYYMMDDHHmmss')
      item.options.EP_BUILD_ENV = DEF.BUILD_ENV
    }
  })
}
