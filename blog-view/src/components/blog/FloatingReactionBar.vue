<template>
  <div class="floating-reaction-bar" v-if="visible">
    <button v-for="r in reactions" :key="r.type" :class="['reaction-btn', { active: userReaction === r.type }]" @click="$emit('react', r.type)">
      {{ r.icon }}
    </button>
    <div class="floating-actions">
      <button @click="$emit('bookmark')"><svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M19 21l-7-5-7 5V5a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2z"/></svg></button>
      <button @click="$emit('share')"><svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M4 12v8a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2v-8"/><polyline points="16 6 12 2 8 6"/><line x1="12" y1="2" x2="12" y2="15"/></svg></button>
    </div>
  </div>
</template>
<script setup>
defineProps({ visible: Boolean, reactions: Array, userReaction: String })
defineEmits(['react', 'bookmark', 'share'])
</script>
<style scoped lang="scss">
.floating-reaction-bar { position: fixed; bottom: 0; left: 0; right: 0; z-index: 100; background: var(--surface); border-top: 1px solid var(--border); padding: 8px 16px; display: flex; align-items: center; justify-content: space-around; box-shadow: 0 -2px 10px rgba(0,0,0,0.08); }
.reaction-btn { background: none; border: none; font-size: 1.3rem; cursor: pointer; padding: 4px; transition: transform 0.15s; }
.reaction-btn:hover { transform: scale(1.2); }
.reaction-btn.active { transform: scale(1.3); }
.floating-actions { display: flex; gap: 12px; }
.floating-actions button { background: none; border: none; cursor: pointer; color: var(--text-secondary); padding: 4px; }
</style>
