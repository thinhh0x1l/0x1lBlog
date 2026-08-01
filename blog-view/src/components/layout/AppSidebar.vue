<template>
  <aside class="sidebar">
    <!-- Profile Card -->
    <div class="sidebar-card profile-card" v-if="isLoggedIn">
      <div class="profile-cover"></div>
      <div class="profile-content">
        <el-avatar :size="72" :src="user?.avatarUrl" class="profile-avatar">{{ user?.displayName?.charAt(0) || 'U' }}</el-avatar>
        <h3 class="profile-name">{{ user?.displayName }}</h3>
        <div class="profile-bio" v-if="user?.bio">{{ user.bio }}</div>
        <div class="profile-stats">
          <div class="stat"><span class="stat-value">{{ user?.blogCount || 0 }}</span><span class="stat-label">Bài viết</span></div>
          <div class="stat"><span class="stat-value">{{ user?.followerCount || 0 }}</span><span class="stat-label">Theo dõi</span></div>
          <div class="stat"><span class="stat-value">{{ user?.followingCount || 0 }}</span><span class="stat-label">Đang follow</span></div>
        </div>
        <div class="profile-level" v-if="user?.level">
          <el-tag type="info" size="small">Lv.{{ user.level }}</el-tag>
          <span class="exp-text">{{ user.exp || 0 }} EXP</span>
        </div>
      </div>
    </div>

    <!-- Daily Check-in -->
    <div class="sidebar-card checkin-card" v-if="isLoggedIn">
      <div class="checkin-content">
        <div class="checkin-icon">🎯</div>
        <div class="checkin-info">
          <span class="checkin-title">Điểm danh hàng ngày</span>
          <span class="checkin-streak" v-if="user?.checkinStreak">Streak: {{ user.checkinStreak }} ngày</span>
        </div>
        <el-button type="primary" size="small" round @click="handleCheckin" :loading="checkinLoading">Điểm danh</el-button>
      </div>
    </div>

    <!-- Categories -->
    <div class="sidebar-card">
      <div class="card-header">
        <div class="header-icon" style="background: linear-gradient(135deg, #0ea5e9, #06b6d4)"><el-icon><Grid /></el-icon></div>
        <h3>Danh mục</h3>
      </div>
      <div class="card-body">
        <div class="category-list">
          <router-link v-for="cat in categories" :key="cat.id" :to="`/category/${cat.slug}`" class="category-item">
            <span class="cat-name">{{ cat.name }}</span>
            <span class="cat-count">{{ cat.blogCount }}</span>
          </router-link>
        </div>
      </div>
    </div>

    <!-- Trending Tags -->
    <div class="sidebar-card">
      <div class="card-header">
        <div class="header-icon" style="background: linear-gradient(135deg, #f97316, #f59e0b)"><el-icon><TrendCharts /></el-icon></div>
        <h3>Tag phổ biến</h3>
      </div>
      <div class="card-body">
        <div class="tag-cloud">
          <router-link v-for="tag in tags" :key="tag.id" :to="`/tag/${tag.id}`" class="tag-item">#{{ tag.name }}</router-link>
        </div>
      </div>
    </div>

    <!-- Trending Posts -->
    <div class="sidebar-card">
      <div class="card-header">
        <div class="header-icon" style="background: linear-gradient(135deg, #ec4899, #f43f5e)"><el-icon><Star /></el-icon></div>
        <h3>Xu hướng</h3>
      </div>
      <div class="card-body">
        <div class="trending-list">
          <router-link v-for="(post, idx) in trending" :key="post.id" :to="`/blog/${post.id}`" class="trending-item">
            <span class="trend-rank" :class="`rank-${idx + 1}`">{{ idx + 1 }}</span>
            <div class="trend-info">
              <span class="trend-title">{{ post.title }}</span>
              <span class="trend-meta">{{ post.views }} lượt xem · {{ post.readTime }} phút đọc</span>
            </div>
          </router-link>
        </div>
      </div>
    </div>

    <!-- Badges -->
    <div class="sidebar-card" v-if="isLoggedIn && userBadges.length">
      <div class="card-header">
        <div class="header-icon" style="background: linear-gradient(135deg, #eab308, #f59e0b)"><el-icon><Medal /></el-icon></div>
        <h3>Huy hiệu</h3>
      </div>
      <div class="card-body">
        <div class="badge-list">
          <div v-for="badge in userBadges" :key="badge.id" class="badge-item" :class="badge.tier.toLowerCase()">
            <span class="badge-icon">{{ badge.icon || '🏆' }}</span>
            <span class="badge-name">{{ badge.displayName }}</span>
          </div>
        </div>
      </div>
    </div>
  </aside>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { Grid, TrendCharts, Star, Medal } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/store/auth'
import { categoryApi, hashtagApi, blogApi } from '@/api'

const authStore = useAuthStore()
const isLoggedIn = computed(() => authStore.isLoggedIn)
const user = computed(() => authStore.user)

const categories = ref([])
const tags = ref([])
const trending = ref([])
const userBadges = ref([])
const checkinLoading = ref(false)

const handleCheckin = async () => {
  checkinLoading.value = true
  setTimeout(() => { ElMessage.success('Điểm danh thành công! +10 EXP'); checkinLoading.value = false }, 1000)
}

onMounted(async () => {
  try {
    const [catRes, tagRes, blogRes] = await Promise.all([categoryApi.getAll(), hashtagApi.getTop(10), blogApi.trending(5)])
    categories.value = catRes.data || []
    tags.value = tagRes.data || []
    trending.value = blogRes.data || []
  } catch (e) { console.error(e) }
})
</script>

<style scoped lang="scss">
.sidebar { width: var(--sidebar-width); flex-shrink: 0; display: flex; flex-direction: column; gap: var(--space-md); position: sticky; top: calc(var(--header-height) + var(--space-lg)); height: fit-content; }
@media (max-width: 1024px) { .sidebar { width: 100%; position: static; } }
.sidebar-card { background: var(--surface); border-radius: var(--radius-lg); box-shadow: var(--shadow); overflow: hidden; }
.card-header { display: flex; align-items: center; gap: var(--space-sm); padding: var(--space-md) var(--space-md) 0; }
.header-icon { width: 28px; height: 28px; border-radius: var(--radius-sm); display: flex; align-items: center; justify-content: center; color: white; font-size: 0.875rem; }
.card-header h3 { font-size: 0.95rem; font-weight: 600; }
.card-body { padding: var(--space-md); }

/* Profile */
.profile-card { position: relative; }
.profile-cover { height: 80px; background: linear-gradient(135deg, var(--primary), #8b5cf6 50%, #ec4899); }
.profile-content { padding: 0 var(--space-md) var(--space-md); text-align: center; }
.profile-avatar { margin: -36px auto var(--space-sm); border: 4px solid var(--surface); }
.profile-name { font-size: 1rem; font-weight: 600; margin-bottom: 4px; }
.profile-bio { font-size: 0.8rem; color: var(--text-muted); margin-bottom: var(--space-sm); }
.profile-stats { display: flex; justify-content: center; gap: var(--space-xl); margin-bottom: var(--space-sm); }
.stat { display: flex; flex-direction: column; align-items: center; }
.stat-value { font-size: 1.125rem; font-weight: 700; }
.stat-label { font-size: 0.75rem; color: var(--text-muted); }
.profile-level { display: flex; align-items: center; justify-content: center; gap: var(--space-sm); }
.exp-text { font-size: 0.75rem; color: var(--text-muted); }

/* Check-in */
.checkin-card { padding: var(--space-md); }
.checkin-content { display: flex; align-items: center; gap: var(--space-sm); }
.checkin-icon { font-size: 1.5rem; }
.checkin-info { flex: 1; }
.checkin-title { display: block; font-size: 0.85rem; font-weight: 600; }
.checkin-streak { font-size: 0.75rem; color: var(--accent); }

/* Categories */
.category-list { display: flex; flex-direction: column; }
.category-item { display: flex; justify-content: space-between; align-items: center; padding: 10px 12px; border-radius: var(--radius); color: var(--text-secondary); text-decoration: none; transition: all var(--duration-fast) ease; }
.category-item:hover { background: var(--surface-hover); color: var(--primary); }
.cat-count { font-size: 0.75rem; padding: 2px 8px; background: var(--bg); border-radius: var(--radius-full); color: var(--text-muted); }

/* Tags */
.tag-cloud { display: flex; flex-wrap: wrap; gap: var(--space-sm); }
.tag-item { padding: 6px 14px; background: var(--bg); border-radius: var(--radius-full); font-size: 0.8rem; font-weight: 500; color: var(--text-secondary); text-decoration: none; transition: all var(--duration-fast) ease; }
.tag-item:hover { background: var(--primary-50); color: var(--primary); }

/* Trending */
.trending-list { display: flex; flex-direction: column; gap: var(--space-sm); }
.trending-item { display: flex; align-items: flex-start; gap: var(--space-sm); padding: 8px; border-radius: var(--radius); text-decoration: none; transition: background var(--duration-fast) ease; }
.trending-item:hover { background: var(--surface-hover); }
.trend-rank { width: 24px; height: 24px; border-radius: var(--radius-sm); display: flex; align-items: center; justify-content: center; font-size: 0.75rem; font-weight: 700; flex-shrink: 0; background: var(--bg); color: var(--text-muted); }
.trend-rank.rank-1 { background: linear-gradient(135deg, #f97316, #f59e0b); color: white; }
.trend-rank.rank-2 { background: linear-gradient(135deg, #8b5cf6, #a78bfa); color: white; }
.trend-rank.rank-3 { background: linear-gradient(135deg, #06b6d4, #22d3ee); color: white; }
.trend-info { flex: 1; min-width: 0; }
.trend-title { display: block; font-size: 0.85rem; font-weight: 500; line-height: 1.4; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.trend-meta { font-size: 0.75rem; color: var(--text-muted); }

/* Badges */
.badge-list { display: flex; flex-wrap: wrap; gap: var(--space-sm); }
.badge-item { display: flex; align-items: center; gap: 4px; padding: 4px 10px; border-radius: var(--radius-full); font-size: 0.75rem; font-weight: 500; }
.badge-item.bronze { background: linear-gradient(135deg, #d97706, #f59e0b); color: white; }
.badge-item.silver { background: linear-gradient(135deg, #9ca3af, #d1d5db); color: #374151; }
.badge-item.gold { background: linear-gradient(135deg, #f59e0b, #eab308); color: white; }
</style>
