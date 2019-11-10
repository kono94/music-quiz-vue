<template>
  <li class="list-group-item">
    <div>
      <div> <span v-if="isAdmin()">(A) </span> {{player.username}} <span v-if="isLocalUser()">(YOU)</span> </div>
      <button type="submit" @click="toggleReady()" class="btn "
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
    isAdmin(){
      return this.player.sessionID === this.$store.getters.roomAdminID;
    },
    isLocalUser() {
      return this.player.sessionID === this.$store.getters.sessionID;
    },
    toggleReady() {
      if (this.isLocalUser()) {
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
