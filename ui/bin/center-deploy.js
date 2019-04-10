/**
 * Vue 应用集成式打包部署
 * @author: Merjiezo
 * @since: 2019-02-18
 * @version: 1.0.0
 */
const path = require('path')
const fs = require('fs')
const fse = require('fs-extra')

const buildFilePath = path.join(__dirname, '../build')
const BUILD_UI_PATH = path.join(buildFilePath, 'ui')
const ENV_LIST = {
  development: 'dev',
  test: 'test',
  production: 'prod'
}

copyFilesThenTar('development')
copyFilesThenTar('test')
copyFilesThenTar('production')

function copyFilesThenTar (env) {
  let targetDir = path.join(buildFilePath, ENV_LIST[env])
  fse.copySync(BUILD_UI_PATH, targetDir)
  fs.copyFileSync(path.join(targetDir, `web.config.${env}.js`), path.join(targetDir, 'web.config.js'))
  fs.unlinkSync(path.join(targetDir, `web.config.development.js`))
  fs.unlinkSync(path.join(targetDir, `web.config.test.js`))
  fs.unlinkSync(path.join(targetDir, `web.config.production.js`))
}
