<template>
  <div>
    Lobbies
    <ul>
      <li v-for="(roomID, index) in Object.keys(rooms)"
      :key="index">
        ID: {{roomID}}
      </li>
    </ul>
  </div>
</template>
<script>
import gameSocket from '../gameSocket';
import * as SocketRoutes from '../socket-routes';

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
    gameSocket.subscribe(SocketRoutes.REFRESH_LOBBY_LIST, this.refreshLobbyList);
  },
  methods: {
    refreshLobbyList(data) {
      console.log(data)
      this.rooms = data;
    },
  },

};

</script>
