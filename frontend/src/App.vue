<template>
  <div id="app">
        <nav class="navbar">
        <div class="nav-links">
          <router-link to="/">Home</router-link>
          <router-link to="/play">Play</router-link>
          <router-link to="/about">About</router-link>
        </div>
      <div @click="showUserNameModal =  true">
        {{username}}
      </div>
          <ChangeUsername v-if="showUserNameModal" @close="showUserNameModal= false" @ok="handleOk"/>
       </nav>
     <router-view class="content"/>
    <Footer/>
  </div>
</template>
<script>
  // @ is an alias to /src
  import ChangeUsername from './components/modals/ChangeUsername.vue';
  import gameSocket from './gameSocket';

  export default {
  name: 'app',
  components: {
    ChangeUsername,
  },
  data() {
    return {
      showUserNameModal: false,
    };
  },
  computed: {
    username(){
      return this.$store.getters.username;
    },
  },
  async created(){
    await gameSocket.connectToSocket(this.$store);
  },
  methods: {
    handleOk(username) {
      gameSocket.changeUserName(username);
      this.showUserNameModal = false;
    },
  },
};
</script>

<style>
  :root {
    --primaryC: #dae2e2;
    --secondaryC: #8db1b1;
    --backgroundC: #e4f1eb;
    --darkC: #333;
    --lightC: #fff;
    --shadowC: 0 1px 5px rgba(104, 104, 104, 0.8);
  }

  html {
    box-sizing: border-box;
    font-family: Arial, Helvetica, sans-serif;
    color: var(--darkC);
  }

  html, body {
    background: var(--secondaryC);
    margin: 0px;
    line-height: 1.4;
  }

  #app {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
    height: 100%;
    display: grid;
    grid-template-columns: 1fr;
  }


  .navbar {
    padding-right:100px;
    padding-left: 100px;
    display:flex;
    justify-content: space-between;
    background: var(--secondaryC);
  }

  .nav-links{
    display: flex;
  }
  .navbar a {
    background: var(--primaryC);
    display: block;
    text-decoration: none;
    padding: 0.5rem 2rem;
    text-align: center;
    color: var(--dark);
    font-size: 1.1rem;
    font-weight: bold;
  }

  .navbar a.router-link-exact-active {
    text-decoration: underline;
  }

  .content {
    background-color: var(--backgroundC);
    padding-bottom: 200px;
    padding-top: 80px;
    display: flex;
    justify-content: center;
  }

  @media (max-width: 800px) {
  }
  .cursor-pointer{
    cursor: pointer;
  }
</style>
