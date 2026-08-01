<template>
  <div class="sidebar-card trending-mini" v-if="items.length">
    <div class="card-header">
      <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="23 6 13.5 15.5 8.5 10.5 1 18"/><polyline points="17 6 23 6 23 12"/></svg>
      <span>Xu hướng</span>
    </div>
    <div class="trending-list">
      <NuxtLink v-for="(item, idx) in items" :key="item.id" :to="`/blog/${item.id}`" class="trending-item">
        <span class="trend-num">{{ idx + 1 }}</span>
        <span class="trend-title">{{ item.title }}</span>
      </NuxtLink>
    </div>
  </div>
</template>
<script setup lang="ts">
defineProps({ items: { type: Array, default: () => [] } })
</script>
<style scoped lang="scss">
.trending-mini { padding: 16px; }
.trending-mini .card-header { display: flex; align-items: center; gap: 8px; font-size: 0.85rem; font-weight: 600; margin-bottom: 10px; }
.trending-list { display: flex; flex-direction: column; gap: 2px; }
.trending-item { display: flex; align-items: center; gap: 10px; padding: 8px; border-radius: var(--radius-sm); text-decoration: none; transition: background 0.15s; }
.trending-item:hover { background: var(--surface-hover); }
.trend-num { width: 20px; height: 20px; border-radius: var(--radius-sm); display: flex; align-items: center; justify-content: center; font-size: 0.7rem; font-weight: 700; background: var(--bg); color: var(--text-muted); flex-shrink: 0; }
.trend-title { font-size: 0.78rem; color: var(--text-secondary); overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
</style>
