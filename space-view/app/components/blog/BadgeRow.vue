<template>
  <div class="badge-row" v-if="badges.length">
    <div v-for="b in badges.slice(0, max)" :key="b.id" class="badge-mini" :title="b.displayName">
      <span class="badge-icon">{{ b.icon || '🏆' }}</span>
    </div>
    <span class="badge-more" v-if="badges.length > max">+{{ badges.length - max }}</span>
  </div>
</template>
<script setup lang="ts">
defineProps({
  badges: { type: Array, default: () => [] },
  max: { type: Number, default: 4 },
})
</script>
<style scoped lang="scss">
.badge-row { display: flex; align-items: center; gap: 4px; padding: 4px 0; }
.badge-mini { width: 24px; height: 24px; border-radius: 50%; display: flex; align-items: center; justify-content: center; background: var(--bg-secondary); cursor: help; }
.badge-icon { font-size: 0.8rem; }
.badge-more { font-size: 0.68rem; color: var(--text-muted); font-weight: 600; }
</style>
