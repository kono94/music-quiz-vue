<template>
  <section>
    <div>
      <h5>Room: {{room.id}}</h5>
    </div>
    <Settings :room="room"/>
    <PlayerList :players="room.players"/>
  </section>
</template>
<script>
  import gameSocket from '../gameSocket';
  import * as Events from '../events';
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
  computed:{
    room(){
      return this.$store.getters.room;
    }
  },
  created() {
    gameSocket.requestRoomData();
    gameSocket.addEventListener('message', this.messageListener);
  },
  methods: {
    messageListener(event) {
      const msg = JSON.parse(event.data);
      switch (msg.event) {
        case Events.REFRESH_LOBBY_LIST:
          this.rooms = msg.payload;
          break;
      }
    },
  },
  beforeDestroy() {
    gameSocket.socket.removeEventListener('message', this.messageListener);
  },
};

</script>
