<template>
  <div class="sidebar-card blog-tags" v-if="tags.length">
    <div class="card-header">
      <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20.59 13.41l-7.17 7.17a2 2 0 0 1-2.83 0L2 12V2h10l8.59 8.59a2 2 0 0 1 0 2.82z"/><line x1="7" y1="7" x2="7.01" y2="7"/></svg>
      <span>Tags</span>
    </div>
    <div class="tag-cloud">
      <NuxtLink v-for="tag in tags" :key="tag.id" :to="`/tag/${tag.id}`" class="tag-pill">#{{ tag.name }}</NuxtLink>
    </div>
  </div>
</template>
<script setup lang="ts">
defineProps({ tags: { type: Array, default: () => [] } })
</script>
<style scoped lang="scss">
.blog-tags { padding: 16px; }
.blog-tags .card-header { display: flex; align-items: center; gap: 8px; font-size: 0.85rem; font-weight: 600; margin-bottom: 10px; }
.tag-cloud { display: flex; flex-wrap: wrap; gap: 6px; }
.tag-pill { padding: 4px 10px; background: var(--bg); border-radius: 99px; font-size: 0.72rem; color: var(--text-secondary); text-decoration: none; transition: all 0.15s; }
.tag-pill:hover { background: var(--primary-50); color: var(--primary); }
</style>
