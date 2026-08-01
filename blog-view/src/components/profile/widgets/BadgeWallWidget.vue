<template>
  <div class="widget-card">
    <div class="widget-header"><span class="widget-title">Huy hiệu</span></div>
    <div class="widget-body">
      <div class="badge-grid">
        <div v-for="b in badges" :key="b.id" class="badge-item" :title="b.displayName">
          <span class="badge-icon">{{ b.icon || '🏆' }}</span>
          <span class="badge-name">{{ b.displayName }}</span>
        </div>
      </div>
      <div v-if="!badges?.length" class="empty-state">Chưa có huy hiệu</div>
    </div>
  </div>
</template>
<script setup>
defineProps({
  badges: { type: Array, default: () => [] },
})
</script>
<style scoped lang="scss">
.widget-card { background: var(--surface); border-radius: var(--radius-xl); border: 1px solid var(--border-light); overflow: hidden; }
.widget-header { padding: 14px 16px; border-bottom: 1px solid var(--border-light); }
.widget-title { font-size: 0.85rem; font-weight: 700; }
.widget-body { padding: 12px 16px; }
.badge-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(70px, 1fr)); gap: 8px; }
.badge-item { display: flex; flex-direction: column; align-items: center; gap: 4px; padding: 8px 4px; border-radius: var(--radius); background: var(--bg-secondary); cursor: help; }
.badge-icon { font-size: 1.5rem; }
.badge-name { font-size: 0.62rem; color: var(--text-muted); text-align: center; word-break: break-word; line-height: 1.2; }
.empty-state { font-size: 0.8rem; color: var(--text-muted); text-align: center; padding: 12px 0; }
</style>
