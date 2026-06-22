<template>
  <div class="home-3col">
    <!-- LEFT SIDEBAR -->
    <aside class="home-sidebar-left">
      <div class="sidebar-card user-card">
        <div class="user-cover"></div>
        <div class="user-info">
          <div class="user-avatar-wrapper">
            <img v-if="user?.avatarUrl" :src="user.avatarUrl" class="user-avatar" />
            <div v-else class="user-avatar user-avatar-placeholder">{{ user?.displayName?.charAt(0) || 'U' }}</div>
            <span class="status-dot"></span>
          </div>
          <h4 class="user-name">{{ user?.displayName }}</h4>
          <div class="user-stats-row">
            <span><strong>{{ user?.blogCount || 0 }}</strong> bài viết</span>
            <span class="dot">·</span>
            <span><strong>{{ formatCount(user?.followerCount || 0) }}</strong> theo dõi</span>
          </div>
        </div>
      </div>

      <nav class="sidebar-card nav-card">
        <router-link to="/home" class="nav-item active">
          <div class="nav-icon" style="background: linear-gradient(135deg, #0ea5e9, #06b6d4)">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="m3 9 9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/><polyline points="9 22 9 12 15 12 15 22"/></svg>
          </div>
          <span>Trang chủ</span>
        </router-link>
        <router-link to="/trending" class="nav-item">
          <div class="nav-icon" style="background: linear-gradient(135deg, #f97316, #f59e0b)">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="23 6 13.5 15.5 8.5 10.5 1 18"/><polyline points="17 6 23 6 23 12"/></svg>
          </div>
          <span>Xu hướng</span>
        </router-link>
        <router-link to="/bookmarks" class="nav-item">
          <div class="nav-icon" style="background: linear-gradient(135deg, #10b981, #059669)">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M19 21l-7-5-7 5V5a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2z"/></svg>
          </div>
          <span>Đã lưu</span>
        </router-link>
        <router-link to="/notifications" class="nav-item">
          <div class="nav-icon" style="background: linear-gradient(135deg, #ec4899, #f43f5e)">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M6 8a6 6 0 0 1 12 0c0 7 3 9 3 9H3s3-2 3-9"/><path d="M10.3 21a1.94 1.94 0 0 0 3.4 0"/></svg>
          </div>
          <span>Thông báo</span>
          <span class="nav-badge" v-if="unreadCount > 0">{{ unreadCount }}</span>
        </router-link>
        <router-link to="/series" class="nav-item">
          <div class="nav-icon" style="background: linear-gradient(135deg, #8b5cf6, #6366f1)">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M4 19.5v-15A2.5 2.5 0 0 1 6.5 2H20v20H6.5a2.5 2.5 0 0 1 0-5H20"/></svg>
          </div>
          <span>Series</span>
        </router-link>
      </nav>

      <div class="sidebar-card cat-card">
        <h4 class="card-title">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="7" height="7"/><rect x="14" y="3" width="7" height="7"/><rect x="14" y="14" width="7" height="7"/><rect x="3" y="14" width="7" height="7"/></svg>
          Danh mục
        </h4>
        <div class="cat-list">
          <router-link v-for="cat in categories" :key="cat.id" :to="`/category/${cat.slug}`" class="cat-item">
            <span class="cat-icon">{{ cat.icon }}</span>
            <span class="cat-name">{{ cat.name }}</span>
            <span class="cat-count">{{ cat.blogCount }}</span>
          </router-link>
        </div>
      </div>
    </aside>

    <!-- CENTER FEED -->
    <main class="home-feed">
      <div class="feed-tabs">
        <button v-for="tab in tabs" :key="tab.key" :class="['tab', { active: activeTab === tab.key }]" @click="changeTab(tab.key)">
          {{ tab.label }}
        </button>
      </div>

      <div class="trending-bar">
        <div class="trending-scroll">
          <div v-for="(tag, idx) in trendingTags" :key="tag.id" class="trending-chip" @click="$router.push(`/tag/${tag.id}`)">
            <span class="chip-icon" :style="{ color: chipColors[idx % chipColors.length] }">🔥</span>
            <span class="chip-name">#{{ tag.name }}</span>
            <span class="chip-count">{{ formatCount(tag.usageCount) }}</span>
          </div>
        </div>
      </div>

      <div class="stats-card">
        <div class="stats-header">
          <div class="stats-title">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="23 6 13.5 15.5 8.5 10.5 1 18"/><polyline points="17 6 23 6 23 12"/></svg>
            <span>Lượt xem 7 ngày</span>
          </div>
          <span class="stats-badge">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="23 6 13.5 15.5 8.5 10.5 1 18"/><polyline points="17 6 23 6 23 12"/></svg>
            +12.5%
          </span>
        </div>
        <div class="stats-chart">
          <EChart :option="chartOption" />
        </div>
      </div>

      <div class="feed-list">
        <article v-for="(blog, idx) in blogs" :key="blog.id" class="feed-card" :style="{ animationDelay: `${idx * 0.06}s` }">
          <div class="feed-header">
            <router-link :to="`/profile/${blog.authorId}`" class="feed-author">
              <img v-if="blog.authorAvatar" :src="blog.authorAvatar" class="feed-avatar" />
              <div v-else class="feed-avatar feed-avatar-placeholder">{{ blog.authorName?.charAt(0) || 'U' }}</div>
            </router-link>
            <div class="feed-meta">
              <router-link :to="`/profile/${blog.authorId}`" class="author-name">{{ blog.authorName }}</router-link>
              <div class="meta-row">
                <span class="meta-time">{{ formatDate(blog.publishedAt) }}</span>
                <span class="meta-dot">·</span>
                <span class="meta-read">{{ blog.readTime }} phút đọc</span>
              </div>
            </div>
          </div>

          <div class="feed-cover" v-if="blog.coverImage">
            <router-link :to="`/blog/${blog.id}`">
              <img :src="blog.coverImage" :alt="blog.title" loading="lazy" />
            </router-link>
          </div>

          <div class="feed-body">
            <router-link :to="`/blog/${blog.id}`" class="feed-title">{{ blog.title }}</router-link>
            <p class="feed-desc" v-if="blog.description">{{ blog.description }}</p>
          </div>

          <div class="feed-stats">
            <span class="stat-item">
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/></svg>
              {{ formatCount(blog.views) }}
            </span>
            <span class="stat-item">
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/></svg>
              {{ blog.commentCount }}
            </span>
            <span class="stat-item">
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"/></svg>
              {{ blog.likeCount }}
            </span>
          </div>

          <div class="feed-actions">
            <button v-for="r in reactions" :key="r.type" :class="['action-btn', { active: userReaction === r.type }]" @click="react(blog, r.type)">
              <span class="action-icon">{{ r.icon }}</span>
              <span class="action-count">{{ blog[r.key] || 0 }}</span>
            </button>
            <button class="action-btn" @click="$router.push(`/blog/${blog.id}#comments`)">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/></svg>
              <span>Bình luận</span>
            </button>
            <button class="action-btn" @click="share(blog)">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="18" cy="5" r="3"/><circle cx="6" cy="12" r="3"/><circle cx="18" cy="19" r="3"/><line x1="8.59" y1="13.51" x2="15.42" y2="17.49"/><line x1="15.41" y1="6.51" x2="8.59" y2="10.49"/></svg>
              <span>Chia sẻ</span>
            </button>
          </div>
        </article>

        <div class="load-more" v-if="hasMore">
          <el-button @click="loadMore" :loading="loading" round>
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="6 9 12 15 18 9"/></svg>
            Xem thêm
          </el-button>
        </div>
      </div>
    </main>

    <!-- RIGHT SIDEBAR -->
    <aside class="home-sidebar-right">
      <div class="sidebar-card">
        <div class="card-header">
          <div class="header-icon" style="background: linear-gradient(135deg, #f97316, #f59e0b)">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="23 6 13.5 15.5 8.5 10.5 1 18"/></svg>
          </div>
          <h3>Xu hướng</h3>
        </div>
        <div class="card-body">
          <div v-for="(post, idx) in trendingPosts" :key="post.id" class="trend-item">
            <span class="trend-rank" :class="`rank-${idx + 1}`">{{ idx + 1 }}</span>
            <router-link :to="`/blog/${post.id}`" class="trend-info">
              <span class="trend-title">{{ post.title }}</span>
              <span class="trend-meta">{{ formatCount(post.views) }} views · {{ post.readTime }} phút</span>
            </router-link>
          </div>
        </div>
      </div>

      <div class="sidebar-card">
        <div class="card-header">
          <div class="header-icon" style="background: linear-gradient(135deg, #0ea5e9, #8b5cf6)">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M22 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/></svg>
          </div>
          <h3>Gợi ý theo dõi</h3>
        </div>
        <div class="card-body">
          <div v-for="u in suggestedUsers" :key="u.id" class="suggest-item">
            <router-link :to="`/profile/${u.id}`" class="suggest-link">
              <div class="suggest-avatar">{{ u.displayName?.charAt(0) }}</div>
              <div class="suggest-info">
                <span class="suggest-name">{{ u.displayName }}</span>
                <span class="suggest-meta">{{ u.blogCount }} bài viết</span>
              </div>
            </router-link>
            <button class="follow-btn-small" @click="ElMessage.success('Đã follow')">Follow</button>
          </div>
        </div>
      </div>

      <div class="sidebar-card">
        <div class="card-header">
          <div class="header-icon" style="background: linear-gradient(135deg, #10b981, #059669)">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20.59 13.41l-7.17 7.17a2 2 0 0 1-2.83 0L2 12V2h10l8.59 8.59a2 2 0 0 1 0 2.82z"/><line x1="7" y1="7" x2="7.01" y2="7"/></svg>
          </div>
          <h3>Tag phổ biến</h3>
        </div>
        <div class="card-body">
          <div class="tag-cloud">
            <router-link v-for="tag in tags" :key="tag.id" :to="`/tag/${tag.id}`" class="tag-chip">#{{ tag.name }}</router-link>
          </div>
        </div>
      </div>
    </aside>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/store/auth'
import { blogApi, categoryApi, hashtagApi } from '@/api'
import EChart from '@/components/EChart.vue'
import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'
import 'dayjs/locale/vi'
dayjs.extend(relativeTime)
dayjs.locale('vi')

const authStore = useAuthStore()
const user = ref(authStore.user)

const activeTab = ref('latest')
const blogs = ref([])
const categories = ref([])
const tags = ref([])
const trendingPosts = ref([])
const trendingTags = ref([])
const suggestedUsers = ref([])
const loading = ref(false)
const page = ref(0)
const hasMore = ref(true)
const unreadCount = ref(3)
const userReaction = ref(null)

const tabs = [{ key: 'latest', label: 'Mới nhất' }, { key: 'following', label: 'Đang follow' }, { key: 'trending', label: 'Xu hướng' }]
const reactions = [
  { type: 'LIKE', icon: '👍', key: 'likeCount' },
  { type: 'LOVE', icon: '❤️', key: 'loveCount' },
  { type: 'HAHA', icon: '😂', key: 'hahaCount' },
]
const chipColors = ['#0ea5e9', '#f97316', '#10b981', '#ec4899', '#8b5cf6', '#f59e0b', '#06b6d4', '#ef4444']

const chartDays = ['T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'CN']
const chartData = [1420, 1880, 2210, 1950, 2780, 3150, 2890]

const chartOption = computed(() => ({
  tooltip: {
    trigger: 'axis',
    backgroundColor: 'rgba(255,255,255,0.95)',
    borderColor: 'var(--border)',
    borderWidth: 1,
    textStyle: { fontSize: 12, color: 'var(--text-primary)' }
  },
  grid: { top: 16, right: 8, bottom: 20, left: 40 },
  xAxis: {
    type: 'category',
    data: chartDays,
    axisLine: { show: false },
    axisTick: { show: false },
    axisLabel: { color: 'var(--text-muted)', fontSize: 11, fontWeight: 500 }
  },
  yAxis: {
    type: 'value',
    splitLine: { lineStyle: { color: 'var(--border-light)', type: 'dashed' } },
    axisLabel: { color: 'var(--text-muted)', fontSize: 11 },
    axisLine: { show: false },
    axisTick: { show: false }
  },
  series: [{
    type: 'bar',
    data: chartData,
    barWidth: '40%',
    itemStyle: {
      borderRadius: [4, 4, 0, 0],
      color: {
        type: 'linear',
        x: 0, y: 0, x2: 0, y2: 1,
        colorStops: [
          { offset: 0, color: '#fb7293' },
          { offset: 1, color: '#32c5e9' }
        ]
      }
    },
    emphasis: {
      itemStyle: {
        color: {
          type: 'linear',
          x: 0, y: 0, x2: 0, y2: 1,
          colorStops: [
            { offset: 0, color: '#32c5e9' },
            { offset: 1, color: '#fb7293' }
          ]
        }
      }
    }
  }]
}))

const formatDate = (d) => dayjs(d).fromNow()
const formatCount = (n) => n >= 1000 ? (n / 1000).toFixed(1) + 'k' : n
const changeTab = (key) => { activeTab.value = key; loadBlogs(true) }

const loadBlogs = async (reset = false) => {
  if (loading.value) return
  if (reset) { page.value = 0; blogs.value = []; hasMore.value = true }
  loading.value = true
  try {
    const res = activeTab.value === 'trending' ? await blogApi.trending(10) : await blogApi.getAll(page.value, 10)
    const data = res.data || []
    if (reset) blogs.value = data; else blogs.value.push(...data)
    hasMore.value = data.length === 10
    page.value++
  } catch (e) { console.error(e) }
  finally { loading.value = false }
}

const react = (blog, type) => { ElMessage.success(`Đã react ${type}`) }
const share = (blog) => { navigator.clipboard.writeText(window.location.origin + `/blog/${blog.id}`); ElMessage.success('Đã copy link') }
const loadMore = () => loadBlogs()

onMounted(() => { loadBlogs(true); loadSidebar() })

const loadSidebar = async () => {
  try {
    const [catRes, tagRes, blogRes] = await Promise.all([categoryApi.getAll(), hashtagApi.getTop(8), blogApi.trending(5)])
    categories.value = catRes.data || []
    tags.value = tagRes.data || []
    trendingPosts.value = blogRes.data || []
    trendingTags.value = tagRes.data || []
    suggestedUsers.value = [
      { id: 2, displayName: 'Trần Thị Bình', blogCount: 35 },
      { id: 3, displayName: 'Lê Hoàng Nam', blogCount: 28 },
      { id: 4, displayName: 'Phạm Minh Châu', blogCount: 22 },
    ]
  } catch (e) { console.error(e) }
}
</script>

<style scoped lang="scss">
.home-3col {
  display: grid;
  grid-template-columns: 240px 1fr 300px;
  gap: var(--space-lg);
  max-width: 100%;
}

/* ===== LEFT SIDEBAR ===== */
.home-sidebar-left {
  display: flex;
  flex-direction: column;
  gap: var(--space-md);
  position: sticky;
  top: calc(var(--header-height) + var(--space-lg));
  height: fit-content;
}

.sidebar-card {
  background: var(--surface);
  border-radius: var(--radius-xl);
  border: 1px solid var(--border-light);
  overflow: hidden;
}

/* User Card */
.user-card { position: relative; }
.user-cover {
  height: 64px;
  background: linear-gradient(135deg, var(--primary) 0%, #8b5cf6 50%, #ec4899 100%);
}
.user-info { padding: 0 var(--space-md) var(--space-md); text-align: center; margin-top: -24px; }
.user-avatar-wrapper { display: inline-block; position: relative; margin-bottom: 6px; }
.user-avatar { width: 52px; height: 52px; border-radius: 50%; object-fit: cover; border: 3px solid var(--surface); }
.user-avatar-placeholder { display: flex; align-items: center; justify-content: center; background: linear-gradient(135deg, var(--primary), #8b5cf6); color: white; font-weight: 700; font-size: 1.1rem; }
.status-dot { position: absolute; bottom: 2px; right: 2px; width: 12px; height: 12px; background: var(--success); border-radius: 50%; border: 2px solid var(--surface); }
.user-name { font-size: 0.95rem; font-weight: 700; color: var(--text-primary); margin-bottom: 4px; }
.user-stats-row { font-size: 0.8rem; color: var(--text-muted); }
.user-stats-row strong { color: var(--text-primary); font-weight: 600; }
.dot { margin: 0 4px; }

/* Nav Card */
.nav-card { padding: 6px; }
.nav-item {
  display: flex; align-items: center; gap: 10px;
  padding: 10px 12px; border-radius: var(--radius-md);
  color: var(--text-secondary); text-decoration: none;
  font-size: 0.88rem; font-weight: 500;
  transition: all var(--duration-fast) ease;
  position: relative;
}
.nav-item:hover { background: var(--surface-hover); color: var(--text-primary); }
.nav-item.active { background: var(--primary-50); color: var(--primary); font-weight: 600; }
.nav-icon { width: 30px; height: 30px; border-radius: var(--radius-sm); display: flex; align-items: center; justify-content: center; color: white; flex-shrink: 0; }
.nav-badge { margin-left: auto; padding: 1px 7px; background: var(--danger); color: white; border-radius: var(--radius-full); font-size: 0.7rem; font-weight: 700; }

/* Category Card */
.cat-card { padding: var(--space-md); }
.card-title { display: flex; align-items: center; gap: 6px; font-size: 0.88rem; font-weight: 700; color: var(--text-primary); margin-bottom: var(--space-sm); }
.cat-list { display: flex; flex-direction: column; gap: 2px; }
.cat-item { display: flex; align-items: center; gap: 8px; padding: 8px 10px; border-radius: var(--radius); text-decoration: none; color: var(--text-secondary); transition: all var(--duration-fast) ease; }
.cat-item:hover { background: var(--surface-hover); color: var(--primary); }
.cat-icon { font-size: 0.95rem; width: 20px; text-align: center; }
.cat-name { flex: 1; font-size: 0.85rem; }
.cat-count { font-size: 0.7rem; padding: 2px 8px; background: var(--bg-secondary); border-radius: var(--radius-full); color: var(--text-muted); font-weight: 500; }

/* ===== CENTER FEED ===== */
.home-feed { min-width: 0; }

.feed-tabs {
  display: flex; gap: 4px;
  background: var(--surface); border-radius: var(--radius-xl);
  padding: 5px; margin-bottom: var(--space-md);
  border: 1px solid var(--border-light);
}
.tab {
  flex: 1; padding: 10px 16px; border: none;
  background: transparent; border-radius: var(--radius-lg);
  font-size: 0.88rem; font-weight: 500;
  color: var(--text-secondary); cursor: pointer;
  transition: all var(--duration-fast) ease;
}
.tab:hover { background: var(--surface-hover); color: var(--text-primary); }
.tab.active { background: var(--primary); color: white; font-weight: 600; box-shadow: 0 2px 8px rgba(14,165,233,0.3); }

/* Trending Tags */
.trending-bar { margin-bottom: var(--space-md); overflow: hidden; }
.trending-scroll { display: flex; gap: 8px; overflow-x: auto; padding: 4px 0; scrollbar-width: none; }
.trending-scroll::-webkit-scrollbar { display: none; }
.trending-chip {
  display: flex; align-items: center; gap: 6px;
  padding: 8px 16px; background: var(--surface);
  border-radius: var(--radius-full); border: 1px solid var(--border-light);
  white-space: nowrap; font-size: 0.82rem; cursor: pointer;
  transition: all var(--duration-fast) ease;
}
.trending-chip:hover { border-color: var(--primary); background: var(--primary-50); transform: translateY(-1px); box-shadow: var(--shadow-sm); }
.chip-icon { font-size: 0.85rem; }
.chip-name { font-weight: 600; color: var(--text-primary); }
.chip-count { font-size: 0.7rem; color: var(--text-muted); }

/* Feed Cards */
.feed-list { display: flex; flex-direction: column; gap: var(--space-md); }

.feed-card {
  background: var(--surface); border-radius: var(--radius-xl);
  border: 1px solid var(--border-light);
  padding: var(--space-lg);
  transition: all var(--duration-normal) var(--ease-out);
  animation: fadeIn var(--duration-normal) var(--ease-out) forwards;
  opacity: 0;
}
.feed-card:hover { border-color: var(--border); box-shadow: var(--shadow-md); transform: translateY(-2px); }

.feed-header { display: flex; align-items: center; gap: 10px; margin-bottom: var(--space-md); }
.feed-author { flex-shrink: 0; text-decoration: none; }
.feed-avatar { width: 42px; height: 42px; border-radius: 50%; object-fit: cover; border: 2px solid var(--border-light); transition: border-color var(--duration-fast) ease; }
.feed-avatar:hover { border-color: var(--primary); }
.feed-avatar-placeholder { display: flex; align-items: center; justify-content: center; background: linear-gradient(135deg, var(--primary), #8b5cf6); color: white; font-weight: 700; font-size: 0.9rem; }
.feed-meta { flex: 1; min-width: 0; }
.author-name { font-weight: 600; font-size: 0.9rem; color: var(--text-primary); text-decoration: none; display: block; margin-bottom: 1px; }
.author-name:hover { color: var(--primary); }
.meta-row { display: flex; align-items: center; gap: 4px; font-size: 0.78rem; color: var(--text-muted); }
.meta-dot { font-size: 0.6rem; }

.feed-cover { margin-bottom: var(--space-md); border-radius: var(--radius-lg); overflow: hidden; }
.feed-cover img { width: 100%; height: 220px; object-fit: cover; transition: transform var(--duration-slow) var(--ease-out); }
.feed-cover:hover img { transform: scale(1.02); }

.feed-body { margin-bottom: var(--space-md); }
.feed-title {
  font-size: 1.15rem; font-weight: 700; color: var(--text-primary);
  text-decoration: none; line-height: 1.45; display: block; margin-bottom: 6px;
  transition: color var(--duration-fast) ease;
}
.feed-title:hover { color: var(--primary); }
.feed-desc {
  font-size: 0.88rem; color: var(--text-secondary); line-height: 1.55;
  display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden;
}

.feed-stats {
  display: flex; gap: var(--space-lg);
  padding: var(--space-sm) 0;
  border-top: 1px solid var(--border-light);
  border-bottom: 1px solid var(--border-light);
  margin-bottom: var(--space-sm);
}
.stat-item {
  display: flex; align-items: center; gap: 4px;
  font-size: 0.78rem; color: var(--text-muted); font-weight: 500;
}
.stat-item svg { opacity: 0.6; }

.feed-actions { display: flex; gap: 2px; }
.action-btn {
  flex: 1; display: flex; align-items: center; justify-content: center; gap: 6px;
  padding: 8px; border: none; background: transparent; border-radius: var(--radius-md);
  font-size: 0.82rem; color: var(--text-secondary); cursor: pointer;
  transition: all var(--duration-fast) ease; font-weight: 500;
}
.action-btn:hover { background: var(--surface-hover); color: var(--primary); }
.action-btn.active { background: var(--primary-50); color: var(--primary); }
.action-count { font-size: 0.78rem; }

.load-more { display: flex; justify-content: center; padding: var(--space-lg); }

/* ===== STATS CARD ===== */
.stats-card {
  background: var(--surface);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-xl);
  padding: var(--space-lg);
  margin-bottom: var(--space-md);
  transition: all var(--duration-normal) var(--ease-out);

  &:hover {
    border-color: var(--border);
    box-shadow: var(--shadow-md);
  }
}

.stats-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--space-md);
}

.stats-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.95rem;
  font-weight: 700;
  color: var(--text-primary);

  svg {
    color: var(--primary);
  }
}

.stats-badge {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 4px 10px;
  background: rgba(16, 185, 129, 0.1);
  color: var(--success);
  border-radius: var(--radius-full);
  font-size: 0.78rem;
  font-weight: 600;

  svg { width: 12px; height: 12px; }
}

.stats-chart {
  height: 220px;
}

/* ===== RIGHT SIDEBAR ===== */
.home-sidebar-right {
  display: flex; flex-direction: column; gap: var(--space-md);
  position: sticky; top: calc(var(--header-height) + var(--space-lg)); height: fit-content;
}
.card-header {
  display: flex; align-items: center; gap: 8px;
  padding: var(--space-md) var(--space-md) 0;
}
.header-icon { width: 26px; height: 26px; border-radius: var(--radius-sm); display: flex; align-items: center; justify-content: center; color: white; }
.card-header h3 { font-size: 0.9rem; font-weight: 700; }
.card-body { padding: var(--space-sm) var(--space-md) var(--space-md); }

/* Trending */
.trend-item { display: flex; align-items: flex-start; gap: 8px; padding: 8px; border-radius: var(--radius-md); transition: background var(--duration-fast) ease; }
.trend-item:hover { background: var(--surface-hover); }
.trend-rank { width: 22px; height: 22px; border-radius: var(--radius-xs); display: flex; align-items: center; justify-content: center; font-size: 0.7rem; font-weight: 700; flex-shrink: 0; background: var(--bg-secondary); color: var(--text-muted); }
.trend-rank.rank-1 { background: linear-gradient(135deg, #f97316, #f59e0b); color: white; }
.trend-rank.rank-2 { background: linear-gradient(135deg, #8b5cf6, #a78bfa); color: white; }
.trend-rank.rank-3 { background: linear-gradient(135deg, #06b6d4, #22d3ee); color: white; }
.trend-info { flex: 1; text-decoration: none; min-width: 0; }
.trend-title { display: block; font-size: 0.82rem; font-weight: 500; color: var(--text-primary); line-height: 1.35; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.trend-meta { font-size: 0.72rem; color: var(--text-muted); }

/* Suggested Users */
.suggest-item { display: flex; align-items: center; gap: 8px; padding: 8px 0; border-bottom: 1px solid var(--border-light); }
.suggest-item:last-child { border-bottom: none; }
.suggest-link { display: flex; align-items: center; gap: 8px; flex: 1; text-decoration: none; min-width: 0; }
.suggest-avatar { width: 34px; height: 34px; border-radius: 50%; display: flex; align-items: center; justify-content: center; background: linear-gradient(135deg, var(--primary-light), #8b5cf6); color: white; font-weight: 600; font-size: 0.8rem; flex-shrink: 0; }
.suggest-info { display: flex; flex-direction: column; min-width: 0; }
.suggest-name { font-size: 0.82rem; font-weight: 600; color: var(--text-primary); overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.suggest-meta { font-size: 0.72rem; color: var(--text-muted); }
.follow-btn-small { padding: 4px 14px; border: 1px solid var(--primary); background: transparent; color: var(--primary); border-radius: var(--radius-full); font-size: 0.75rem; font-weight: 600; cursor: pointer; transition: all var(--duration-fast) ease; white-space: nowrap; }
.follow-btn-small:hover { background: var(--primary); color: white; }

/* Tags */
.tag-cloud { display: flex; flex-wrap: wrap; gap: 6px; }
.tag-chip { padding: 6px 12px; background: var(--bg-secondary); border-radius: var(--radius-full); font-size: 0.78rem; font-weight: 500; color: var(--text-secondary); text-decoration: none; transition: all var(--duration-fast) ease; }
.tag-chip:hover { background: var(--primary-50); color: var(--primary); }

@media (max-width: 1200px) { .home-sidebar-right { display: none; } .home-3col { grid-template-columns: 220px 1fr; } }
@media (max-width: 900px) { .home-sidebar-left { display: none; } .home-3col { grid-template-columns: 1fr; } }
</style>
