<template>
  <div class="exp-bar">
    <div class="exp-bar-header">
      <span class="exp-level">Lv.{{ level }}</span>
      <span class="exp-rep" v-if="rep !== undefined">★ {{ rep }}</span>
    </div>
    <div class="exp-track">
      <div class="exp-fill" :style="{ width: pct + '%' }"></div>
    </div>
    <span class="exp-text">{{ currentExp }}/{{ nextLevelExp }} EXP</span>
  </div>
</template>
<script setup>
import { computed } from 'vue'
const props = defineProps({
  level: { type: Number, default: 1 },
  currentExp: { type: Number, default: 0 },
  nextLevelExp: { type: Number, default: 1000 },
  rep: { type: Number, default: undefined },
})
const pct = computed(() => Math.min(100, Math.round((props.currentExp / props.nextLevelExp) * 100)))
</script>
<style scoped lang="scss">
.exp-bar { padding: 8px 0; }
.exp-bar-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 4px; }
.exp-level { font-size: 0.85rem; font-weight: 700; color: var(--primary); }
.exp-rep { font-size: 0.75rem; color: #f59e0b; }
.exp-track { height: 6px; background: var(--bg-secondary); border-radius: 99px; overflow: hidden; margin-bottom: 2px; }
.exp-fill { height: 100%; border-radius: 99px; background: linear-gradient(90deg, var(--primary), #8b5cf6); transition: width 0.5s ease; }
.exp-text { font-size: 0.68rem; color: var(--text-muted); }
</style>
