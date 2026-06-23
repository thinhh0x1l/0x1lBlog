<template>
  <div class="sidebar-card canvas-card" v-if="canvas">
    <div class="card-header">
      <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="18" height="18" rx="2" ry="2"/><circle cx="8.5" cy="8.5" r="1.5"/><polyline points="21 15 16 10 5 21"/></svg>
      <span>Canvas</span>
    </div>
    <div class="canvas-thumb" @click="$emit('viewCanvas', canvas)">
      <img v-if="canvas.thumbnailUrl" :src="canvas.thumbnailUrl" alt="Canvas" />
      <div v-else class="canvas-placeholder">🎨</div>
    </div>
    <p class="canvas-caption">Click để xem full</p>
  </div>
</template>
<script setup>
defineProps({ canvas: Object })
defineEmits(['viewCanvas'])
</script>
<style scoped lang="scss">
.canvas-card { padding: 16px; }
.canvas-card .card-header { display: flex; align-items: center; gap: 8px; font-size: 0.85rem; font-weight: 600; margin-bottom: 10px; }
.canvas-thumb { border-radius: var(--radius); overflow: hidden; cursor: pointer; }
.canvas-thumb img { width: 100%; height: auto; display: block; transition: transform 0.3s ease; }
.canvas-thumb:hover img { transform: scale(1.03); }
.canvas-placeholder { width: 100%; aspect-ratio: 1; background: var(--bg-secondary); display: flex; align-items: center; justify-content: center; font-size: 3rem; }
.canvas-caption { font-size: 0.72rem; color: var(--text-muted); text-align: center; margin-top: 8px; }
</style>
