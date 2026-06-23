<template>
  <div class="author-story" v-if="stories.length">
    <div class="story-header">
      <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="2"/><path d="M2 12h4M18 12h4M12 2v4M12 18v4"/></svg>
      <span>Story</span>
      <span class="story-expiry">· {{ timeLeft }} còn lại</span>
    </div>
    <div class="story-rings">
      <div v-for="s in stories" :key="s.id" class="story-ring" @click="$emit('viewStory', s)">
        <div class="ring-border">
          <img v-if="s.mediaUrl" :src="s.mediaUrl" class="ring-thumb" />
          <div v-else class="ring-placeholder">📸</div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { computed } from 'vue'
import dayjs from 'dayjs'
const props = defineProps({ stories: { type: Array, default: () => [] } })
defineEmits(['viewStory'])
const timeLeft = computed(() => {
  if (!props.stories.length) return ''
  const expires = dayjs(props.stories[0].expiresAt)
  const hours = expires.diff(dayjs(), 'hour')
  return hours > 0 ? `${hours}h` : 'Sắp hết hạn'
})
</script>
<style scoped lang="scss">
.author-story { padding: 12px; background: var(--bg-secondary); border-radius: var(--radius-lg); }
.story-header { display: flex; align-items: center; gap: 6px; font-size: 0.75rem; font-weight: 600; color: var(--text-muted); text-transform: uppercase; margin-bottom: 10px; }
.story-header svg { width: 14px; height: 14px; }
.story-expiry { text-transform: none; font-weight: 400; color: var(--accent); }
.story-rings { display: flex; gap: 8px; overflow-x: auto; padding-bottom: 4px; }
.story-ring { cursor: pointer; flex-shrink: 0; }
.ring-border { width: 48px; height: 48px; border-radius: 50%; padding: 2px; background: linear-gradient(135deg, #f97316, #ec4899, #8b5cf6); display: flex; align-items: center; justify-content: center; }
.ring-thumb { width: 42px; height: 42px; border-radius: 50%; object-fit: cover; border: 2px solid var(--surface); }
.ring-placeholder { width: 42px; height: 42px; border-radius: 50%; background: var(--surface); display: flex; align-items: center; justify-content: center; font-size: 1.2rem; }
</style>
