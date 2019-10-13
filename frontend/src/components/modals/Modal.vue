<template>
  <transition name="modal">
    <div class="modal-mask" @click="close">
      <div class="modal-container" @click.stop>
        <div class="modal-header">

          <slot name="header"></slot>

        </div>
        <div class="modal-body">

          <slot></slot>

        </div>
        <div class="modal-footer text-right">
          <button class="btn btn-group-sm btn-default" @click="close()">
            Cancel
          </button>
          <button class="btn btn-group-sm btn-primary" @click="ok()">
            Ok
          </button>
        </div>
      </div>
    </div>
  </transition>
</template>
<script>
export default {
  name: 'Modal',
  components: {

  },
  data() {
    return {
    };
  },
  methods: {
    close() {
      this.$emit('close');
    },
    ok() {
      this.$emit('ok');
    },
  },

};

</script>
