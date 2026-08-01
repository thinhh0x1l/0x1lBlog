<template>
  <div class="notification-item" :class="{ unread: !notification.isRead }" @click="$emit('click', notification)">
    <div class="notif-icon" :class="typeClass">
      <svg v-if="notification.type === 'COMMENT'" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/></svg>
      <svg v-else-if="notification.type === 'FOLLOW'" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M16 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="8.5" cy="7" r="4"/><line x1="20" y1="8" x2="20" y2="14"/><line x1="23" y1="11" x2="17" y2="11"/></svg>
      <svg v-else width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"/><path d="M13.73 21a2 2 0 0 1-3.46 0"/></svg>
    </div>
    <div class="notif-content">
      <p class="notif-text">{{ notification.message }}</p>
      <span class="notif-time">{{ fromNow(notification.createdAt) }}</span>
    </div>
    <div v-if="!notification.isRead" class="unread-dot"></div>
  </div>
</template>

<script setup lang="ts">
import { fromNow } from '~/utils/time'

defineProps({
  notification: { type: Object, required: true },
})

defineEmits(['click'])

const typeClass = computed(() => '')
</script>

<style scoped lang="scss">
.notification-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 12px 16px;
  cursor: pointer;
  transition: background 0.15s ease;
  position: relative;

  &:hover {
    background: var(--surface-hover);
  }

  &.unread {
    background: var(--primary-50);
  }
}

.notif-icon {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--bg-secondary);
  color: var(--text-muted);
  flex-shrink: 0;
}

.notif-content {
  flex: 1;
  min-width: 0;
}

.notif-text {
  font-size: 0.85rem;
  color: var(--text-primary);
  margin: 0 0 4px 0;
  line-height: 1.4;
}

.notif-time {
  font-size: 0.72rem;
  color: var(--text-muted);
}

.unread-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: var(--primary);
  flex-shrink: 0;
}
</style>
