<template>
  <div class="play">
    <LobbyList v-if="!inRoom && isSocketConnected"/>
    <Room v-if="inRoom && isSocketConnected"/>
    <span v-if="!isSocketConnected"> CONNECTING TO SOCKET</span>
  </div>
</template>

<script>
// @ is an alias to /src
import LobbyList from '@/components/LobbyList.vue';
import Room from '@/components/Room.vue';
import gameSocket from '../gameSocket';

export default {
  name: 'play',
  components: {
    LobbyList,
    Room,
  },
  data() {
    return {
    };
  },
  computed: {
    inRoom() {
      return this.$store.getters.roomID !== null;
    },
    isSocketConnected() {
      return this.$store.getters.isSocketConnected;
    },
  },
  async created() {
    try {
      await gameSocket.connectToSocket(this.$store);
      if (this.$route.query.roomID) {
        gameSocket.joinRoom(this.$route.query.roomID);
      } else if (this.$store.getters.roomID !== null) {
        await this.$router.push({ query: { id: this.$store.getters.roomID } });
      }
    } catch (e) {
      console.log(e);
    }
  },
  methods: {
  },

};
</script>
