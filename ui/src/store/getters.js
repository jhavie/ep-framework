const getters = {
  getChangeWidth: state => state.app.changeWidth,
  getTheme: state => state.app.theme,
  getDark: state => state.app.dark,
  getNowPath: state => state.app.nowPath,
  getRouterJson: state => state.app.routerJson,
  getShowMenu: state => state.app.showMenu,
  getNoPermissionPage: state => state.app.noPermissionPage,
  getOrgName: state => state.user.orgName,
  getOrgCode: state => state.user.orgCode,
  getLoginId: state => state.user.loginId,
  getUsername: state => state.user.username,
  getUserTele: state => state.user.userTele,
  getDraccNo: state => state.user.draccNo,
  getRoles: state => state.user.roles,
  getToken: state => state.user.token
}

export default getters
