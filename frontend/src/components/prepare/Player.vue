<template>
  <li class="list-group-item">
    <div>
      <div>{{player.username}}</div>
      <button type="submit" @click="$emit('toggleReady')" class="btn "
              :class="[player.ready ? 'btn-warning' : 'btn-success']">
        {{player.ready ? 'Ready' : 'Not Ready'}}
      </button>
    </div>
  </li>

</template>

<script>
export default {
  name: 'Player',
  props: {
    player: Object,
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
