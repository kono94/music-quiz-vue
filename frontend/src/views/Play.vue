<template>
  <div class="play">
    <LobbyList v-if="!inRoom && socketReady"/>
    <Room v-if="inRoom && socketReady"/>
    <span v-if="!socketReady"> CONNECTING TO SOCKET</span>
  </div>
</template>

<script>
// @ is an alias to /src
import LobbyList from '@/components/LobbyList.vue';
import Room from '@/components/Room.vue';
import gameSocket from '../gameSocket';
import { SET_ROOM_ID } from '../mutation-types';

export default {
  name: 'play',
  components: {
    LobbyList,
    Room,
  },
  data() {
    return {
      socketReady: false,
    };
  },
  computed: {
    inRoom() {
      return this.$store.getters.roomID !== null;
    },
  },
  async created() {
    await gameSocket.connectToSocket(this.$store);
    if (gameSocket.isOpen()) {
      this.socketReady = true;
    }
    if (this.$route.query.roomID) {
      this.$store.commit(SET_ROOM_ID, { roomID: this.$route.query.roomID });
    } else if (this.$store.getters.roomID !== null) {
      await this.$router.push({ query: { id: this.$store.getters.roomID } });
    }
  },
  methods: {
  },

};
</script>
