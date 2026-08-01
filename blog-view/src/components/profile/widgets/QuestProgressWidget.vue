<template>
  <div class="widget-card">
    <div class="widget-header"><span class="widget-title">Nhiệm vụ</span></div>
    <div class="widget-body">
      <div v-for="q in quests" :key="q.id" class="quest-item">
        <div class="quest-info">
          <span class="quest-title">{{ getQuestTitle(q.questId) }}</span>
          <div class="quest-progress">
            <div class="quest-track">
              <div class="quest-fill" :style="{ width: (q.progress / q.target * 100) + '%' }"></div>
            </div>
            <span class="quest-count">{{ q.progress }}/{{ q.target }}</span>
          </div>
        </div>
        <el-button v-if="q.status === 'COMPLETED'" type="primary" size="small" round>Nhận</el-button>
        <el-tag v-else-if="q.status === 'CLAIMED'" type="success" size="small" effect="plain">✅</el-tag>
      </div>
      <div v-if="!quests?.length" class="empty-state">Chưa có nhiệm vụ</div>
    </div>
  </div>
</template>
<script setup>
defineProps({
  quests: { type: Array, default: () => [] },
})
const questTitles = { 1: 'Viết 1 blog', 2: 'Đọc 3 blogs', 3: 'Reaction 5 bài', 4: 'Viết 1 status', 5: 'Viết 3 blogs (tuần)' }
const getQuestTitle = (id) => questTitles[id] || `Nhiệm vụ #${id}`
</script>
<style scoped lang="scss">
.widget-card { background: var(--surface); border-radius: var(--radius-xl); border: 1px solid var(--border-light); overflow: hidden; }
.widget-header { padding: 14px 16px; border-bottom: 1px solid var(--border-light); }
.widget-title { font-size: 0.85rem; font-weight: 700; }
.widget-body { padding: 12px 16px; }
.quest-item { display: flex; align-items: center; gap: 10px; margin-bottom: 10px; }
.quest-item:last-child { margin-bottom: 0; }
.quest-info { flex: 1; }
.quest-title { display: block; font-size: 0.78rem; font-weight: 500; margin-bottom: 4px; }
.quest-progress { display: flex; align-items: center; gap: 8px; }
.quest-track { flex: 1; height: 6px; background: var(--bg-secondary); border-radius: 99px; overflow: hidden; }
.quest-fill { height: 100%; background: linear-gradient(90deg, var(--primary), #8b5cf6); border-radius: 99px; transition: width 0.5s ease; }
.quest-count { font-size: 0.68rem; color: var(--text-muted); flex-shrink: 0; }
.empty-state { font-size: 0.8rem; color: var(--text-muted); text-align: center; padding: 12px 0; }
</style>
