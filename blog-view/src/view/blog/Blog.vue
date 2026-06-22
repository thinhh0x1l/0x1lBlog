<template>
  <div class="blog-layout" v-if="blog">
    <!-- LEFT SIDEBAR: Author + TOC -->
    <BlogSidebar :author="author" :content="blog.content" :isFollowing="isFollowing" @toggleFollow="toggleFollow" />

    <!-- RIGHT: Content -->
    <div class="blog-main">
      <div class="blog-header">
        <div class="blog-category" v-if="blog.categoryName">
          <router-link :to="`/category/${blog.categoryName}`">{{ blog.categoryName }}</router-link>
        </div>
        <h1 class="blog-title">{{ blog.title }}</h1>
        <div class="blog-meta">
          <div class="meta-left">
            <router-link :to="`/profile/${blog.authorId}`" class="author-info">
              <el-avatar :size="40" :src="blog.authorAvatar">{{ blog.authorName?.charAt(0) }}</el-avatar>
              <div class="author-detail">
                <span class="author-name">{{ blog.authorName }}</span>
                <span class="meta-date">{{ formatDate(blog.publishedAt) }} · {{ blog.readTime }} phút đọc</span>
              </div>
            </router-link>
          </div>
          <div class="blog-stats">
            <span><el-icon><View /></el-icon> {{ blog.views }}</span>
            <span><el-icon><ChatDotRound /></el-icon> {{ blog.commentCount }}</span>
          </div>
        </div>
      </div>

      <div class="blog-cover" v-if="blog.coverImage">
        <img :src="blog.coverImage" :alt="blog.title" />
      </div>

      <div class="blog-content typo" v-html="renderedContent" ref="contentRef"></div>

      <div class="reaction-bar">
        <div class="reaction-buttons">
          <button v-for="r in reactions" :key="r.type" :class="['reaction-btn', { active: userReaction === r.type }]" @click="toggleReaction(r.type)">
            {{ r.icon }} <span>{{ blog[r.key] || 0 }}</span>
          </button>
        </div>
        <div class="action-buttons">
          <el-button :type="isBookmarked ? 'primary' : 'default'" @click="toggleBookmark"><el-icon><Star /></el-icon> {{ isBookmarked ? 'Đã lưu' : 'Lưu' }}</el-button>
          <el-button @click="shareBlog"><el-icon><Share /></el-icon> Chia sẻ</el-button>
        </div>
      </div>

      <div id="comments" class="comments-section">
        <h3>Bình luận ({{ comments.length }})</h3>
        <div class="comment-form" v-if="isLoggedIn">
          <el-input v-model="commentContent" type="textarea" :rows="3" placeholder="Viết bình luận..." />
          <div class="form-actions"><el-button type="primary" @click="submitComment" :loading="commentLoading">Gửi</el-button></div>
        </div>
        <div class="comment-form" v-else>
          <el-button type="primary" @click="$router.push('/login')">Đăng nhập để bình luận</el-button>
        </div>
        <div class="comment-list">
          <div v-for="comment in comments" :key="comment.id" class="comment-item">
            <div class="comment-header">
              <el-avatar :size="36" :src="comment.authorAvatar">{{ comment.authorName?.charAt(0) || 'G' }}</el-avatar>
              <div class="comment-meta">
                <span class="comment-author">{{ comment.authorName || comment.guestName || 'Khách' }}</span>
                <span class="comment-time">{{ formatDate(comment.createdAt) }}</span>
              </div>
            </div>
            <div class="comment-content">{{ comment.content }}</div>
            <div class="comment-actions">
              <el-button text size="small"><el-icon><Star /></el-icon> {{ comment.likeCount || 0 }}</el-button>
              <el-button text size="small"><el-icon><ChatRound /></el-icon> Trả lời</el-button>
            </div>
            <div class="comment-replies" v-if="comment.children?.length">
              <div v-for="reply in comment.children" :key="reply.id" class="reply-item">
                <div class="comment-header">
                  <el-avatar :size="28" :src="reply.authorAvatar">{{ reply.authorName?.charAt(0) || 'G' }}</el-avatar>
                  <div class="comment-meta">
                    <span class="comment-author">{{ reply.authorName || reply.guestName }}</span>
                    <span class="comment-time">{{ formatDate(reply.createdAt) }}</span>
                  </div>
                </div>
                <div class="comment-content">{{ reply.content }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <el-skeleton :rows="10" animated v-else-if="loading" />
  <el-empty v-else description="Không tìm thấy bài viết" />
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { View, ChatDotRound, Star, Share, ChatRound } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { marked } from 'marked'
import DOMPurify from 'dompurify'
import { useAuthStore } from '@/store/auth'
import { blogApi, commentApi, reactionApi, bookmarkApi, followApi, profileApi } from '@/api'
import BlogSidebar from '@/components/blog/BlogSidebar.vue'
import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'
import 'dayjs/locale/vi'
dayjs.extend(relativeTime)
dayjs.locale('vi')

const route = useRoute()
const authStore = useAuthStore()
const isLoggedIn = computed(() => authStore.isLoggedIn)
const currentUserId = computed(() => authStore.user?.id)

const blog = ref(null)
const author = ref(null)
const comments = ref([])
const loading = ref(true)
const commentContent = ref('')
const commentLoading = ref(false)
const userReaction = ref(null)
const isBookmarked = ref(false)
const isFollowing = ref(false)
const contentRef = ref(null)

const reactions = [
  { type: 'LIKE', icon: '👍', key: 'likeCount' },
  { type: 'LOVE', icon: '❤️', key: 'loveCount' },
  { type: 'HAHA', icon: '😂', key: 'hahaCount' },
  { type: 'WOW', icon: '😮', key: 'wowCount' },
  { type: 'SAD', icon: '😢', key: 'sadCount' },
  { type: 'ANGRY', icon: '😡', key: 'angryCount' },
]

const renderedContent = computed(() => blog.value?.content ? DOMPurify.sanitize(marked.parse(blog.value.content)) : '')
const formatDate = (d) => dayjs(d).fromNow()

const toggleReaction = async (type) => {
  try { await reactionApi.toggle(blog.value.id, type); userReaction.value = userReaction.value === type ? null : type; ElMessage.success('Đã react') }
  catch (e) { console.error(e) }
}

const toggleBookmark = async () => {
  try { await bookmarkApi.toggle({ blogId: blog.value.id }); isBookmarked.value = !isBookmarked.value; ElMessage.success(isBookmarked.value ? 'Đã lưu' : 'Đã bỏ lưu') }
  catch (e) { console.error(e) }
}

const toggleFollow = async () => {
  try { if (isFollowing.value) await followApi.unfollow(blog.value.authorId); else await followApi.follow(blog.value.authorId); isFollowing.value = !isFollowing.value }
  catch (e) { console.error(e) }
}

const shareBlog = () => { navigator.clipboard.writeText(window.location.href); ElMessage.success('Đã copy link') }

const submitComment = async () => {
  if (!commentContent.value.trim()) return
  commentLoading.value = true
  try { await commentApi.create({ blogId: blog.value.id, content: commentContent.value }); commentContent.value = ''; ElMessage.success('Bình luận thành công'); loadComments() }
  catch (e) { ElMessage.error('Lỗi') }
  finally { commentLoading.value = false }
}

const loadComments = async () => {
  try { const res = await commentApi.getByBlog(route.params.id); comments.value = res.data || [] } catch (e) {}
}

onMounted(async () => {
  loading.value = true
  try {
    const blogRes = await blogApi.getById(route.params.id)
    blog.value = blogRes.data
    if (blog.value) {
      await Promise.all([
        loadComments(),
        blogApi.incrementView(route.params.id),
        profileApi.getPublic(blog.value.authorId).then(r => author.value = r.data),
      ])
    }
  } catch (e) { console.error(e) }
  finally { loading.value = false }
})
</script>

<style scoped lang="scss">
.blog-layout {
  display: grid;
  grid-template-columns: 320px 1fr;
  gap: 32px;
  max-width: 100%;
}

.blog-main {
  flex: 1;
  min-width: 0;
}

.blog-header {
  margin-bottom: var(--space-lg);
  padding-bottom: var(--space-lg);
  border-bottom: 1px solid var(--border-light);
}

.blog-category {
  margin-bottom: var(--space-md);

  a {
    display: inline-block;
    padding: 5px 14px;
    background: var(--primary-50);
    color: var(--primary);
    border-radius: var(--radius-full);
    font-size: 0.8rem;
    font-weight: 600;
    text-decoration: none;
    transition: all var(--duration-fast) ease;

    &:hover {
      background: var(--primary-100);
    }
  }
}

.blog-title {
  font-size: 2rem;
  font-weight: 800;
  line-height: 1.3;
  margin-bottom: var(--space-md);
  letter-spacing: -0.02em;
}

.blog-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.author-info {
  display: flex;
  align-items: center;
  gap: var(--space-sm);
  text-decoration: none;
}

.author-name { font-weight: 600; color: var(--text-primary); display: block; }
.meta-date { font-size: 0.8rem; color: var(--text-muted); }
.blog-stats {
  display: flex;
  gap: var(--space-md);

  span {
    display: flex;
    align-items: center;
    gap: 4px;
    font-size: 0.85rem;
    color: var(--text-muted);
  }
}

.blog-cover {
  margin-bottom: var(--space-lg);
  border-radius: var(--radius-xl);
  overflow: hidden;
  box-shadow: var(--shadow-md);

  img {
    width: 100%;
    max-height: 400px;
    object-fit: cover;
    transition: transform var(--duration-slow) var(--ease-out);

    &:hover {
      transform: scale(1.01);
    }
  }
}

.blog-content {
  background: var(--surface);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow);
  padding: var(--space-2xl);
  margin-bottom: var(--space-lg);
  font-size: 1rem;
  line-height: 1.8;
  border: 1px solid var(--border-light);
}

.reaction-bar {
  background: var(--surface);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-sm);
  padding: var(--space-md) var(--space-lg);
  margin-bottom: var(--space-lg);
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: var(--space-sm);
}

.reaction-buttons {
  display: flex;
  gap: var(--space-xs);
}

.reaction-btn {
  padding: 8px 14px;
  border: 1px solid transparent;
  background: transparent;
  border-radius: var(--radius);
  cursor: pointer;
  font-size: 1.1rem;
  transition: all var(--duration-fast) ease;
  display: flex;
  align-items: center;
  gap: 6px;

  &:hover {
    background: var(--surface-hover);
    border-color: var(--border-light);
    transform: scale(1.05);
  }

  &.active {
    background: var(--primary-50);
    border-color: var(--primary-100);
  }

  span {
    font-size: 0.8rem;
    color: var(--text-muted);
    font-weight: 500;
  }
}

.comments-section {
  background: var(--surface);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-sm);
  padding: var(--space-2xl);

  h3 {
    font-size: 1.15rem;
    font-weight: 700;
    margin-bottom: var(--space-lg);
  }
}

.comment-form {
  margin-bottom: var(--space-lg);
  padding-bottom: var(--space-lg);
  border-bottom: 1px solid var(--border-light);
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: var(--space-sm);
}

.comment-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-lg);
}

.comment-item {
  padding-bottom: var(--space-md);
  border-bottom: 1px solid var(--border-light);

  &:last-child { border-bottom: none; }
}

.comment-header {
  display: flex;
  align-items: center;
  gap: var(--space-sm);
  margin-bottom: 8px;
}

.comment-meta { display: flex; flex-direction: column; }
.comment-author { font-size: 0.9rem; font-weight: 600; }
.comment-time { font-size: 0.75rem; color: var(--text-muted); }
.comment-content { font-size: 0.9rem; color: var(--text-secondary); line-height: 1.6; margin-bottom: 8px; padding-left: 48px; }
.comment-actions { padding-left: 48px; }
.comment-replies { margin-top: var(--space-md); padding-left: 48px; border-left: 2px solid var(--border); }
.reply-item { padding: var(--space-sm) 0; }
.reply-item .comment-content { padding-left: 40px; }

@include respond-to(lg) {
  .blog-layout { grid-template-columns: 1fr; }
  .blog-sidebar { display: none; }
}
</style>
