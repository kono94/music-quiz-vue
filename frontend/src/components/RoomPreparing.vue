<template>
  <section>
    <div>
      <h5>Room: {{room.id}}</h5>
    </div>
    <Settings :room="room"/>
    <PlayerList :players="room.players"/>
    <button @click="$emit('leaveRoom')">LEAVE ROOM</button>
    <button @click="$emit('startGame')" :disabled="!ableToStart" v-if="isRoomAdmin" >START GAME</button>
  </section>
</template>
<script>
import gameSocket from '../gameSocket';
import PlayerList from './prepare/PlayerList.vue';
import Settings from './prepare/RoomSettings.vue';

export default {
  name: 'RoomPreparing',
  components: {
    Settings,
    PlayerList,

  },
  data() {
    return {

    };
  },
  computed: {
    isRoomAdmin() {
      return this.$store.getters.isRoomAdmin;
    },
    room() {
      return this.$store.getters.room;
    },
    ableToStart() {
      let allReady = true;
      this.$store.getters.room.players.forEach((player) => {
        if (!player.ready) {
          allReady = false;
        }
      });
      return allReady && this.$store.getters.isRoomAdmin;
    },
  },
  created() {
    gameSocket.requestRoomData();
    gameSocket.addEventListener('message', this.messageListener);
  },
  methods: {
    messageListener(event) {
      const msg = JSON.parse(event.data);
      switch (msg.event) {
      }
    },
  },
  beforeDestroy() {
    gameSocket.socket.removeEventListener('message', this.messageListener);
  },
};

</script>
