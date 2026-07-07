<template>
  <ThreeColumnLayout>
    <template #sidebar-left>
      <div class="home-sidebar-left">
        <router-link to="/profile" class="sc-profile-card">
          <img v-if="user?.avatarUrl" :src="user.avatarUrl" class="sc-avatar" />
          <div v-else class="sc-avatar sc-avatar-placeholder">{{ user?.displayName?.charAt(0) || 'U' }}</div>
          <div class="sc-info">
            <span class="sc-name">{{ user?.displayName || 'Khách' }}</span>
            <span class="sc-meta">{{ user?.blogCount || 0 }} bài viết · {{ formatCount(user?.followerCount || 0) }} theo dõi</span>
          </div>
        </router-link>

        <nav class="sc-nav">
          <router-link to="/home" class="sc-nav-item active">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"><path d="m3 9 9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/><polyline points="9 22 9 12 15 12 15 22"/></svg>
            Trang chủ
          </router-link>
          <router-link to="/trending" class="sc-nav-item">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"><polyline points="23 6 13.5 15.5 8.5 10.5 1 18"/><polyline points="17 6 23 6 23 12"/></svg>
            Xu hướng
          </router-link>
          <router-link to="/series" class="sc-nav-item">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"><path d="M4 19.5v-15A2.5 2.5 0 0 1 6.5 2H20v20H6.5a2.5 2.5 0 0 1 0-5H20"/></svg>
            Series
          </router-link>
          <router-link to="/music" class="sc-nav-item">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"><path d="M9 18V5l12-2v13"/><circle cx="6" cy="18" r="3"/><circle cx="18" cy="16" r="3"/></svg>
            Music
          </router-link>
          <router-link to="/notifications" class="sc-nav-item">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"><path d="M6 8a6 6 0 0 1 12 0c0 7 3 9 3 9H3s3-2 3-9"/><path d="M10.3 21a1.94 1.94 0 0 0 3.4 0"/></svg>
            Thông báo
            <span class="sc-badge" v-if="unreadCount > 0">{{ unreadCount }}</span>
          </router-link>
          <router-link to="/bookmarks" class="sc-nav-item">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"><path d="M19 21l-7-5-7 5V5a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2z"/></svg>
            Đã lưu
          </router-link>
        </nav>

        <div class="sc-section">
          <h4 class="sc-section-title">Danh mục</h4>
          <div class="sc-cat-list">
            <router-link v-for="cat in categories" :key="cat.id" :to="`/category/${cat.slug}`" class="sc-cat-item">
              <span class="sc-cat-icon">{{ cat.icon }}</span>
              <span class="sc-cat-name">{{ cat.name }}</span>
              <span class="sc-cat-count">{{ cat.blogCount }}</span>
            </router-link>
          </div>
        </div>
      </div>
    </template>

    <div class="home-feed">
      <div class="feed-tabs">
        <button v-for="tab in tabs" :key="tab.key" :class="['tab', { active: activeTab === tab.key }]" @click="changeTab(tab.key)">
          {{ tab.label }}
        </button>
      </div>

      <StoryList />
      <StoryViewer />
      <CanvasViewer v-if="canvasViewerVisible" :canvas="viewingCanvas" :initial-strokes="[]" @close="canvasViewerVisible = false" @stroke="handleCanvasStroke" />

      <div class="trending-chip-row">
        <div class="trending-scroll">
          <div v-for="(tag, idx) in trendingTags" :key="tag.id" class="trending-chip" @click="$router.push(`/tag/${tag.id}`)">
            <span class="chip-icon" :style="{ color: chipColors[idx % chipColors.length] }">🔥</span>
            <span class="chip-name">#{{ tag.name }}</span>
          </div>
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
                <span>{{ formatDate(blog.publishedAt) }}</span>
                <span class="meta-dot">·</span>
                <span>{{ blog.readTime }} phút đọc</span>
                <span class="meta-dot">·</span>
                <span>{{ formatCount(blog.views) }} lượt xem</span>
              </div>
            </div>
          </div>

          <div class="feed-body">
            <router-link :to="`/blog/${blog.id}`" class="feed-title">{{ blog.title }}</router-link>
            <p class="feed-desc" v-if="blog.description">{{ blog.description }}</p>
          </div>

          <div class="feed-cover" v-if="blog.coverImage">
            <router-link :to="`/blog/${blog.id}`">
              <img :src="blog.coverImage" :alt="blog.title" loading="lazy" />
            </router-link>
          </div>

          <div class="feed-footer">
            <div class="ff-stats">
              <span class="ff-stat">
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/></svg>
                {{ blog.commentCount }}
              </span>
              <span class="ff-stat">
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"/></svg>
                {{ blog.likeCount }}
              </span>
            </div>
            <div class="ff-actions">
              <button v-for="r in reactions" :key="r.type" :class="['ff-btn', { active: userReaction === r.type }]" @click="react(blog, r.type)">
                {{ r.icon }}
              </button>
              <button class="ff-btn" @click="$router.push(`/blog/${blog.id}#comments`)" title="Bình luận">
                <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/></svg>
              </button>
              <button class="ff-btn" @click="share(blog)" title="Chia sẻ">
                <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="18" cy="5" r="3"/><circle cx="6" cy="12" r="3"/><circle cx="18" cy="19" r="3"/><line x1="8.59" y1="13.51" x2="15.42" y2="17.49"/><line x1="15.41" y1="6.51" x2="8.59" y2="10.49"/></svg>
              </button>
            </div>
          </div>
        </article>

        <div class="load-more" v-if="hasMore">
          <el-button @click="loadMore" :loading="loading" round>Xem thêm</el-button>
        </div>
      </div>
    </div>

    <template #sidebar-right>
      <div class="home-sidebar-right">
        <APlayerMusicBox v-if="playlist" :playlist="playlist" :is-logged-in="isLoggedIn" compact :plugins="[UrlSourcePlugin, VotePlugin]" />
        <CanvasPreview v-if="canvas" :canvas="canvas" @viewCanvas="openCanvas" />
        <div class="sr-section" v-if="trendingPosts.length">
          <h4 class="sr-title">Bài viết nổi bật</h4>
          <div class="sr-list">
            <router-link v-for="post in trendingPosts" :key="post.id" :to="`/blog/${post.id}`" class="sr-item">
              <span class="sr-rank" :class="`rank-${post.rank || 0}`">{{ post.rank || 0 }}</span>
              <span class="sr-text">{{ post.title }}</span>
            </router-link>
          </div>
        </div>
        <div class="sr-section" v-if="tags.length">
          <h4 class="sr-title">Thẻ tags</h4>
          <div class="sr-tags">
            <router-link v-for="tag in tags" :key="tag.id" :to="`/tag/${tag.id}`" class="sr-tag">#{{ tag.name }}</router-link>
          </div>
        </div>
      </div>
    </template>
  </ThreeColumnLayout>
</template>
<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/store/auth'
import { blogApi, categoryApi, hashtagApi, questApi, blindApi, skillApi, statusApi, playlistApi, canvasApi } from '@/api'
import ThreeColumnLayout from '@/components/layouts/ThreeColumnLayout.vue'
import APlayerMusicBox from '@/components/blog/APlayerMusicBox.vue'
import { UrlSourcePlugin, VotePlugin } from '@/components/blog/music-plugins'
import CanvasPreview from '@/components/blog/CanvasPreview.vue'
import StoryList from '@/components/story/StoryList.vue'
import StoryViewer from '@/components/story/StoryViewer.vue'
import CanvasViewer from '@/components/canvas/CanvasViewer.vue'
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
const loading = ref(false)
const page = ref(0)
const hasMore = ref(true)
const unreadCount = ref(3)
const userReaction = ref(null)
const quests = ref([])
const playlist = ref(null)
const canvas = ref(null)
const canvasViewerVisible = ref(false)
const viewingCanvas = ref(null)

const isLoggedIn = computed(() => !!authStore.user)
const tabs = [{ key: 'latest', label: 'Mới nhất' }, { key: 'following', label: 'Đang follow' }, { key: 'trending', label: 'Xu hướng' }]
const reactions = [
  { type: 'LIKE', icon: '👍' },
  { type: 'LOVE', icon: '❤️' },
  { type: 'HAHA', icon: '😂' },
]
const chipColors = ['#0ea5e9', '#f97316', '#10b981', '#ec4899', '#8b5cf6', '#f59e0b', '#06b6d4', '#ef4444']

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

onMounted(() => { loadBlogs(true); loadSidebar(); loadGamification() })

const loadGamification = async () => {
  try {
    const userId = user.value?.id
    const [playlistRes, canvasRes] = await Promise.all([
      playlistApi.getByUser(userId),
      canvasApi.getByUser(userId),
    ])
    playlist.value = playlistRes.data
    canvas.value = canvasRes.data
  } catch (e) { console.error(e) }
}

const openCanvas = (canvas) => { viewingCanvas.value = canvas; canvasViewerVisible.value = true }
const handleCanvasStroke = (stroke) => {}

const loadSidebar = async () => {
  try {
    const [catRes, tagRes, blogRes] = await Promise.all([categoryApi.getAll(), hashtagApi.getTop(12), blogApi.trending(5)])
    categories.value = catRes.data || []
    tags.value = tagRes.data || []
    trendingPosts.value = blogRes.data || []
    trendingTags.value = tagRes.data || []
  } catch (e) { console.error(e) }
}
</script>
<style scoped lang="scss">
/* ─── Left Sidebar ─── */
.home-sidebar-left { display: flex; flex-direction: column; gap: 12px; }

.sc-profile-card {
  display: flex; align-items: center; gap: 10px;
  padding: 14px; background: var(--surface);
  border-radius: var(--radius-xl); border: 1px solid var(--border-light);
  text-decoration: none; transition: border-color 0.15s;
  &:hover { border-color: var(--border); }
}
.sc-avatar {
  width: 44px; height: 44px; border-radius: 50%; object-fit: cover;
  flex-shrink: 0;
}
.sc-avatar-placeholder {
  display: flex; align-items: center; justify-content: center;
  background: linear-gradient(135deg, var(--primary), #8b5cf6); color: #fff;
  font-weight: 700; font-size: 1rem;
}
.sc-info { flex: 1; min-width: 0; }
.sc-name { display: block; font-weight: 600; font-size: 0.9rem; color: var(--text-primary); margin-bottom: 1px; }
.sc-meta { font-size: 0.74rem; color: var(--text-muted); }

.sc-nav {
  background: var(--surface); border-radius: var(--radius-xl);
  border: 1px solid var(--border-light); padding: 4px;
}
.sc-nav-item {
  display: flex; align-items: center; gap: 10px;
  padding: 9px 12px; border-radius: var(--radius-lg);
  color: var(--text-secondary); text-decoration: none;
  font-size: 0.85rem; font-weight: 500;
  transition: all 0.12s; position: relative;
  svg { width: 16px; height: 16px; flex-shrink: 0; opacity: 0.7; }
  &:hover { background: var(--surface-hover); color: var(--text-primary); }
  &.active { background: var(--primary-50); color: var(--primary); font-weight: 600; svg { opacity: 1; } }
}
.sc-badge {
  margin-left: auto; padding: 1px 7px; background: var(--danger);
  color: #fff; border-radius: 99px; font-size: 0.68rem; font-weight: 700;
}

.sc-section {
  background: var(--surface); border-radius: var(--radius-xl);
  border: 1px solid var(--border-light); padding: 12px 14px;
}
.sc-section-title {
  font-size: 0.75rem; font-weight: 700; color: var(--text-muted);
  text-transform: uppercase; letter-spacing: 0.05em;
  margin-bottom: 8px;
}
.sc-cat-list { display: flex; flex-direction: column; gap: 1px; }
.sc-cat-item {
  display: flex; align-items: center; gap: 8px;
  padding: 6px 8px; border-radius: var(--radius-md);
  text-decoration: none; color: var(--text-secondary);
  font-size: 0.83rem; transition: all 0.12s;
  &:hover { background: var(--surface-hover); color: var(--primary); }
}
.sc-cat-icon { font-size: 0.9rem; width: 20px; text-align: center; flex-shrink: 0; }
.sc-cat-name { flex: 1; }
.sc-cat-count {
  font-size: 0.68rem; padding: 1px 7px;
  background: var(--bg-secondary); border-radius: 99px;
  color: var(--text-muted); font-weight: 500;
}

/* ─── Center Feed ─── */
.home-feed { min-width: 0; }

.feed-tabs {
  display: flex; gap: 4px;
  background: var(--surface); border-radius: var(--radius-xl);
  padding: 4px; margin-bottom: 16px;
  border: 1px solid var(--border-light);
}
.tab {
  flex: 1; padding: 9px 16px; border: none;
  background: transparent; border-radius: var(--radius-lg);
  font-size: 0.85rem; font-weight: 500;
  color: var(--text-secondary); cursor: pointer;
  transition: all 0.12s;
  &:hover { background: var(--surface-hover); color: var(--text-primary); }
  &.active { background: var(--primary); color: #fff; font-weight: 600; }
}

.trending-chip-row {
  margin-bottom: 16px; overflow: hidden;
}
.trending-scroll {
  display: flex; gap: 6px; overflow-x: auto;
  padding: 2px 0; scrollbar-width: none;
  &::-webkit-scrollbar { display: none; }
}
.trending-chip {
  display: flex; align-items: center; gap: 4px;
  padding: 5px 12px; background: var(--surface);
  border-radius: 99px; border: 1px solid var(--border-light);
  white-space: nowrap; font-size: 0.8rem; cursor: pointer;
  transition: all 0.12s;
  &:hover { border-color: var(--primary); background: var(--primary-50); }
}
.chip-icon { font-size: 0.75rem; }
.chip-name { font-weight: 600; color: var(--text-primary); }

.feed-list { display: flex; flex-direction: column; gap: 14px; }
.feed-card {
  background: var(--surface); border-radius: var(--radius-xl);
  border: 1px solid var(--border-light); overflow: hidden;
  transition: all 0.2s ease;
  animation: fadeIn 0.3s ease forwards; opacity: 0;
}
.feed-card:hover { border-color: var(--border); box-shadow: var(--shadow-sm); }
.feed-header { display: flex; align-items: center; gap: 10px; padding: 16px 20px 0; }
.feed-author { flex-shrink: 0; text-decoration: none; }
.feed-avatar { width: 36px; height: 36px; border-radius: 50%; object-fit: cover; }
.feed-avatar-placeholder { display: flex; align-items: center; justify-content: center; background: linear-gradient(135deg, var(--primary), #8b5cf6); color: #fff; font-weight: 700; font-size: 0.85rem; }
.feed-meta { flex: 1; min-width: 0; }
.author-name { font-weight: 600; font-size: 0.88rem; color: var(--text-primary); text-decoration: none; display: block; margin-bottom: 1px; }
.author-name:hover { color: var(--primary); }
.meta-row { display: flex; align-items: center; gap: 4px; font-size: 0.75rem; color: var(--text-muted); }
.meta-dot { opacity: 0.4; }

.feed-body { padding: 12px 20px 0; }
.feed-title {
  font-size: 1.1rem; font-weight: 700; color: var(--text-primary);
  text-decoration: none; line-height: 1.45; display: block; margin-bottom: 4px;
  transition: color 0.12s;
}
.feed-title:hover { color: var(--primary); }
.feed-desc {
  font-size: 0.85rem; color: var(--text-secondary); line-height: 1.55;
  display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden;
}

.feed-cover { padding: 12px 20px 0; }
.feed-cover img { width: 100%; height: 200px; object-fit: cover; border-radius: var(--radius-lg); transition: transform 0.2s; }
.feed-cover:hover img { transform: scale(1.01); }

.feed-footer {
  display: flex; align-items: center; justify-content: space-between;
  padding: 12px 20px; margin-top: 12px;
  border-top: 1px solid var(--border-light);
}
.ff-stats { display: flex; gap: 14px; }
.ff-stat { display: flex; align-items: center; gap: 4px; font-size: 0.76rem; color: var(--text-muted); font-weight: 500; }
.ff-stat svg { opacity: 0.5; }
.ff-actions { display: flex; gap: 4px; }
.ff-btn {
  width: 32px; height: 32px; display: flex; align-items: center; justify-content: center;
  border: none; background: transparent; border-radius: var(--radius-md);
  font-size: 0.9rem; cursor: pointer; color: var(--text-secondary);
  transition: all 0.12s;
}
.ff-btn:hover { background: var(--surface-hover); }
.ff-btn.active { background: var(--primary-50); }
.ff-btn svg { width: 15px; height: 15px; }

.load-more { display: flex; justify-content: center; padding: 20px; }

/* ─── Right Sidebar ─── */
.home-sidebar-right { display: flex; flex-direction: column; gap: 14px; }

.sr-section {
  background: var(--surface); border-radius: var(--radius-xl);
  border: 1px solid var(--border-light); padding: 14px;
}
.sr-title {
  font-size: 0.75rem; font-weight: 700; color: var(--text-muted);
  text-transform: uppercase; letter-spacing: 0.05em; margin-bottom: 10px;
}
.sr-list { display: flex; flex-direction: column; gap: 2px; }
.sr-item {
  display: flex; align-items: center; gap: 8px;
  padding: 7px 8px; border-radius: var(--radius-md);
  text-decoration: none; font-size: 0.83rem; color: var(--text-secondary);
  transition: all 0.12s;
  &:hover { background: var(--surface-hover); color: var(--primary); }
}
.sr-rank {
  width: 20px; height: 20px; border-radius: 4px;
  display: flex; align-items: center; justify-content: center;
  font-size: 0.68rem; font-weight: 700; color: var(--text-muted);
  background: var(--bg-secondary); flex-shrink: 0;
  &.rank-0, &.rank-1, &.rank-2 {
    background: var(--primary); color: #fff;
  }
}
.sr-text { flex: 1; min-width: 0; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }

.sr-tags { display: flex; flex-wrap: wrap; gap: 4px; }
.sr-tag {
  padding: 4px 10px; background: var(--bg-secondary);
  border-radius: 99px; font-size: 0.76rem; font-weight: 500;
  color: var(--text-secondary); text-decoration: none;
  transition: all 0.12s;
  &:hover { background: var(--primary-50); color: var(--primary); }
}

@keyframes fadeIn { from { opacity: 0; transform: translateY(4px); } to { opacity: 1; transform: translateY(0); } }
</style>
