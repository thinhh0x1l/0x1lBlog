<template>
  <div class="srp-row">
    <input v-model="text" class="srp-input" placeholder="Trả lời..." @keydown.enter="send" maxlength="200" />
    <button class="srp-send" :disabled="!text.trim()" @click="send">Gửi</button>
  </div>
</template>

<script setup lang="ts">
const emit = defineEmits(['reply'])
const text = ref('')
const send = () => { if (!text.value.trim()) return; emit('reply', text.value.trim()); text.value = '' }
</script>

<style scoped lang="scss">
.srp-row { display: flex; gap: 6px; flex: 1; }
.srp-input { flex: 1; background: rgba(255,255,255,0.1); border: none; border-radius: 20px; padding: 8px 14px; color: #fff; font-size: 0.82rem; outline: none; }
.srp-input::placeholder { color: rgba(255,255,255,0.4); }
.srp-send { background: none; border: none; color: #60a5fa; font-size: 0.85rem; font-weight: 600; cursor: pointer; padding: 0 8px; }
.srp-send:disabled { opacity: 0.4; cursor: default; }
</style>
