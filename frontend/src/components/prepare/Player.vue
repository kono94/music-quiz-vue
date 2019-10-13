<template>
  <li class="list-group-item">
    <div>
      <div>{{player.username}}</div>
      <button type="submit" @click="toggleReady" class="btn "
              :class="[player.ready ? 'btn-success' : 'btn-warning']">
        {{player.ready ? 'Ready' : 'Not Ready'}}
      </button>
    </div>
  </li>

</template>

<script>
  import gameSocket from '../../gameSocket';

  export default {
  name: 'Player',
  props: {
    player: Object,
  },
  methods: {
    toggleReady() {
      if(this.player.sessionID === this.$store.getters.sessionID){
        gameSocket.toggleReady();
      }
    },
  },
};
</script>

<style scoped>
  li {

    background-color: transparent;

  }
  li > div {
    color: red;
    display: flex;
    width: 100%;
    justify-content: space-between;
  }

  .userColor {
    padding: 0;
    width: 25px;
    height: 25px;
  }
</style>
