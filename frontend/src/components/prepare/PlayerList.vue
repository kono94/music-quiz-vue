<template>
  <section class="information lobby players">
    <div>
      <h5>Players list</h5>
    </div>
    <ul class="list-group" >
      <Player v-for="player in players" :player="player"/>
    </ul>
  </section>
</template>

<script>
import Player from './Player.vue';

export default {
  name: 'PlayerList',
  components: { Player },
  props: {
    players: Array,
  },
};
</script>

<style >
  ul {
    width: 100%;
  }
</style>
