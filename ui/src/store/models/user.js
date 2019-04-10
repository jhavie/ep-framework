/**
 * UUMM的部分用户信息
 * 扩展属性请根据系统的不同做修改
 */
const user = {
  state: {
    orgName: '',
    orgCode: '',
    loginId: '',
    username: '',
    userTele: '',
    draccNo: '',
    roles: '',
    token: ''
  },

  mutations: {
    SETORGNAME (state, newOrgName) {
      state.orgName = newOrgName
    },
    SETORGCODE (state, newOrgCode) {
      state.orgCode = newOrgCode
    },
    SETLOGINID (state, newLoginId) {
      state.loginId = newLoginId
    },
    SETUSERNAME (state, newUsername) {
      state.username = newUsername
    },
    SETUSERTELE (state, newUserTele) {
      state.userTele = newUserTele
    },
    SETDRACCNO (state, newDraccNo) {
      state.draccNo = newDraccNo
    },
    SETROLES (state, newRoles) {
      state.roles = newRoles
    },
    SETTOKEN (state, newToken) {
      state.token = newToken
    }
  },

  actions: {
    setUserInfo ({commit}, data) {
      let {
        orgName, orgCode, userLoginId, 
        username, userTele, draccNo, roles
      } = data
      commit('SETORGNAME', orgName)
      commit('SETORGCODE', orgCode)
      commit('SETLOGINID', userLoginId)
      commit('SETUSERNAME', username)
      commit('SETUSERTELE', userTele)
      commit('SETDRACCNO', draccNo)
      commit('SETROLES', roles)
    },
    setToken ({commit}, data) {
      commit('SETTOKEN', data)
    }
  }
}

export default user
