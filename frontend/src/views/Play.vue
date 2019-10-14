<template>
  <div class="play">
    <RoomList v-if="!inRoom && isSocketConnected"
    @joinRoom="joinRoom"/>
    <RoomPreparing v-if="inRoom && !isRoomRunning && isSocketConnected" @leaveRoom="leaveRoom"/>
    <RoomRunning v-if="inRoom && isRoomRunning  && isSocketConnected" @leaveRoom="leaveRoom"/>
    <span v-if="!isSocketConnected"> CONNECTING TO SOCKET</span>
  </div>
</template>

<script>
  // @ is an alias to /src
  import RoomList from '@/components/RoomList.vue';
  import RoomPreparing from '@/components/RoomPreparing.vue';
  import RoomRunning from '@/components/RoomRunning.vue';
  import gameSocket from '../gameSocket';

  export default {
  name: 'play',
  components: {
    RoomList,
    RoomPreparing,
    RoomRunning,
  },
  data() {
    return {
    };
  },
  computed: {
    inRoom() {
      return this.$store.getters.room !== null;
    },
    isRoomRunning() {
      return this.$store.getters.isRoomRunning;
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
    joinRoom(room){
      gameSocket.joinRoom(room.id);
    },
    leaveRoom(){
      gameSocket.leaveRoom();
    }
  },

};
</script>
