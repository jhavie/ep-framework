// 配置文件打包
const DEF = require('./def')
const utils = require('./utils')

let config = {

  entry: {
    config: [ `./${DEF.ENTRY_DIR}/config.js` ]
  },

  resolve: {
    alias: {
      'utils': utils.resolve(`${DEF.ENTRY_DIR}/utils`)
    }
  },

  output: {
    filename: `web.[name].${DEF.BUILD_ENV}.js`,
    path: utils.resolve(DEF.OUTPUT_DIR)
  },

  plugins: [
    utils.defPlugin()
  ]
}

module.exports = config
