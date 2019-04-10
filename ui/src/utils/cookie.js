// 自带解码编码
export default {
  
  setCookie (key, value, exdays) {
    let d = new Date()
    d.setTime(d.getTime() + (exdays * 60 * 24 * 60 * 1000))
    let expires = "expires=" + d.toGMTString()
    document.cookie = key + '=' + escape(value) + '; ' + expires
  },

  setProcessCookie(key, value) {
    document.cookie = key + '=' + escape(value) + '; expires=0'
  },

  getCookie (name, escape) {
    let cname = name + '='
    let ca = document.cookie.split(';')
    for (let i = 0; i < ca.length; i++) {
      let c = ca[i].trim()
      if (c.indexOf(cname) == 0) {
        let res = c.substring(cname.length, c.length)
        return escape === false? res: unescape(res)
      }
    }
    return ''
  },

  delCookie (name) {
    let exp = new Date()
    exp.setTime(exp.getTime() - 1)
    let val = this.getCookie(name, false)
    if (val != null)
      document.cookie = name + '=' + val + ';expires=' + exp.toGMTString()
  }

}
