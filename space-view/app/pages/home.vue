<template>
  <ThreeColumnLayout>
    <template #sidebar-left>
      <div class="sidebar-left">
        <NuxtLink to="/profile" class="user-card">
          <img v-if="user?.avatarUrl" :src="user.avatarUrl" class="user-card-avatar" />
          <div v-else class="user-card-avatar user-card-avatar--placeholder">{{ user?.displayName?.charAt(0) || 'U' }}</div>
          <div class="user-card-info">
            <span class="user-card-name">{{ user?.displayName || 'Khách' }}</span>
            <span class="user-card-meta">{{ user?.blogCount || 0 }} bài viết · {{ formatCount(user?.followerCount || 0) }} theo dõi</span>
          </div>
        </NuxtLink>

        <nav class="side-nav">
          <NuxtLink to="/home" class="side-nav-item side-nav-item--active">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="m3 9 9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/><polyline points="9 22 9 12 15 12 15 22"/></svg>
            <span>Trang chủ</span>
          </NuxtLink>
          <NuxtLink to="/trending" class="side-nav-item">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="23 6 13.5 15.5 8.5 10.5 1 18"/><polyline points="17 6 23 6 23 12"/></svg>
            <span>Xu hướng</span>
          </NuxtLink>
          <NuxtLink to="/series" class="side-nav-item">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M4 19.5v-15A2.5 2.5 0 0 1 6.5 2H20v20H6.5a2.5 2.5 0 0 1 0-5H20"/></svg>
            <span>Series</span>
          </NuxtLink>
          <NuxtLink to="/music" class="side-nav-item">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M9 18V5l12-2v13"/><circle cx="6" cy="18" r="3"/><circle cx="18" cy="16" r="3"/></svg>
            <span>Music</span>
          </NuxtLink>
          <NuxtLink to="/notifications" class="side-nav-item">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M6 8a6 6 0 0 1 12 0c0 7 3 9 3 9H3s3-2 3-9"/><path d="M10.3 21a1.94 1.94 0 0 0 3.4 0"/></svg>
            <span>Thông báo</span>
            <span v-if="unreadCount > 0" class="side-nav-badge">{{ unreadCount }}</span>
          </NuxtLink>
          <NuxtLink to="/bookmarks" class="side-nav-item">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M19 21l-7-5-7 5V5a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2z"/></svg>
            <span>Đã lưu</span>
          </NuxtLink>
        </nav>

        <div class="side-section">
          <h4 class="side-section-title">Danh mục</h4>
          <NuxtLink v-for="cat in categories" :key="cat.id" :to="`/category/${cat.slug}`" class="cat-item">
            <span class="cat-icon">{{ cat.icon }}</span>
            <span class="cat-name">{{ cat.name }}</span>
            <span class="cat-count">{{ cat.blogCount }}</span>
          </NuxtLink>
        </div>
      </div>
    </template>

    <div class="feed-main">
      <StoryList />
      <StoryViewer />
      <CanvasViewer v-if="canvasViewerVisible" :canvas="viewingCanvas" :initial-strokes="[]" @close="canvasViewerVisible = false" @stroke="handleCanvasStroke" />

      <div class="feed-tabs">
        <button v-for="tab in tabs" :key="tab.key" :class="['feed-tab', { 'feed-tab--active': activeTab === tab.key }]" @click="changeTab(tab.key)">
          {{ tab.label }}
        </button>
      </div>

      <div class="feed-list">
        <article v-for="blog in blogs" :key="blog.id" class="feed-card">
          <div class="feed-card-header">
            <NuxtLink :to="`/profile/${blog.authorId}`" class="feed-card-avatar-link">
              <img v-if="blog.authorAvatar" :src="blog.authorAvatar" class="feed-card-avatar" />
              <div v-else class="feed-card-avatar feed-card-avatar--placeholder">{{ blog.authorName?.charAt(0) || 'U' }}</div>
            </NuxtLink>
            <div class="feed-card-meta">
              <NuxtLink :to="`/profile/${blog.authorId}`" class="feed-card-author">{{ blog.authorName }}</NuxtLink>
              <div class="feed-card-sub">
                <time>{{ formatDate(blog.publishedAt) }}</time>
                <span>·</span>
                <span>{{ blog.readTime }} phút đọc</span>
                <span>·</span>
                <span>{{ formatCount(blog.views) }} lượt xem</span>
              </div>
            </div>
          </div>

          <div class="feed-card-body">
            <NuxtLink :to="`/blog/${blog.id}`" class="feed-card-title">{{ blog.title }}</NuxtLink>
            <p v-if="blog.description" class="feed-card-desc">{{ blog.description }}</p>
          </div>

          <div v-if="blog.coverImage" class="feed-card-cover">
            <NuxtLink :to="`/blog/${blog.id}`">
              <img :src="blog.coverImage" :alt="blog.title" loading="lazy" />
            </NuxtLink>
          </div>

          <div class="feed-card-footer">
            <div class="feed-card-stats">
              <span class="feed-card-stat">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/></svg>
                {{ blog.commentCount }}
              </span>
              <span class="feed-card-stat">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"/></svg>
                {{ blog.likeCount }}
              </span>
            </div>
            <div class="feed-card-actions">
              <button v-for="r in reactions" :key="r.type" :class="['feed-card-action', { 'feed-card-action--active': userReaction === r.type }]" @click="react(blog, r.type)">
                {{ r.icon }} {{ r.label }}
              </button>
              <button class="feed-card-action" @click="navigateTo(`/blog/${blog.id}#comments`)">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/></svg>
                Bình luận
              </button>
              <button class="feed-card-action" @click="share(blog)">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="18" cy="5" r="3"/><circle cx="6" cy="12" r="3"/><circle cx="18" cy="19" r="3"/><line x1="8.59" y1="13.51" x2="15.42" y2="17.49"/><line x1="15.41" y1="6.51" x2="8.59" y2="10.49"/></svg>
                Chia sẻ
              </button>
            </div>
          </div>
        </article>

        <div v-if="hasMore" class="feed-load-more">
          <button class="load-more-btn" @click="loadMore" :disabled="loading">
            <span v-if="loading" class="load-more-spinner"></span>
            {{ loading ? 'Đang tải...' : 'Xem thêm bài viết' }}
          </button>
        </div>
      </div>
    </div>

    <template #sidebar-right>
      <div class="sidebar-right">
        <APlayerMusicBox v-if="playlist" :playlist="playlist" :is-logged-in="isLoggedIn" compact :plugins="[UrlSourcePlugin, VotePlugin]" />
        <CanvasPreview v-if="canvas" :canvas="canvas" @viewCanvas="openCanvas" />

        <div class="side-section" v-if="trendingPosts.length">
          <h4 class="side-section-title">Bài viết nổi bật</h4>
          <NuxtLink v-for="(post, idx) in trendingPosts" :key="post.id" :to="`/blog/${post.id}`" class="trending-item">
            <span class="trending-rank" :class="{ 'trending-rank--top': idx < 3 }">{{ idx + 1 }}</span>
            <div class="trending-info">
              <span class="trending-title">{{ post.title }}</span>
              <span class="trending-views">{{ formatCount(post.views) }} lượt xem</span>
            </div>
          </NuxtLink>
        </div>

        <div class="side-section" v-if="tags.length">
          <h4 class="side-section-title">Thẻ tags phổ biến</h4>
          <div class="tag-cloud">
            <NuxtLink v-for="tag in tags" :key="tag.id" :to="`/tag/${tag.id}`" class="tag-item">#{{ tag.name }}</NuxtLink>
          </div>
        </div>
      </div>
    </template>
  </ThreeColumnLayout>
</template>

<script setup>
import { ref } from 'vue'
import { blogs as mockBlogs, categories as mockCategories, hashtags } from '~/utils/dummy'

definePageMeta({ layout: 'default' })
useHead({ title: 'Trang chủ - 0x1lBlog' })

const activeTab = ref('latest')
const blogs = ref([])
const categories = ref([])
const tags = ref([])
const trendingPosts = ref([])
const loading = ref(false)
const hasMore = ref(true)
const unreadCount = ref(3)
const userReaction = ref(null)
const playlist = ref(null)
const canvas = ref(null)
const canvasViewerVisible = ref(false)
const viewingCanvas = ref(null)
const user = ref({ id: 1, displayName: 'Admin', avatarUrl: '', blogCount: 15, followerCount: 120 })

const tabs = [
  { key: 'latest', label: 'Mới nhất' },
  { key: 'following', label: 'Đang follow' },
  { key: 'trending', label: 'Xu hướng' },
]
const reactions = [
  { type: 'LIKE', icon: '👍', label: 'Thích' },
  { type: 'LOVE', icon: '❤️', label: 'Yêu thích' },
  { type: 'HAHA', icon: '😂', label: 'Hài hước' },
]

const { formatDate, formatCount } = useFormat()

const published = mockBlogs.filter(b => b.status === 'PUBLISHED')
blogs.value = published.slice(0, 10)
categories.value = mockCategories.slice(0, 8)
tags.value = hashtags.slice(0, 15)
trendingPosts.value = [...published].sort((a, b) => b.views - a.views).slice(0, 5)

const changeTab = (key) => { activeTab.value = key }
const react = (blog, type) => { console.log(`React ${type}`) }
const share = (blog) => { navigator.clipboard.writeText(window.location.origin + `/blog/${blog.id}`) }
const loadMore = () => {}
const openCanvas = (c) => { viewingCanvas.value = c; canvasViewerVisible.value = true }
const handleCanvasStroke = (stroke) => {}
</script>

<style scoped lang="scss">
/* ===== LEFT SIDEBAR (CSDN-style) ===== */
.sidebar-left { display: flex; flex-direction: column; gap: 2px; }

.user-card {
  display: flex; align-items: center; gap: 12px;
  padding: 16px; margin-bottom: 8px;
  background: var(--surface); border: 1px solid var(--border-light);
  border-radius: 8px; text-decoration: none;
  transition: border-color 0.15s;
  &:hover { border-color: var(--border); }
}
.user-card-avatar {
  width: 44px; height: 44px; border-radius: 50%; object-fit: cover; flex-shrink: 0;
}
.user-card-avatar--placeholder {
  display: flex; align-items: center; justify-content: center;
  background: linear-gradient(135deg, var(--primary), #8b5cf6); color: #fff;
  font-weight: 700; font-size: 1rem;
}
.user-card-info { flex: 1; min-width: 0; }
.user-card-name { display: block; font-weight: 600; font-size: 0.9rem; color: var(--text-primary); margin-bottom: 1px; }
.user-card-meta { font-size: 0.78rem; color: var(--text-muted); }

.side-nav {
  display: flex; flex-direction: column; gap: 1px;
  background: var(--surface); border: 1px solid var(--border-light);
  border-radius: 8px; padding: 6px; margin-bottom: 8px;
}
.side-nav-item {
  display: flex; align-items: center; gap: 12px;
  padding: 10px 12px; border-radius: 6px;
  color: var(--text-secondary); text-decoration: none;
  font-size: 0.875rem; font-weight: 500;
  transition: all 0.12s;
  svg { width: 20px; height: 20px; flex-shrink: 0; opacity: 0.55; }
  &:hover { background: var(--bg-secondary); color: var(--text-primary); }
  &.side-nav-item--active {
    background: var(--primary-50); color: var(--primary); font-weight: 600;
    svg { opacity: 1; }
  }
}
.side-nav-badge {
  margin-left: auto; padding: 1px 7px; background: var(--danger);
  color: #fff; border-radius: 10px; font-size: 0.7rem; font-weight: 700; line-height: 1.4;
}

.side-section {
  background: var(--surface); border: 1px solid var(--border-light);
  border-radius: 8px; padding: 16px;
}
.side-section-title {
  font-size: 0.8rem; font-weight: 700; color: var(--text-primary);
  margin-bottom: 12px; padding-bottom: 10px;
  border-bottom: 1px solid var(--border-light);
}
.cat-item {
  display: flex; align-items: center; gap: 10px;
  padding: 8px 8px; border-radius: 6px;
  text-decoration: none; color: var(--text-secondary);
  font-size: 0.85rem; transition: all 0.12s;
  &:hover { background: var(--bg-secondary); color: var(--primary); }
}
.cat-icon { font-size: 0.95rem; width: 22px; text-align: center; flex-shrink: 0; }
.cat-name { flex: 1; }
.cat-count {
  font-size: 0.72rem; padding: 2px 8px;
  background: var(--bg-secondary); border-radius: 10px;
  color: var(--text-muted); font-weight: 500;
}

/* ===== FEED MAIN (Facebook-style) ===== */
.feed-main { min-width: 0; display: flex; flex-direction: column; gap: 16px; }

.feed-tabs {
  display: flex; gap: 0;
  background: var(--surface); border: 1px solid var(--border-light);
  border-radius: 8px; overflow: hidden;
}
.feed-tab {
  flex: 1; padding: 12px 0; border: none;
  background: transparent; border-bottom: 2px solid transparent;
  font-size: 0.875rem; font-weight: 500;
  color: var(--text-muted); cursor: pointer;
  transition: all 0.15s;
  &:hover { color: var(--text-primary); background: var(--bg-secondary); }
  &.feed-tab--active {
    color: var(--primary); font-weight: 600;
    border-bottom-color: var(--primary);
  }
}

.feed-list { display: flex; flex-direction: column; gap: 12px; }

.feed-card {
  background: var(--surface); border: 1px solid var(--border-light);
  border-radius: 8px; overflow: hidden;
}
.feed-card-header {
  display: flex; align-items: center; gap: 10px;
  padding: 16px 20px 0;
}
.feed-card-avatar-link { flex-shrink: 0; text-decoration: none; }
.feed-card-avatar { width: 40px; height: 40px; border-radius: 50%; object-fit: cover; }
.feed-card-avatar--placeholder {
  display: flex; align-items: center; justify-content: center;
  background: linear-gradient(135deg, var(--primary), #8b5cf6);
  color: #fff; font-weight: 700; font-size: 0.85rem;
}
.feed-card-meta { flex: 1; min-width: 0; }
.feed-card-author {
  font-weight: 600; font-size: 0.9rem; color: var(--text-primary);
  text-decoration: none; display: block; line-height: 1.3;
  &:hover { text-decoration: underline; }
}
.feed-card-sub {
  display: flex; align-items: center; gap: 6px;
  font-size: 0.8rem; color: var(--text-muted); margin-top: 2px;
}

.feed-card-body { padding: 12px 20px 0; }
.feed-card-title {
  font-size: 1.05rem; font-weight: 700; color: var(--text-primary);
  text-decoration: none; line-height: 1.45; display: block; margin-bottom: 6px;
  &:hover { color: var(--primary); }
}
.feed-card-desc {
  font-size: 0.875rem; color: var(--text-secondary); line-height: 1.55;
  display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical;
  overflow: hidden; margin-bottom: 4px;
}

.feed-card-cover {
  padding: 12px 20px 0;
  img { width: 100%; height: 240px; object-fit: cover; border-radius: 6px; }
}

.feed-card-footer {
  padding: 12px 20px 16px;
}
.feed-card-stats {
  display: flex; gap: 16px; padding-bottom: 10px;
  border-bottom: 1px solid var(--border-light); margin-bottom: 8px;
}
.feed-card-stat {
  display: flex; align-items: center; gap: 5px;
  font-size: 0.8rem; color: var(--text-muted);
  svg { opacity: 0.5; }
}
.feed-card-actions {
  display: flex; gap: 4px;
}
.feed-card-action {
  display: flex; align-items: center; gap: 6px;
  padding: 8px 12px; border: none; background: transparent;
  border-radius: 6px; font-size: 0.85rem; font-weight: 500;
  color: var(--text-muted); cursor: pointer;
  transition: all 0.12s;
  svg { width: 16px; height: 16px; }
  &:hover { background: var(--bg-secondary); color: var(--text-primary); }
  &.feed-card-action--active { color: var(--primary); background: var(--primary-50); }
}

.feed-load-more {
  display: flex; justify-content: center; padding: 20px 0;
}
.load-more-btn {
  display: flex; align-items: center; gap: 8px;
  padding: 10px 28px; border: 1px solid var(--border);
  background: var(--surface); border-radius: 6px;
  font-size: 0.875rem; font-weight: 500; color: var(--text-secondary);
  cursor: pointer; transition: all 0.15s;
  &:hover { border-color: var(--primary); color: var(--primary); }
  &:disabled { opacity: 0.6; cursor: not-allowed; }
}
.load-more-spinner {
  width: 14px; height: 14px; border: 2px solid var(--border);
  border-top-color: var(--primary); border-radius: 50%;
  animation: spin 0.6s linear infinite;
}
@keyframes spin { to { transform: rotate(360deg); } }

/* ===== RIGHT SIDEBAR (CSDN-style) ===== */
.sidebar-right { display: flex; flex-direction: column; gap: 16px; }

.trending-item {
  display: flex; align-items: flex-start; gap: 10px;
  padding: 10px 0; border-bottom: 1px solid var(--border-light);
  text-decoration: none; transition: all 0.12s;
  &:last-child { border-bottom: none; }
  &:hover .trending-title { color: var(--primary); }
}
.trending-rank {
  width: 22px; height: 22px; border-radius: 4px;
  display: flex; align-items: center; justify-content: center;
  font-size: 0.72rem; font-weight: 700; flex-shrink: 0; margin-top: 1px;
  background: var(--bg-secondary); color: var(--text-muted);
  &.trending-rank--top {
    background: var(--primary); color: #fff;
  }
}
.trending-info { flex: 1; min-width: 0; }
.trending-title {
  display: block; font-size: 0.85rem; font-weight: 500;
  color: var(--text-primary); line-height: 1.4;
  display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden;
  transition: color 0.12s;
}
.trending-views { font-size: 0.75rem; color: var(--text-muted); margin-top: 2px; display: block; }

.tag-cloud { display: flex; flex-wrap: wrap; gap: 6px; }
.tag-item {
  display: inline-block; padding: 5px 12px;
  background: var(--bg-secondary); border-radius: 4px;
  font-size: 0.8rem; color: var(--text-secondary); text-decoration: none;
  transition: all 0.12s;
  &:hover { background: var(--primary-50); color: var(--primary); }
}
</style>
