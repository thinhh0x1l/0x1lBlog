<template>
  <div class="sidebar-card canvas-card" v-if="canvas">
    <div class="card-header">
      <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="18" height="18" rx="2" ry="2"/><circle cx="8.5" cy="8.5" r="1.5"/><polyline points="21 15 16 10 5 21"/></svg>
      <span>Canvas</span>
    </div>
    <div class="canvas-thumb" @click="$emit('viewCanvas', canvas)">
      <img v-if="canvas.thumbnailUrl" :src="canvas.thumbnailUrl" alt="Canvas" />
      <div v-else class="canvas-placeholder">&#127912;</div>
    </div>
    <div class="canvas-meta">
      <span :class="['type-badge', `type-${canvas.type?.toLowerCase()}`]">{{ canvas.type }}</span>
      <span class="canvas-size-label">{{ canvas.width }}x{{ canvas.height }}</span>
    </div>
    <p class="canvas-caption">Click xem full</p>
  </div>
  <div class="sidebar-card canvas-card canvas-empty" v-else>
    <div class="card-header">
      <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="18" height="18" rx="2" ry="2"/><circle cx="8.5" cy="8.5" r="1.5"/><polyline points="21 15 16 10 5 21"/></svg>
      <span>Canvas</span>
    </div>
    <div class="canvas-empty-body">
      <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" opacity="0.3"><rect x="3" y="3" width="18" height="18" rx="2"/><circle cx="8.5" cy="8.5" r="1.5"/><polyline points="21 15 16 10 5 21"/></svg>
      <p class="empty-text">Tác giả chưa có canvas</p>
    </div>
  </div>
</template>
<script setup lang="ts">
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
.canvas-meta { display: flex; align-items: center; gap: 8px; margin-top: 8px; }
.type-badge { font-size: 0.65rem; font-weight: 600; padding: 2px 8px; border-radius: var(--radius-full); text-transform: uppercase; letter-spacing: 0.5px; }
.type-profile { background: rgba(14,165,233,0.15); color: #0ea5e9; }
.type-community { background: rgba(16,185,129,0.15); color: #10b981; }
.type-event { background: rgba(245,158,11,0.15); color: #f59e0b; }
.canvas-size-label { font-size: 0.65rem; color: var(--text-muted); }
.canvas-caption { font-size: 0.72rem; color: var(--text-muted); text-align: center; margin-top: 8px; }
.canvas-empty { opacity: 0.6; }
.canvas-empty-body { display: flex; flex-direction: column; align-items: center; gap: 8px; padding: 16px 0; }
.empty-text { font-size: 0.78rem; color: var(--text-muted); margin: 0; }
</style>
