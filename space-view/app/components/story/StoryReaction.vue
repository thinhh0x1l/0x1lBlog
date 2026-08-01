<template>
  <div class="sr-picker">
    <button v-for="r in reactions" :key="r" class="sr-btn" @click="$emit('react', r)">{{ r }}</button>
  </div>
</template>

<script setup lang="ts">
defineEmits(['react'])
const reactions = ['❤️', '😂', '😮', '😢', '😡']
</script>

<style scoped lang="scss">
.sr-picker { display: flex; gap: 6px; }
.sr-btn { background: rgba(255,255,255,0.1); border: none; border-radius: 50%; width: 36px; height: 36px; font-size: 1.2rem; cursor: pointer; transition: transform 0.15s; display: flex; align-items: center; justify-content: center; }
.sr-btn:hover { transform: scale(1.25); background: rgba(255,255,255,0.2); }
</style>
