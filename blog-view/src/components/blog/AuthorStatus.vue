<template>
  <div class="author-status" v-if="status">
    <div class="status-header">
      <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 20h9"/><path d="M16.5 3.5a2.121 2.121 0 0 1 3 3L7 19l-4 1 1-4L16.5 3.5z"/></svg>
      <span>Status</span>
    </div>
    <p class="status-content">{{ status.content }}</p>
    <div class="status-poll" v-if="status.type === 'POLL' && status.pollOptions">
      <div v-for="opt in parsedOptions" :key="opt.id" class="poll-option">
        <div class="poll-bar" :style="{ width: pollPct(opt.votes) + '%' }"></div>
        <span class="poll-text">{{ opt.text }}</span>
        <span class="poll-votes">{{ opt.votes }}</span>
      </div>
    </div>
    <span class="status-time">{{ timeAgo(status.createdAt) }}</span>
  </div>
</template>
<script setup>
import { computed } from 'vue'
import dayjs from 'dayjs'
const props = defineProps({ status: Object })
const parsedOptions = computed(() => {
  if (!props.status?.pollOptions) return []
  try { return JSON.parse(props.status.pollOptions) } catch { return [] }
})
const pollPct = (votes) => {
  const total = parsedOptions.value.reduce((s, o) => s + o.votes, 0)
  return total ? (votes / total) * 100 : 0
}
const timeAgo = (d) => dayjs(d).fromNow()
</script>
<style scoped lang="scss">
.author-status { padding: 12px; background: var(--bg-secondary); border-radius: var(--radius-lg); }
.status-header { display: flex; align-items: center; gap: 6px; font-size: 0.75rem; font-weight: 600; color: var(--text-muted); text-transform: uppercase; margin-bottom: 8px; }
.status-header svg { width: 14px; height: 14px; }
.status-content { font-size: 0.85rem; color: var(--text-secondary); line-height: 1.5; margin-bottom: 8px; }
.status-poll { display: flex; flex-direction: column; gap: 4px; }
.poll-option { position: relative; display: flex; align-items: center; gap: 8px; padding: 4px 8px; }
.poll-bar { position: absolute; left: 0; top: 0; height: 100%; background: var(--primary-50); border-radius: var(--radius-sm); transition: width 0.5s ease; }
.poll-text { position: relative; font-size: 0.78rem; flex: 1; }
.poll-votes { position: relative; font-size: 0.7rem; color: var(--text-muted); }
.status-time { font-size: 0.7rem; color: var(--text-muted); }
</style>
