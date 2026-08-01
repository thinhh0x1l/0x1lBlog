<template>
  <div class="widget-card">
    <div class="widget-header"><span class="widget-title">Cây kỹ năng</span></div>
    <div class="widget-body">
      <div v-for="tree in trees" :key="tree.id" class="skill-item">
        <div class="skill-header">
          <span class="skill-name">{{ tree.name }}</span>
          <span class="skill-points">{{ tree.pointsRequired }} điểm</span>
        </div>
        <div class="skill-progress">
          <div class="skill-track">
            <div class="skill-fill" :style="{ width: skillPct(tree) + '%' }"></div>
          </div>
          <span class="skill-pct">{{ skillPct(tree) }}%</span>
        </div>
      </div>
      <div v-if="!trees?.length" class="empty-state">Chưa có kỹ năng</div>
    </div>
  </div>
</template>
<script setup>
import { computed } from 'vue'
const props = defineProps({
  trees: { type: Array, default: () => [] },
  progress: { type: Array, default: () => [] },
  unlocks: { type: Array, default: () => [] },
  isLoggedIn: { type: Boolean, default: false },
})
const myPoints = computed(() => props.progress?.reduce((s, p) => s + (p.totalPoints || 0), 0) || 0)
const unlockedIds = computed(() => props.unlocks || [])
const skillPct = (tree) => {
  if (unlockedIds.value.includes(tree.id)) return 100
  return Math.min(100, Math.round((myPoints.value / tree.pointsRequired) * 100))
}
</script>
<style scoped lang="scss">
.widget-card { background: var(--surface); border-radius: var(--radius-xl); border: 1px solid var(--border-light); overflow: hidden; }
.widget-header { padding: 14px 16px; border-bottom: 1px solid var(--border-light); }
.widget-title { font-size: 0.85rem; font-weight: 700; }
.widget-body { padding: 12px 16px; }
.skill-item { margin-bottom: 12px; }
.skill-item:last-child { margin-bottom: 0; }
.skill-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 4px; }
.skill-name { font-size: 0.78rem; font-weight: 500; }
.skill-points { font-size: 0.68rem; color: var(--text-muted); }
.skill-progress { display: flex; align-items: center; gap: 8px; }
.skill-track { flex: 1; height: 6px; background: var(--bg-secondary); border-radius: 99px; overflow: hidden; }
.skill-fill { height: 100%; background: linear-gradient(90deg, #f97316, #f59e0b); border-radius: 99px; transition: width 0.5s; }
.skill-pct { font-size: 0.68rem; color: var(--text-muted); flex-shrink: 0; }
.empty-state { font-size: 0.8rem; color: var(--text-muted); text-align: center; padding: 12px 0; }
</style>
