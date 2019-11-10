<template>
  <section>
    <div>
      <h5>Room: {{room.id}} IN GAME</h5>
    </div>
    <button @click="$emit('stopGame')" v-if="ableToStop">Stop Game</button>
    <button @click="$emit('leaveRoom')">LEAVE ROOM</button>
  </section>
</template>
<script>
  import gameSocket from '../gameSocket';

  export default {
  name: 'RoomRunning',
  components: {
  },
  data() {
    return {

    };
  },
  computed: {
    room() {
      return this.$store.getters.room;
    },
    ableToStop() {
      return this.$store.getters.isRoomAdmin;
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
