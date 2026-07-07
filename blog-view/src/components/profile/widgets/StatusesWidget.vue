<template>
  <div class="widget-card">
    <div class="widget-header"><span class="widget-title">Trạng thái</span></div>
    <div class="widget-body">
      <div v-for="s in statuses" :key="s.id" class="status-item">
        <p class="status-content">{{ s.content }}</p>
        <span class="status-time">{{ formatDate(s.createdAt) }}</span>
      </div>
      <div v-if="!statuses?.length" class="empty-state">Chưa có trạng thái</div>
    </div>
  </div>
</template>
<script setup>
defineProps({
  statuses: { type: Array, default: () => [] },
})
const formatDate = (d) => d ? new Date(d).toLocaleDateString('vi-VN', { year: 'numeric', month: 'short', day: 'numeric' }) : ''
</script>
<style scoped lang="scss">
.widget-card { background: var(--surface); border-radius: var(--radius-xl); border: 1px solid var(--border-light); overflow: hidden; }
.widget-header { padding: 14px 16px; border-bottom: 1px solid var(--border-light); }
.widget-title { font-size: 0.85rem; font-weight: 700; }
.widget-body { padding: 12px 16px; }
.status-item { padding: 10px 0; border-bottom: 1px solid var(--border-light); }
.status-item:last-child { border-bottom: none; }
.status-content { font-size: 0.85rem; color: var(--text-primary); margin-bottom: 4px; line-height: 1.4; }
.status-time { font-size: 0.68rem; color: var(--text-muted); }
.empty-state { font-size: 0.8rem; color: var(--text-muted); text-align: center; padding: 12px 0; }
</style>
