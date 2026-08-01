<template>
  <div class="sidebar-card quest-panel" v-if="quests.length">
    <div class="card-header">
      <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><path d="M12 6v6l4 2"/></svg>
      <span>Nhiệm vụ hôm nay</span>
    </div>
    <div class="quest-list">
      <div v-for="q in quests.slice(0, 3)" :key="q.id" class="quest-item">
        <div class="quest-info">
          <span class="quest-title">{{ getQuestTitle(q.questId) }}</span>
          <div class="quest-progress">
            <div class="quest-track">
              <div class="quest-fill" :style="{ width: (q.progress / q.target * 100) + '%' }"></div>
            </div>
            <span class="quest-count">{{ q.progress }}/{{ q.target }}</span>
          </div>
        </div>
        <el-button v-if="q.status === 'COMPLETED'" type="primary" size="small" round @click="$emit('claim', q.id)">Nhận</el-button>
        <el-tag v-else-if="q.status === 'CLAIMED'" type="success" size="small" effect="plain">✅</el-tag>
      </div>
    </div>
    <div class="quest-summary">
      {{ completedCount }}/{{ quests.length }} nhiệm vụ hoàn thành
    </div>
  </div>
</template>
<script setup>
import { computed } from 'vue'
const props = defineProps({
  quests: { type: Array, default: () => [] },
  questNames: { type: Object, default: () => ({}) },
})
defineEmits(['claim'])
const questTitles = { 1: 'Viết 1 blog', 2: 'Đọc 3 blogs', 3: 'Reaction 5 bài', 4: 'Viết 1 status', 5: 'Viết 3 blogs (tuần)' }
const getQuestTitle = (id) => props.questNames[id] || questTitles[id] || `Nhiệm vụ #${id}`
const completedCount = computed(() => props.quests.filter(q => q.status === 'COMPLETED' || q.status === 'CLAIMED').length)
</script>
<style scoped lang="scss">
.quest-panel { padding: 16px; }
.quest-panel .card-header { display: flex; align-items: center; gap: 8px; font-size: 0.85rem; font-weight: 600; margin-bottom: 12px; }
.quest-list { display: flex; flex-direction: column; gap: 10px; }
.quest-item { display: flex; align-items: center; gap: 10px; }
.quest-info { flex: 1; }
.quest-title { display: block; font-size: 0.78rem; font-weight: 500; margin-bottom: 4px; }
.quest-progress { display: flex; align-items: center; gap: 8px; }
.quest-track { flex: 1; height: 4px; background: var(--bg-secondary); border-radius: 99px; overflow: hidden; }
.quest-fill { height: 100%; background: linear-gradient(90deg, var(--primary), #8b5cf6); border-radius: 99px; transition: width 0.5s ease; }
.quest-count { font-size: 0.68rem; color: var(--text-muted); flex-shrink: 0; }
.quest-summary { font-size: 0.7rem; color: var(--text-muted); text-align: center; margin-top: 12px; padding-top: 10px; border-top: 1px solid var(--border-light); }
</style>
