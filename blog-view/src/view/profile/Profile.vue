<template>
  <div class="profile-page">
    <div class="profile-header">
      <div class="profile-cover"></div>
      <div class="profile-info">
        <el-avatar :size="96" :src="profile?.avatarUrl" class="profile-avatar">{{ profile?.displayName?.charAt(0) || 'U' }}</el-avatar>
        <h1 class="profile-name">{{ profile?.displayName }}</h1>
        <p class="profile-bio" v-if="profile?.bio">{{ profile.bio }}</p>
        <div class="profile-stats">
          <div class="stat"><span class="stat-value">{{ profile?.blogCount || 0 }}</span><span class="stat-label">Bài viết</span></div>
          <div class="stat"><span class="stat-value">{{ profile?.followerCount || 0 }}</span><span class="stat-label">Theo dõi</span></div>
          <div class="stat"><span class="stat-value">{{ profile?.followingCount || 0 }}</span><span class="stat-label">Đang follow</span></div>
        </div>
        <div class="profile-badges" v-if="profile?.level">
          <el-tag type="info">Lv.{{ profile.level }}</el-tag>
          <el-tag type="warning" v-if="profile?.isCreator">Creator</el-tag>
          <el-tag type="danger" v-if="profile?.role === 'ADMIN'">Admin</el-tag>
        </div>
        <div class="profile-actions">
          <el-button v-if="isOwnProfile" type="primary" @click="editProfile" round>
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/><path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/></svg>
            Chỉnh sửa hồ sơ
          </el-button>
          <el-button v-else :type="isFollowing ? 'default' : 'primary'" @click="toggleFollow" round>
            <svg v-if="isFollowing" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><line x1="19" y1="8" x2="19" y2="14"/><line x1="16" y1="11" x2="22" y2="11"/></svg>
            <svg v-else width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M22 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/></svg>
            {{ isFollowing ? 'Đang follow' : 'Follow' }}
          </el-button>
        </div>
      </div>
    </div>

    <div class="profile-activity-card">
      <div class="activity-header">
        <div class="activity-title">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="23 6 13.5 15.5 8.5 10.5 1 18"/><polyline points="17 6 23 6 23 12"/></svg>
          <span>Hoạt động 30 ngày</span>
        </div>
        <span class="activity-badge">+{{ totalActivity }} lượt xem</span>
      </div>
      <div class="activity-chart">
        <EChart :option="activityOption" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useAuthStore } from '@/store/auth'
import { profileApi } from '@/api'
import EChart from '@/components/EChart.vue'

const route = useRoute()
const authStore = useAuthStore()
const profile = ref(null)
const isFollowing = ref(false)
const isOwnProfile = computed(() => authStore.user?.id === Number(route.params.userId))
const totalActivity = ref(0)

const toggleFollow = async () => { isFollowing.value = !isFollowing.value }
const editProfile = () => {}

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
    data: Array.from({ length: 30 }, (_, i) => `${i + 1}`),
    axisLine: { show: false },
    axisTick: { show: false },
    axisLabel: {
      color: 'var(--text-muted)',
      fontSize: 10,
      interval: 4
    }
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
    data: Array.from({ length: 30 }, () => Math.floor(Math.random() * 80 + 10)),
    lineStyle: {
      width: 2.5,
      color: '#fb7293'
    },
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
    const res = await profileApi.getPublic(route.params.userId)
    profile.value = res.data
    totalActivity.value = Math.floor(Math.random() * 5000 + 1000)
  } catch (e) { console.error(e) }
})
</script>

<style scoped lang="scss">
.profile-header {
  background: var(--surface);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow);
  overflow: hidden;
  margin-bottom: var(--space-lg);
  border: 1px solid var(--border-light);
}

.profile-cover {
  height: 200px;
  background: linear-gradient(135deg, var(--primary), #8b5cf6 50%, #ec4899);
  position: relative;

  &::after {
    content: '';
    position: absolute;
    inset: 0;
    background: linear-gradient(180deg, transparent 50%, rgba(0,0,0,0.2));
  }
}

.profile-info {
  padding: 0 var(--space-xl) var(--space-xl);
  text-align: center;
  margin-top: -48px;
  position: relative;
  z-index: 1;
}

.profile-avatar {
  border: 6px solid var(--surface);
  margin-bottom: var(--space-md);
  box-shadow: var(--shadow-md);
}

.profile-name {
  font-size: 1.5rem;
  font-weight: 800;
  margin-bottom: 4px;
  letter-spacing: -0.02em;
}

.profile-bio {
  color: var(--text-secondary);
  margin-bottom: var(--space-md);
  font-size: 0.95rem;
}

.profile-stats {
  display: flex;
  justify-content: center;
  gap: var(--space-2xl);
  margin-bottom: var(--space-md);
}

.stat {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-value {
  font-size: 1.25rem;
  font-weight: 700;
}

.stat-label {
  font-size: 0.8rem;
  color: var(--text-muted);
}

.profile-badges {
  display: flex;
  justify-content: center;
  gap: var(--space-sm);
  margin-bottom: var(--space-md);
}

.profile-actions {
  display: flex;
  justify-content: center;
  gap: var(--space-sm);

  .el-button svg {
    margin-right: 4px;
  }
}

.profile-activity-card {
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
</style>
