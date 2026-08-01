<template>
  <div class="sidebar-card skill-card" v-if="trees.length">
    <div class="card-header">
      <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 2L2 7l10 5 10-5-10-5z"/><path d="M2 17l10 5 10-5"/><path d="M2 12l10 5 10-5"/></svg>
      <span>Cây kỹ năng</span>
    </div>
    <div class="skill-list">
      <div v-for="tree in trees.slice(0, 4)" :key="tree.id" class="skill-item">
        <div class="skill-header">
          <span class="skill-name">{{ tree.name }}</span>
          <span class="skill-points">{{ tree.pointsRequired }} điểm</span>
        </div>
        <div class="skill-progress" v-if="isLoggedIn && myProgress">
          <div class="skill-track">
            <div class="skill-fill" :style="{ width: skillPct(tree.pointsRequired) + '%' }"></div>
          </div>
          <span class="skill-pct">{{ skillPct(tree.pointsRequired) }}%</span>
        </div>
        <div v-else-if="skillPct(tree.id) >= 100" class="skill-unlocked">✅ Đã mở khóa</div>
      </div>
    </div>
    <div class="skill-footer" v-if="trees.length > 4">
      <span>+{{ trees.length - 4 }} kỹ năng khác</span>
    </div>
  </div>
</template>
<script setup>
import { computed } from 'vue'
const props = defineProps({
  trees: { type: Array, default: () => [] },
  myProgress: { type: Object, default: null },
  isLoggedIn: { type: Boolean, default: false },
})
const myPoints = computed(() => props.myProgress?.progress?.totalPoints || 0)
const unlockedIds = computed(() => props.myProgress?.unlockedSkillIds || [])
const skillPct = (pointsRequired) => {
  if (unlockedIds.value.length > 0) {
    const skill = props.trees.find(t => t.pointsRequired === pointsRequired)
    if (skill && unlockedIds.value.includes(skill.id)) return 100
  }
  return Math.min(100, Math.round((myPoints.value / pointsRequired) * 100))
}
</script>
<style scoped lang="scss">
.skill-card { padding: 16px; }
.skill-card .card-header { display: flex; align-items: center; gap: 8px; font-size: 0.85rem; font-weight: 600; margin-bottom: 12px; }
.skill-list { display: flex; flex-direction: column; gap: 10px; }
.skill-item { }
.skill-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 4px; }
.skill-name { font-size: 0.78rem; font-weight: 500; }
.skill-points { font-size: 0.68rem; color: var(--text-muted); }
.skill-progress { display: flex; align-items: center; gap: 8px; }
.skill-track { flex: 1; height: 4px; background: var(--bg-secondary); border-radius: 99px; overflow: hidden; }
.skill-fill { height: 100%; background: linear-gradient(90deg, #f97316, #f59e0b); border-radius: 99px; transition: width 0.5s; }
.skill-pct { font-size: 0.68rem; color: var(--text-muted); flex-shrink: 0; }
.skill-unlocked { font-size: 0.72rem; color: var(--success); }
.skill-footer { font-size: 0.7rem; color: var(--text-muted); text-align: center; margin-top: 10px; padding-top: 8px; border-top: 1px solid var(--border-light); }
</style>
