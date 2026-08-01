<template>
  <div class="canvas-cover" :style="coverStyle" @click="$emit('viewCanvas', canvas)" style="cursor: pointer;">
    <img v-if="canvas?.thumbnailUrl" :src="canvas.thumbnailUrl" class="canvas-image" />
    <div v-else class="canvas-placeholder">
      <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><rect x="3" y="3" width="18" height="18" rx="2"/><circle cx="8.5" cy="8.5" r="1.5"/><polyline points="21 15 16 10 5 21"/></svg>
      <span>Soul Space</span>
    </div>
    <div class="canvas-edit-badge" v-if="isOwn" title="Chỉnh sửa canvas">
      <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 20h9"/><path d="M16.5 3.5a2.121 2.121 0 0 1 3 3L7 19l-4 1 1-4L16.5 3.5z"/></svg>
    </div>
  </div>
</template>
<script setup>
import { computed } from 'vue'
const props = defineProps({ canvas: Object, isOwn: { type: Boolean, default: false } })
defineEmits(['viewCanvas'])
const coverStyle = computed(() => {
  if (props.canvas?.thumbnailUrl) return {}
  return { background: 'linear-gradient(135deg, var(--primary), #8b5cf6 50%, #ec4899)' }
})
</script>
<style scoped lang="scss">
.canvas-cover { width: 100%; height: 280px; overflow: hidden; position: relative; }
.canvas-image { width: 100%; height: 100%; object-fit: cover; }
.canvas-placeholder { width: 100%; height: 100%; display: flex; flex-direction: column; align-items: center; justify-content: center; gap: 8px; color: rgba(255,255,255,0.6); font-size: 1rem; font-weight: 600; }
.canvas-placeholder svg { width: 48px; height: 48px; }
.canvas-edit-badge { position: absolute; top: 12px; right: 12px; width: 36px; height: 36px; border-radius: 50%; background: rgba(0,0,0,0.5); color: white; display: flex; align-items: center; justify-content: center; backdrop-filter: blur(4px); transition: all 0.2s ease; }
.canvas-edit-badge:hover { background: rgba(0,0,0,0.7); transform: scale(1.1); }
</style>
