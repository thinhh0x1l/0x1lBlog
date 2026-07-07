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
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="23 6 13.5 15.5 8.5 10.5 1 18"/><polyline points="17 6 23 6 23 12"/></svg>
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
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useAuthStore } from '@/store/auth'
import { profileExtApi } from '@/api/profile-ext'
import EChart from '@/components/EChart.vue'
import CanvasCover from '@/components/profile/CanvasCover.vue'
import CanvasViewer from '@/components/canvas/CanvasViewer.vue'
import ProfileInfo from '@/components/profile/ProfileInfo.vue'
import WidgetGrid from '@/components/profile/WidgetGrid.vue'
import GameModeSheet from '@/components/profile/GameModeSheet.vue'
import ProfileLayout from '@/components/layouts/ProfileLayout.vue'

const route = useRoute()
const authStore = useAuthStore()
const profileData = ref(null)
const isFollowing = ref(false)
const isOwnProfile = computed(() => authStore.user?.id === Number(route.params.userId))
const gameMode = ref(false)
const canvasViewerVisible = ref(false)
const viewingCanvas = ref(null)

const openCanvas = (canvas) => { viewingCanvas.value = canvas; canvasViewerVisible.value = true }
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
  tooltip: {
    trigger: 'axis',
    backgroundColor: 'rgba(255,255,255,0.95)',
    borderColor: 'var(--border)',
    borderWidth: 1,
    textStyle: { fontSize: 12, color: 'var(--text-primary)' }
  },
  grid: { top: 16, right: 8, bottom: 24, left: 40 },
  xAxis: {
    type: 'category',
    data: (profileData.value?.reputationHistory || []).map(d => {
      const parts = d.date.split('-')
      return parts[2]
    }),
    axisLine: { show: false },
    axisTick: { show: false },
    axisLabel: { color: 'var(--text-muted)', fontSize: 10, interval: 4 }
  },
  yAxis: {
    type: 'value',
    splitLine: { lineStyle: { color: 'var(--border-light)', type: 'dashed' } },
    axisLabel: { color: 'var(--text-muted)', fontSize: 10 },
    axisLine: { show: false },
    axisTick: { show: false }
  },
  series: [{
    type: 'line',
    smooth: true,
    showSymbol: false,
    data: (profileData.value?.reputationHistory || []).map(d => d.points),
    lineStyle: { width: 2.5, color: '#fb7293' },
    areaStyle: {
      color: {
        type: 'linear',
        x: 0, y: 0, x2: 0, y2: 1,
        colorStops: [
          { offset: 0, color: 'rgba(251,114,147,0.3)' },
          { offset: 1, color: 'rgba(251,114,147,0)' }
        ]
      }
    },
    itemStyle: { color: '#32c5e9' },
    markPoint: {
      data: [{ type: 'max', name: 'Cao nhất' }],
      symbol: 'circle',
      symbolSize: 40,
      label: { fontSize: 10, fontWeight: 600, color: '#fff' }
    }
  }]
}))

onMounted(async () => {
  try {
    const res = await profileExtApi.getProfileData(route.params.userId)
    profileData.value = res.data
  } catch (e) { console.error(e) }
})
</script>

<style scoped lang="scss">
.profile-section {
  padding: 0 24px;

  &:last-of-type {
    padding-bottom: 24px;
  }
}

.activity-card {
  background: var(--surface);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-xl);
  padding: var(--space-lg);
  box-shadow: var(--shadow-sm);
  transition: all var(--duration-normal) var(--ease-out);

  &:hover {
    border-color: var(--border);
    box-shadow: var(--shadow-md);
  }
}

.activity-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--space-md);
}

.activity-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.95rem;
  font-weight: 700;
  color: var(--text-primary);

  svg { color: var(--primary); }
}

.activity-badge {
  display: inline-flex;
  align-items: center;
  padding: 4px 10px;
  background: rgba(251, 114, 147, 0.1);
  color: #fb7293;
  border-radius: var(--radius-full);
  font-size: 0.78rem;
  font-weight: 600;
}

.activity-chart {
  height: 240px;
}

.profile-footer {
  padding: 16px 24px;
  text-align: center;

  .member-since {
    font-size: 0.78rem;
    color: var(--text-muted);
  }
}
</style>
