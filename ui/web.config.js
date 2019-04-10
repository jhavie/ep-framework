/**
 * Easipass Vue Application Config
 * Copyright (C) 2018 easipass.com. All Rights Reserved
 * @version vue-cli:^3.0.4 cautiously use in other BIG Version of vue-cli
 * 
 * 开发，测试，生产不同配置专用配置文件
 * DIR为工程代码的路径
 * src节点为默认，请勿删除此节点，如不作任何多应用配置的变化，框架自动采用此配置
 * 可以进行配置项的动态添加，并且在开发层面使用
 * ⚠️注意：此参数只限于DIR下面的config.js存在并且挂在到全局使用，其他地方完全禁止调用！！！！
 * 
 * @param {Object} [DIR] 不同路径节点
 * 
 * @param {String} [DIR.baseUrl] 应用生产的前端路由，请不同应用做不同的修改
 * 例子： 如imgd的配置则为: '/imgd/'，外部访问则为http://www.singlewindow.sh.cn/imgd
 * @param {Object} [DIR.development] 开发环境配置
 * @param {Object} [DIR.test] 测试环境配置
 * @param {Object} [DIR.production] 生产环境配置
 * 
 * @param {String} [DIR.ENV.serverUrl] 后端路径配置
 * @param {String} [DIR.ENV.md5Prefix] 请求的验签盐
 * @param {String} [DIR.ENV.notHideMenu] 最好不做对应修改
 * @param {String} [DIR.ENV.other] 此节点为其他参数展示，请动态增减
 */
module.exports = {
  src: {
    baseUrl: '/framework/',
    development: {
      serverUrl: '"localhost:9091"',
      md5Prefix: '"http://edi.easipass.com"',
      notHideMenu: '[]'
    },
    test: {
      serverUrl: '"192.168.12.66:9091"',
      md5Prefix: '"http://edi.easipass.com"',
      notHideMenu: '[ "test1", "test2" ]'
    },
    production: {
      serverUrl: '"192.168.12.66:9091"',
      md5Prefix: '"http://edi.easipass.com"',
      notHideMenu: '[]'
    }
  },

  // 不同应用请自行添加，此处为框架预留例子，规范为不同文件夹为不同对象
  app: {
    // ...
  }
}
