<template>
  <div>
    Lobbies
    <ul class="list-group">
      <li @click="clickOnRoom(room)" v-for="(room, index) in rooms"
      :key="index" class="list-group-item list-group-item-action cursor-pointer">
        ID: {{room.id}} in-game: {{room.running}}
      </li>
    </ul>
  </div>
</template>
<script>
import gameSocket from '../gameSocket';
import * as Events from '../events';

export default {
  name: 'lobbyList',
  components: {

  },
  data() {
    return {
      rooms: [],
    };
  },
  computed: {
  },
  mounted() {
    gameSocket.requestLobbyData();
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
    clickOnRoom(room) {
      this.$emit('joinRoom', room);
    },
  },
  beforeDestroy() {
    gameSocket.socket.removeEventListener('message', this.messageListener);
  },
};

</script>
