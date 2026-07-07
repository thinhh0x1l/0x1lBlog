<template>
  <div class="sm-wrap">
    <button class="sm-trigger" @click.stop="open = !open">•••</button>
    <div v-if="open" class="sm-dropdown" @click.stop>
      <button class="sm-item" @click="act('report')">Báo cáo</button>
      <button class="sm-item" @click="act('share')">Chia sẻ</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
const emit = defineEmits(['action'])
const open = ref(false)
const act = (a) => { open.value = false; emit('action', a) }
</script>

<style scoped lang="scss">
.sm-wrap { position: relative; }
.sm-trigger { background: none; border: none; color: rgba(255,255,255,0.7); font-size: 1.2rem; cursor: pointer; padding: 4px; letter-spacing: 2px; }
.sm-dropdown { position: absolute; bottom: 100%; right: 0; background: #1e293b; border-radius: 8px; overflow: hidden; min-width: 120px; margin-bottom: 4px; }
.sm-item { display: block; width: 100%; padding: 10px 16px; background: none; border: none; color: #fff; font-size: 0.82rem; text-align: left; cursor: pointer; }
.sm-item:hover { background: rgba(255,255,255,0.08); }
</style>
