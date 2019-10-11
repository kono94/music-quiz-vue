import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    roomID: null,
    username: null,
  },
  mutations: {
  },
  actions: {

  },
  getters: {
    roomID: state => state.roomID,
    username: state => state.user,
  },
});
