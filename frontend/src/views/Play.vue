<template>
  <div class="play">
    <Lobby v-if="!connected"/>
    <Game v-else/>
  </div>
</template>

<script>
// @ is an alias to /src
import Lobby from '@/components/Lobby.vue';
import Game from '@/components/Game.vue';

export default {
  name: 'play',
  components: {
    Lobby,
    Game,
  },
  computed: {
    connected() {
      return this.$stompClient.isConnected();
    },
  },
  created(){
    console.log(this.$stompClient)
  },
  methods:{
  }

};
</script>
