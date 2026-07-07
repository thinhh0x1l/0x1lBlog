<template>
  <div class="sc-card" @click="$emit('click')">
    <div class="sc-ring" :class="{ viewed: isViewed, own: isOwn }">
      <img v-if="group.userAvatar" :src="group.userAvatar" class="sc-avatar" />
      <div v-else class="sc-avatar-placeholder">{{ group.userName?.charAt(0) || '?' }}</div>
    </div>
    <span class="sc-name">{{ group.userName }}</span>
  </div>
</template>

<script setup>
defineProps({ group: { type: Object, required: true }, isViewed: { type: Boolean, default: false }, isOwn: { type: Boolean, default: false } })
defineEmits(['click'])
</script>

<style scoped lang="scss">
.sc-card { display: flex; flex-direction: column; align-items: center; gap: 4px; cursor: pointer; flex-shrink: 0; width: 64px; }
.sc-ring { width: 56px; height: 56px; border-radius: 50%; padding: 2px; background: linear-gradient(135deg, #0ea5e9, #8b5cf6); display: flex; align-items: center; justify-content: center; }
.sc-ring.viewed { background: #475569; }
.sc-ring.own { background: linear-gradient(135deg, #f59e0b, #ec4899); }
.sc-avatar { width: 100%; height: 100%; border-radius: 50%; object-fit: cover; border: 2px solid var(--surface, #fff); }
.sc-avatar-placeholder { width: 100%; height: 100%; border-radius: 50%; background: #334155; display: flex; align-items: center; justify-content: center; color: #fff; font-weight: 700; font-size: 1.1rem; border: 2px solid var(--surface, #fff); }
.sc-name { font-size: 0.65rem; color: var(--text-muted, #94a3b8); max-width: 64px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; text-align: center; }
</style>
