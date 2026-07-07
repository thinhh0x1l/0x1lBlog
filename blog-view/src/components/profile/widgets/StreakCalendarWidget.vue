<template>
  <div class="widget-card">
    <div class="widget-header"><span class="widget-title">Điểm danh</span></div>
    <div class="widget-body">
      <div class="streak-display">
        <span class="streak-fire">🔥</span>
        <span class="streak-count">{{ streak }}</span>
        <span class="streak-label">ngày liên tiếp</span>
      </div>
      <div class="mini-calendar">
        <span v-for="(day, i) in weekDays" :key="i" :class="['cal-day', { active: i < streak, today: i === 6 }]">
          {{ day }}
        </span>
      </div>
    </div>
  </div>
</template>
<script setup>
import { computed } from 'vue'
const props = defineProps({
  streak: { type: Number, default: 0 },
})
const weekDays = computed(() => {
  const days = []
  const today = new Date()
  for (let i = 6; i >= 0; i--) {
    const d = new Date(today)
    d.setDate(d.getDate() - i)
    days.push(d.getDate())
  }
  return days
})
</script>
<style scoped lang="scss">
.widget-card { background: var(--surface); border-radius: var(--radius-xl); border: 1px solid var(--border-light); overflow: hidden; }
.widget-header { padding: 14px 16px; border-bottom: 1px solid var(--border-light); }
.widget-title { font-size: 0.85rem; font-weight: 700; }
.widget-body { padding: 12px 16px; }
.streak-display { display: flex; align-items: center; justify-content: center; gap: 8px; margin-bottom: 16px; }
.streak-fire { font-size: 2rem; }
.streak-count { font-size: 2.5rem; font-weight: 800; color: var(--accent); }
.streak-label { font-size: 0.8rem; color: var(--text-muted); }
.mini-calendar { display: flex; justify-content: center; gap: 6px; }
.cal-day { width: 32px; height: 32px; border-radius: var(--radius-sm); display: flex; align-items: center; justify-content: center; font-size: 0.78rem; font-weight: 500; background: var(--bg-secondary); color: var(--text-muted); }
.cal-day.active { background: linear-gradient(135deg, #f97316, #f59e0b); color: white; }
.cal-day.today { border: 2px solid var(--primary); }
</style>
