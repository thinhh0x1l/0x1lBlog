<template>
  <OneColumnLayout>
    <div class="notifications-page">
      <div class="page-header">
        <h1>Thông báo</h1>
        <button class="mark-all-btn" @click="markAllRead">Đánh dấu đã đọc tất cả</button>
      </div>
      <div class="notif-list">
        <div v-for="notif in notifications" :key="notif.id" :class="['notif-item', { 'notif-item--unread': !notif.isRead }]" @click="markRead(notif)">
          <div class="notif-avatar">
            <img v-if="notif.actorAvatar" :src="notif.actorAvatar" />
            <div v-else class="notif-avatar-placeholder">
              <span v-if="notif.type === 'NEW_COMMENT'">💬</span>
              <span v-else-if="notif.type === 'LIKE_BLOG'">❤️</span>
              <span v-else-if="notif.type === 'NEW_FOLLOWER'">👤</span>
              <span v-else-if="notif.type === 'BADGE_AWARD'">🏆</span>
              <span v-else-if="notif.type === 'NEW_BLOG'">📝</span>
              <span v-else-if="notif.type === 'NEW_REPLY'">↩️</span>
              <span v-else-if="notif.type === 'LIKE_COMMENT'">👍</span>
              <span v-else-if="notif.type === 'SERIES_NEW_POST'">📚</span>
              <span v-else-if="notif.type === 'MENTION'">@</span>
              <span v-else>🔔</span>
            </div>
          </div>
          <div class="notif-content">
            <p class="notif-text"><strong>{{ notif.actorName }}</strong> {{ notif.message }}</p>
            <span class="notif-time">{{ formatDate(notif.createdAt) }}</span>
          </div>
          <div class="notif-dot" v-if="!notif.isRead"></div>
        </div>
      </div>
      <el-empty v-if="notifications.length === 0" description="Chưa có thông báo nào" />
    </div>
  </OneColumnLayout>
</template>
<script setup>
import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'
import 'dayjs/locale/vi'

dayjs.extend(relativeTime)
dayjs.locale('vi')

definePageMeta({ layout: 'default', ssr: false })
useHead({ title: 'Thông báo - 0x1lBlog' })

const notifications = ref([])
const formatDate = (d) => dayjs(d).fromNow()
const markRead = (n) => { n.isRead = true }
const markAllRead = () => { notifications.value.forEach(n => n.isRead = true) }
</script>
<style scoped lang="scss">
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-header h1 { font-size: 1.25rem; font-weight: 700; color: var(--text-primary); }
.mark-all-btn { background: none; border: none; color: var(--primary); font-size: 0.85rem; cursor: pointer; font-weight: 500; }
.notif-list { display: flex; flex-direction: column; gap: 4px; }
.notif-item {
  display: flex; gap: 12px; padding: 14px 16px;
  background: var(--surface); border: 1px solid var(--border-light); border-radius: 8px;
  cursor: pointer; transition: all 0.12s;
  &:hover { border-color: var(--border); }
  &.notif-item--unread { border-left: 3px solid var(--primary); background: var(--primary-50); }
}
.notif-avatar { width: 40px; height: 40px; flex-shrink: 0; }
.notif-avatar img { width: 40px; height: 40px; border-radius: 50%; object-fit: cover; }
.notif-avatar-placeholder {
  width: 40px; height: 40px; border-radius: 50%; background: var(--bg-secondary);
  display: flex; align-items: center; justify-content: center; font-size: 1.1rem;
}
.notif-content { flex: 1; min-width: 0; }
.notif-text { font-size: 0.88rem; color: var(--text-primary); margin: 0; line-height: 1.4; }
.notif-time { font-size: 0.75rem; color: var(--text-muted); margin-top: 2px; display: block; }
.notif-dot { width: 8px; height: 8px; border-radius: 50%; background: var(--primary); flex-shrink: 0; align-self: center; }
</style>
