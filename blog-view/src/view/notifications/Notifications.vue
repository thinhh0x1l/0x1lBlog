<template>
  <div class="notifications-page">
    <div class="page-header">
      <h1>Thông báo</h1>
      <button class="mark-all-btn" @click="markAllRead">Đánh dấu đã đọc tất cả</button>
    </div>
    <div class="notif-list">
      <div v-for="notif in notifications" :key="notif.id" :class="['notif-item', { unread: !notif.isRead }]" @click="markRead(notif)">
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
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { notificationApi } from '@/api'
import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'
import 'dayjs/locale/vi'
dayjs.extend(relativeTime)
dayjs.locale('vi')

const notifications = ref([])
const formatDate = (d) => dayjs(d).fromNow()
const loadNotifications = async () => { try { const res = await notificationApi.getAll(); notifications.value = res.data || [] } catch (e) {} }
const markRead = (notif) => { notif.isRead = true }
const markAllRead = () => { notifications.value.forEach(n => n.isRead = true) }
onMounted(loadNotifications)
</script>

<style scoped lang="scss">
.notifications-page { max-width: 700px; margin: 0 auto; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: var(--space-lg); }
.page-header h1 { font-size: 1.5rem; font-weight: 700; }
.mark-all-btn { padding: 8px 16px; border: 1px solid var(--border); background: var(--surface); border-radius: var(--radius-md); font-size: 0.85rem; cursor: pointer; transition: all var(--duration-fast) ease; }
.mark-all-btn:hover { border-color: var(--primary); color: var(--primary); }
.notif-list { display: flex; flex-direction: column; gap: var(--space-sm); }
.notif-item { display: flex; align-items: center; gap: var(--space-md); padding: var(--space-md); background: var(--surface); border: 1px solid var(--border-light); border-radius: var(--radius-lg); cursor: pointer; transition: all var(--duration-fast) ease; }
.notif-item:hover { border-color: var(--border); }
.notif-item.unread { background: var(--primary-50); border-color: var(--primary-100); }
.notif-avatar { width: 40px; height: 40px; border-radius: 50%; overflow: hidden; flex-shrink: 0; }
.notif-avatar img { width: 100%; height: 100%; object-fit: cover; }
.notif-avatar-placeholder { width: 100%; height: 100%; display: flex; align-items: center; justify-content: center; background: var(--bg-secondary); font-size: 1.1rem; }
.notif-content { flex: 1; }
.notif-text { font-size: 0.9rem; line-height: 1.4; }
.notif-time { font-size: 0.78rem; color: var(--text-muted); }
.notif-dot { width: 8px; height: 8px; background: var(--primary); border-radius: 50%; flex-shrink: 0; }
</style>
