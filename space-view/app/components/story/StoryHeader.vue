<template>
  <div class="sh-row">
    <img :src="story.userAvatar || 'https://api.dicebear.com/7.x/avataaars/svg?seed=guest'" class="sh-avatar" />
    <div class="sh-meta">
      <span class="sh-name">{{ story.userName }}</span>
      <span class="sh-time">{{ fromNow(story.createdAt) }}</span>
    </div>
    <button class="sh-close" @click="$emit('close')">✕</button>
  </div>
</template>

<script setup lang="ts">
import { fromNow } from '~/utils/time'
defineProps({ story: { type: Object, required: true } })
defineEmits(['close'])
</script>

<style scoped lang="scss">
.sh-row { display: flex; align-items: center; gap: 10px; padding: 0 4px; }
.sh-avatar { width: 32px; height: 32px; border-radius: 50%; object-fit: cover; border: 2px solid rgba(255,255,255,0.3); }
.sh-meta { flex: 1; min-width: 0; display: flex; flex-direction: column; }
.sh-name { font-size: 0.82rem; font-weight: 600; color: #fff; }
.sh-time { font-size: 0.65rem; color: rgba(255,255,255,0.55); }
.sh-close { background: none; border: none; color: rgba(255,255,255,0.7); font-size: 1.4rem; cursor: pointer; padding: 4px; line-height: 1; }
.sh-close:hover { color: #fff; }
</style>
