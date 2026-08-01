<template>
  <ProfileLayout>
    <template #cover>
      <CanvasCover :canvas="profileData?.canvas" :is-own="isOwnProfile" @viewCanvas="openCanvas" />
    </template>
    <template #sidebar>
      <ProfileInfo
        :user="profileData?.user"
        :level="profileData?.level"
        :current-exp="profileData?.currentExp"
        :next-level-exp="profileData?.nextLevelExp"
        :rep-score="profileData?.repScore"
        :streak="profileData?.streak"
        :is-own="isOwnProfile"
        :is-following="isFollowing"
        :game-mode="gameMode"
        @edit-profile="editProfile"
        @edit-layout="editLayout"
        @toggle-game-mode="toggleGameMode"
        @follow="toggleFollow"
      />
    </template>

    <div class="profile-section" v-if="gameMode">
      <GameModeSheet
        :game-mode="gameMode"
        :user="profileData?.user"
        :level="profileData?.level"
        :current-exp="profileData?.currentExp"
        :next-level-exp="profileData?.nextLevelExp"
        :badges="profileData?.badges"
      />
    </div>

    <div class="profile-section" v-if="profileData">
      <WidgetGrid :profile-data="profileData" :is-own="isOwnProfile" />
    </div>

    <div class="profile-section">
      <div class="activity-card">
        <div class="activity-header">
          <div class="activity-title">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="23 6 13.5 15.5 8.5 10.5 1 18"/><polyline points="17 6 23 6 23 12"/></svg>
            <span>Hoạt động 30 ngày</span>
          </div>
          <span class="activity-badge">+{{ totalActivity }} rep</span>
        </div>
        <div class="activity-chart">
          <EChart :option="activityOption" />
        </div>
      </div>
    </div>

    <div class="profile-footer">
      <span class="member-since">Tham gia từ {{ profileData?.user?.createdAt ? new Date(profileData.user.createdAt).toLocaleDateString('vi-VN') : '...' }}</span>
    </div>
  </ProfileLayout>
  <CanvasViewer v-if="canvasViewerVisible" :canvas="viewingCanvas" :initial-strokes="[]" @close="canvasViewerVisible = false" @stroke="handleCanvasStroke" />
</template>

<script setup>
import { ref, computed } from 'vue'
import { users as mockUsers, blogs as mockBlogs } from '~/utils/dummy'

definePageMeta({ layout: 'default' })

const route = useRoute()
const userId = Number(route.params.userId)
const mockUser = mockUsers.find(u => u.id === userId)
const userBlogs = mockBlogs.filter(b => b.authorId === userId && b.status === 'PUBLISHED')

const profileData = ref(mockUser ? {
  user: mockUser,
  level: 5,
  currentExp: 320,
  nextLevelExp: 500,
  repScore: 1250,
  streak: 7,
  badges: [],
  reputationHistory: Array.from({ length: 30 }, (_, i) => ({
    date: `2026-07-${String(30 - i).padStart(2, '0')}`,
    points: Math.floor(Math.random() * 50) + 10
  })),
  canvas: null,
  blogs: userBlogs
} : null)

const isFollowing = ref(false)
const gameMode = ref(false)
const canvasViewerVisible = ref(false)
const viewingCanvas = ref(null)

const isOwnProfile = computed(() => {
  const authUser = useAuthStore().user
  return authUser?.id === userId
})

useHead({ title: `${profileData.value?.user?.displayName || 'Profile'} - 0x1lBlog` })

const openCanvas = (c) => { viewingCanvas.value = c; canvasViewerVisible.value = true }
const handleCanvasStroke = (stroke) => {}
const toggleFollow = async () => { isFollowing.value = !isFollowing.value }
const editProfile = () => {}
const editLayout = () => {}
const toggleGameMode = () => { gameMode.value = !gameMode.value }

const totalActivity = computed(() => {
  const h = profileData.value?.reputationHistory
  return h ? h.reduce((s, d) => s + d.points, 0) : 0
})

const activityOption = computed(() => ({
  tooltip: { trigger: 'axis' },
  grid: { top: 16, right: 8, bottom: 24, left: 40 },
  xAxis: {
    type: 'category',
    data: (profileData.value?.reputationHistory || []).map(d => d.date.split('-')[2]),
    axisLine: { show: false },
    axisTick: { show: false },
    axisLabel: { color: '#94a3b8', fontSize: 10, interval: 4 }
  },
  yAxis: {
    type: 'value',
    splitLine: { lineStyle: { color: '#f1f5f9', type: 'dashed' } },
    axisLabel: { color: '#94a3b8', fontSize: 10 },
    axisLine: { show: false },
    axisTick: { show: false }
  },
  series: [{
    type: 'line', smooth: true, showSymbol: false,
    data: (profileData.value?.reputationHistory || []).map(d => d.points),
    lineStyle: { width: 2, color: '#fb7293' },
    areaStyle: {
      color: { type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
        colorStops: [{ offset: 0, color: 'rgba(251,114,147,0.2)' }, { offset: 1, color: 'rgba(251,114,147,0)' }]
      }
    },
    itemStyle: { color: '#32c5e9' }
  }]
}))
</script>

<style scoped lang="scss">
.profile-section { padding: 0 24px; &:last-of-type { padding-bottom: 24px; } }
.activity-card {
  background: var(--surface); border: 1px solid var(--border-light);
  border-radius: 8px; padding: 18px;
}
.activity-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 14px; }
.activity-title {
  display: flex; align-items: center; gap: 8px;
  font-size: 0.9rem; font-weight: 600; color: var(--text-primary);
  svg { color: var(--primary); }
}
.activity-badge {
  padding: 4px 10px; background: rgba(251, 114, 147, 0.1);
  color: #fb7293; border-radius: 10px; font-size: 0.75rem; font-weight: 600;
}
.activity-chart { height: 220px; }
.profile-footer { padding: 16px 24px; text-align: center; }
.member-since { font-size: 0.78rem; color: var(--text-muted); }
</style>
