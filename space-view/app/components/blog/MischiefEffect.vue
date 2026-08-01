<template>
  <div v-if="mischief" class="mischief-effect" :class="mischief.type?.toLowerCase()">
    <span class="mischief-icon">{{ icons[mischief.type] || '💀' }}</span>
    <span class="mischief-label">Bị {{ mischief.type?.toLowerCase() === 'slime' ? 'slime' : 'phá' }} bởi {{ mischief.appliedBy }}</span>
  </div>
</template>
<script setup lang="ts">
defineProps({ mischief: Object })
const icons: Record<string, string> = { SLIME: '🫠', EGGS: '🥚', SPAM: '📧', TRASH: '🗑️', GHOST: '👻' }
</script>
<style scoped lang="scss">
.mischief-effect { display: flex; align-items: center; gap: 6px; padding: 4px 10px; background: linear-gradient(135deg, #86efac, #a7f3d0); border-radius: var(--radius-full); font-size: 0.7rem; margin-bottom: 8px; animation: pulse-mischief 2s infinite; }
.mischief-effect.slime { background: linear-gradient(135deg, #86efac, #a7f3d0); }
.mischief-icon { font-size: 0.9rem; }
.mischief-label { color: #166534; font-weight: 500; }
@keyframes pulse-mischief { 0%, 100% { transform: scale(1); } 50% { transform: scale(1.02); } }
</style>
