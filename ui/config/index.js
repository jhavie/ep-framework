/**
 * Vue 应用配置
 * 此处暴露的EPApp，方便进行vue.config的配置删除
 * @author: Merjiezo
 * @since: 2018-09-28
 * @version: 1.0.0
 */
const merge = require('webpack-merge')
const DEF = require('./def')
const webpackConfig = require('./webpack.config')
const webConf = require('../web.config')

const baseConfig = {
  // Easipass 应用配置在此
  EPApp: {
    // 全局入口
    entry: [ `./${DEF.ENTRY_DIR}/main.js` ]
  },
  // Same as Vue-cli 2.0 config/index.js param [assetsPublicPath] 
  baseUrl: '/',
  // Webpack output Dir
  outputDir: DEF.OUTPUT_DIR,
  // [js, css, font]静态文件目录
  assetsDir: 'static',
  // No EsLint !
  lintOnSave: false,
  // Webpack Config
  configureWebpack: webpackConfig,

  // transpileDependencies: [ 'ep-ui' ]
  // transpileDependencies: [
  //   /node_modules\/ep-ui/,
  // ]
}

// backUrl 为后端地址，不用应用为不同配置
module.exports = {

  development: merge(baseConfig, {
    EPApp: {
      webConf: webConf[DEF.ENTRY_DIR].development
    },
    // Webpack Dev Server Config
    devServer: {
      host: '0.0.0.0',
      port: 3000,
      // 前端开发环境的反向代理
      proxy: {
        // IMGD Proxy Example
        '/imgd': {
          target: 'http://192.168.129.181',
          changeOrigin: true,
          pathRewrite: { '^/imgd': '/imgd' }
        }
      }
    }
  }),

  test: merge(baseConfig, {
    EPApp: {
      webConf: webConf[DEF.ENTRY_DIR].test
    }
  }),

  production: merge(baseConfig, {
    EPApp: {
      webConf: webConf[DEF.ENTRY_DIR].production
    },
    baseUrl: webConf[DEF.ENTRY_DIR].baseUrl,

    // 生产去除source map配置
    productionSourceMap: false
  })
}
