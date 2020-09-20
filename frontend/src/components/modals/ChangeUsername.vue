<template>
  <Modal @close="$emit('close')" @ok="$emit('ok', username)">
    <h3 slot="header">New Dashboard</h3>
    <label class="form-label">
      New Username
      <input class="form-control" v-model="username" prefix="dashboardName" autofocus>
    </label>
  </Modal>
</template>
<script>
import Modal from './Modal.vue';

export default {
  name: 'ChangeUsername',
  components: {
    Modal,
  },
  data() {
    return {
      username: this.$store.getters.username,
    };
  },
};

</script>
