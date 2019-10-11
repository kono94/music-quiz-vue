<template>
  <div id="app">
        <nav class="navbar">
        <div class="nav-links">
          <router-link to="/">Home</router-link>
          <router-link to="/play">Play</router-link>
          <router-link to="/about">About</router-link>
        </div>
      <div>
        Username
      </div>
       </nav>
     <router-view class="content"/>
    <Footer/>
  </div>
</template>
<script>
// @ is an alias to /src

export default {
  name: 'app',
  components: {
  },
};
</script>

<style>
  :root {
    --primary: #dae2e2;
    --secondary: #8db1b1;
    --background: #e4f1eb;
    --dark: #333;
    --light: #fff;
    --shadow: 0 1px 5px rgba(104, 104, 104, 0.8);
  }

  html {
    box-sizing: border-box;
    font-family: Arial, Helvetica, sans-serif;
    color: var(--dark);
  }

  html, body {
    background: var(--secondary);
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
  }

  .nav-links{
    display: flex;
  }
  .navbar a {
    background: var(--primary);
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
    background-color: var(--background);
    padding-bottom: 200px;
    padding-top: 80px;
    display: flex;
    justify-content: center;
  }

  @media (max-width: 800px) {
  }
</style>
