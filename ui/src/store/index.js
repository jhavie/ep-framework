import Vue from 'vue'
import Vuex from 'vuex'
import app from './models/app'
import user from './models/user'
import getters from './getters'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    app,
    user
  },
  getters
})

export default store
