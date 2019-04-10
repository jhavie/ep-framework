/**
 * Vue 应用集成式清理脚本
 * @author: Merjiezo
 * @since: 2019-02-18
 * @version: 1.0.0
 */
const path = require('path')
const fs = require('fs')
const fse = require('fs-extra')

const buildFilePath = path.join(__dirname, '../build')

fse.removeSync(path.join(buildFilePath, 'dev'))
fse.removeSync(path.join(buildFilePath, 'test'))
fse.removeSync(path.join(buildFilePath, 'prod'))
fse.removeSync(path.join(buildFilePath, 'ui'))
