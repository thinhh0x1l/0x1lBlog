<template>
  <div class="widget-grid">
    <div class="widget-placeholder" v-for="widget in visibleWidgets" :key="widget.widgetType">
      <span class="widget-label">{{ widget.widgetType }}</span>
    </div>
  </div>
</template>
<script setup lang="ts">
const props = defineProps({
  profileData: { type: Object, default: () => ({}) },
  isOwn: Boolean,
})
const visibleWidgets = computed(() => {
  const layout = props.profileData?.profileLayout || []
  return layout.filter((w: any) => w.isVisible)
})
</script>
<style scoped lang="scss">
.widget-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 16px; padding: 24px 0; }
.widget-placeholder { background: var(--surface); border: 1px solid var(--border-light); border-radius: var(--radius-lg); padding: 24px; min-height: 120px; display: flex; align-items: center; justify-content: center; }
.widget-label { font-size: 0.85rem; color: var(--text-muted); font-weight: 500; }
@media (max-width: 1024px) { .widget-grid { grid-template-columns: repeat(2, 1fr); } }
@media (max-width: 640px) { .widget-grid { grid-template-columns: 1fr; } }
</style>
