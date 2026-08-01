<template>
  <div>
    <template v-if="blog">
      <ThreeColumnLayout>
        <template #sidebar-left>
          <BlogSidebar
            :author="author"
            :content="blog.content"
            :is-following="isFollowing"
            :reputation="reputation"
            :streak="streakCount"
            :badges="authorBadges"
            :author-status="authorStatus"
            :author-stories="authorStories"
            :equipped-border="author?.equippedItems?.border"
            :rolltext="author?.equippedItems?.rolltext"
            :active-mischief="author?.activeMischief"
            @toggle-follow="toggleFollow"
            @view-story="viewStory"
          />
        </template>

        <div class="blog-main">
          <div class="blog-header">
            <div class="blog-category" v-if="blog.categoryName">
              <NuxtLink :to="`/category/${blog.categoryName}`">{{ blog.categoryName }}</NuxtLink>
            </div>
            <h1 class="blog-title">{{ blog.title }}</h1>
            <div class="blog-meta">
              <div class="meta-left">
                <NuxtLink :to="`/profile/${blog.authorId}`" class="author-info">
                  <el-avatar :size="40" :src="blog.authorAvatar">{{ blog.authorName?.charAt(0) }}</el-avatar>
                  <div class="author-detail">
                    <span class="author-name">{{ blog.authorName }}</span>
                    <span class="meta-date">{{ formatDate(blog.publishedAt) }} · {{ blog.readTime }} phút đọc</span>
                  </div>
                </NuxtLink>
              </div>
              <div class="blog-stats">
                <span>{{ blog.views }} views</span>
                <span>{{ blog.commentCount }} comments</span>
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
              <el-button :type="isBookmarked ? 'primary' : 'default'" @click="toggleBookmark">⭐ {{ isBookmarked ? 'Đã lưu' : 'Lưu' }}</el-button>
              <el-button @click="shareBlog">🔗 Chia sẻ</el-button>
            </div>
          </div>

          <AuthorCta v-if="author && currentUserId !== author.id" :author="author" :is-following="isFollowing" @follow="toggleFollow" />

          <div id="comments" class="comments-section">
            <h3>Bình luận ({{ comments.length }})</h3>
            <div class="comment-form" v-if="isLoggedIn">
              <el-input v-model="commentContent" type="textarea" :rows="3" placeholder="Viết bình luận..." />
              <div class="form-actions"><el-button type="primary" @click="submitComment" :loading="commentLoading">Gửi</el-button></div>
            </div>
            <div class="comment-form" v-else>
              <el-button type="primary" @click="navigateTo('/login')">Đăng nhập để bình luận</el-button>
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
                  <el-button text size="small">⭐ {{ comment.likeCount || 0 }}</el-button>
                  <el-button text size="small">💬 Trả lời</el-button>
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

          <RelatedPosts v-if="relatedPosts.length" :posts="relatedPosts" />
        </div>

        <template #sidebar-right>
          <BlogDiscoverySidebar
            :playlist="authorPlaylist"
            :canvas="authorCanvas"
            :quests="myQuests"
            :challenge="todayBlind"
            :my-guess="myBlindGuess"
            :skill-trees="categorySkills"
            :skill-progress="mySkillProgress"
            :trending="trendingPosts"
            :tags="blogTags"
            :is-logged-in="isLoggedIn"
            @claim="handleClaim"
            @guess="handleBlindGuess"
            @viewCanvas="openCanvas"
          />
        </template>
      </ThreeColumnLayout>

      <StoryList />
      <StoryViewer />
      <CanvasViewer v-if="canvasViewerVisible" :canvas="viewingCanvas" :initial-strokes="[]" @close="canvasViewerVisible = false" @stroke="handleCanvasStroke" />
    </template>
    <el-skeleton v-else-if="loading" :rows="10" animated />
    <el-empty v-else description="Không tìm thấy bài viết" />
  </div>
</template>
<script setup>
import { ref, computed } from 'vue'
import { blogs as mockBlogs, users as mockUsers, comments as mockComments } from '~/utils/dummy'
import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'
import 'dayjs/locale/vi'

dayjs.extend(relativeTime)
dayjs.locale('vi')

definePageMeta({ layout: 'default' })

const route = useRoute()
const loading = ref(false)

const blogId = Number(route.params.id)
const blog = ref(mockBlogs.find(b => b.id === blogId) || null)

if (blog.value) {
  useHead({ title: `${blog.value.title} - 0x1lBlog` })
  const author = mockUsers.find(u => u.id === blog.value.authorId)
  if (author) {
    blog.value.authorName = author.displayName
    blog.value.authorAvatar = author.avatarUrl
  }
}

const author = ref(blog.value ? mockUsers.find(u => u.id === blog.value.authorId) : null)
const comments = ref(mockComments.filter(c => c.blogId === blogId && !c.parentId))
const commentContent = ref('')
const commentLoading = ref(false)
const userReaction = ref(null)
const isBookmarked = ref(false)
const isFollowing = ref(false)
const contentRef = ref(null)
const canvasViewerVisible = ref(false)
const viewingCanvas = ref(null)

const reputation = ref(null)
const streakCount = ref(0)
const authorBadges = ref([])
const authorStatus = ref(null)
const authorStories = ref([])
const authorPlaylist = ref(null)
const authorCanvas = ref(null)
const myQuests = ref([])
const todayBlind = ref(null)
const myBlindGuess = ref(null)
const categorySkills = ref([])
const mySkillProgress = ref(null)
const trendingPosts = ref([])
const blogTags = ref([])
const relatedPosts = ref([])

const reactions = [
  { type: 'LIKE', icon: '👍', key: 'likeCount' },
  { type: 'LOVE', icon: '❤️', key: 'loveCount' },
  { type: 'HAHA', icon: '😂', key: 'hahaCount' },
  { type: 'WOW', icon: '😮', key: 'wowCount' },
  { type: 'SAD', icon: '😢', key: 'sadCount' },
  { type: 'ANGRY', icon: '😡', key: 'angryCount' },
]

const renderedContent = ref('')

const renderMarkdown = async (content) => {
  if (!content) return ''
  if (import.meta.client) {
    const { marked } = await import('marked')
    const { default: DOMPurify } = await import('dompurify')
    return DOMPurify.sanitize(marked.parse(content))
  }
  return content
}

watch(blog, async (newBlog) => {
  if (newBlog?.content) {
    renderedContent.value = await renderMarkdown(newBlog.content)
  }
}, { immediate: true })

const formatDate = (d) => {
  return dayjs(d).fromNow()
}

const toggleReaction = async (type) => {
  try {
    await $fetch(`/api/reactions/toggle`, { method: 'POST', body: { blogId: blog.value.id, type } })
    userReaction.value = userReaction.value === type ? null : type
  } catch (e) { console.error(e) }
}

const toggleBookmark = async () => {
  try {
    await $fetch('/api/bookmarks/toggle', { method: 'POST', body: { blogId: blog.value.id } })
    isBookmarked.value = !isBookmarked.value
  } catch (e) { console.error(e) }
}

const toggleFollow = async () => {
  try {
    if (isFollowing.value) await $fetch(`/api/follows/${blog.value.authorId}`, { method: 'DELETE' })
    else await $fetch(`/api/follows/${blog.value.authorId}`, { method: 'POST' })
    isFollowing.value = !isFollowing.value
  } catch (e) { console.error(e) }
}

const shareBlog = () => { navigator.clipboard.writeText(window.location.href) }

const submitComment = async () => {
  if (!commentContent.value.trim()) return
  commentLoading.value = true
  try {
    await $fetch('/api/comments', { method: 'POST', body: { blogId: blog.value.id, content: commentContent.value } })
    commentContent.value = ''
    loadComments()
  } catch (e) { console.error(e) }
  finally { commentLoading.value = false }
}

const loadComments = async () => {
  try {
    const res = await $fetch(`/api/comments/blog/${route.params.id}`)
    comments.value = res || []
  } catch (e) {}
}

const viewStory = () => {}
const handleClaim = async (userQuestId) => {
  try { await $fetch(`/api/quests/${userQuestId}/claim`, { method: 'POST' }) } catch (e) {}
}
const openCanvas = (c) => { viewingCanvas.value = c; canvasViewerVisible.value = true }
const handleCanvasStroke = (stroke) => {}

const handleBlindGuess = async (data) => {
  try {
    const res = await $fetch(`/api/blind/${data.challengeId}/guess`, { method: 'POST', body: { guessedTopicId: data.guessedTopicId } })
    myBlindGuess.value = res
  } catch (e) {}
}

if (blog.value) {
  const authorId = blog.value.authorId

  const { data: sideData } = await useAsyncData(`blog-side-${route.params.id}`, async () => {
    const [commentRes, repRes, profileRes, statusRes, storyRes, playlistRes, canvasRes, questRes, blindRes, skillRes, relatedRes, trendRes, tagRes, skillProgressRes] = await Promise.all([
      $fetch(`/api/comments/blog/${route.params.id}`).catch(() => []),
      $fetch(`/api/reputation/user/${authorId}`).catch(() => null),
      $fetch(`/api/profile/${authorId}`).catch(() => null),
      $fetch(`/api/status/user/${authorId}`).catch(() => []),
      $fetch(`/api/stories/user/${authorId}`).catch(() => []),
      $fetch(`/api/playlists/user/${authorId}`).catch(() => null),
      $fetch(`/api/canvases/user/${authorId}`).catch(() => null),
      $fetch('/api/quests/my').catch(() => []),
      $fetch('/api/blind/today').catch(() => null),
      $fetch(`/api/skills/category/${blog.value.categoryId}`).catch(() => []),
      $fetch(`/api/blogs/author/${authorId}`).catch(() => []),
      $fetch('/api/blogs/trending?limit=3').catch(() => []),
      $fetch('/api/hashtags/top?limit=10').catch(() => []),
      $fetch(`/api/skills/my-progress?categoryId=${blog.value.categoryId}`).catch(() => null),
    ])
    return { commentRes, repRes, profileRes, statusRes, storyRes, playlistRes, canvasRes, questRes, blindRes, skillRes, relatedRes, trendRes, tagRes, skillProgressRes }
  })

  if (sideData.value) {
    const d = sideData.value
    comments.value = d.commentRes || []
    reputation.value = d.repRes
    author.value = d.profileRes
    authorStatus.value = (d.statusRes || [])[0]
    authorStories.value = d.storyRes || []
    authorPlaylist.value = d.playlistRes
    authorCanvas.value = d.canvasRes
    myQuests.value = d.questRes || []
    todayBlind.value = d.blindRes
    categorySkills.value = d.skillRes || []
    relatedPosts.value = (d.relatedRes || []).filter(p => p.id !== blog.value.id).slice(0, 3)
    trendingPosts.value = d.trendRes || []
    blogTags.value = d.tagRes || []
    streakCount.value = d.repRes?.level || 0
    mySkillProgress.value = d.skillProgressRes
  }
}
</script>
<style scoped lang="scss">
.blog-main { min-width: 0; }

.blog-header {
  margin-bottom: var(--space-xl);
  padding-bottom: var(--space-xl);
  border-bottom: 1px solid var(--border-light);
}

.blog-category {
  margin-bottom: var(--space-md);
  a {
    display: inline-block;
    padding: 6px 16px;
    background: var(--primary-50);
    color: var(--primary);
    border-radius: var(--radius-full);
    font-size: 0.8rem;
    font-weight: 600;
    text-decoration: none;
    transition: all 0.2s var(--ease-out);
    &:hover {
      background: var(--primary-100);
      transform: translateY(-1px);
    }
  }
}

.blog-title {
  font-size: 2.25rem;
  font-weight: 800;
  line-height: 1.3;
  margin-bottom: var(--space-lg);
  letter-spacing: -0.03em;
  color: var(--text-primary);
}

.blog-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 12px;
  text-decoration: none;
}

.author-name {
  font-weight: 600;
  color: var(--text-primary);
  display: block;
  font-size: 0.95rem;
}

.meta-date {
  font-size: 0.85rem;
  color: var(--text-muted);
}

.blog-stats {
  display: flex;
  gap: var(--space-lg);
  span {
    display: flex;
    align-items: center;
    gap: 6px;
    font-size: 0.875rem;
    color: var(--text-muted);
    font-weight: 500;
  }
}

.blog-cover {
  margin-bottom: var(--space-xl);
  border-radius: var(--radius-2xl);
  overflow: hidden;
  box-shadow: var(--shadow-lg);
  img {
    width: 100%;
    max-height: 460px;
    object-fit: cover;
    transition: transform 0.6s var(--ease-out);
  }
  &:hover img {
    transform: scale(1.02);
  }
}

.blog-content {
  background: var(--surface);
  border-radius: var(--radius-2xl);
  box-shadow: var(--shadow);
  padding: var(--space-3xl);
  margin-bottom: var(--space-xl);
  font-size: 1.0625rem;
  line-height: 1.85;
  border: 1px solid var(--border-light);
  letter-spacing: 0.01em;
}

.reaction-bar {
  background: var(--surface);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-2xl);
  box-shadow: var(--shadow-sm);
  padding: var(--space-lg);
  margin-bottom: var(--space-xl);
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: var(--space-md);
}

.reaction-buttons { display: flex; gap: var(--space-sm); }

.reaction-btn {
  padding: 10px 18px;
  border: 1px solid transparent;
  background: var(--bg-secondary);
  border-radius: var(--radius-full);
  cursor: pointer;
  font-size: 1.15rem;
  transition: all 0.2s var(--ease-out);
  display: flex;
  align-items: center;
  gap: 8px;
  &:hover {
    background: var(--primary-50);
    border-color: var(--primary-100);
    transform: scale(1.08);
  }
  &.active {
    background: var(--primary-50);
    border-color: var(--primary);
    box-shadow: 0 2px 8px rgba(14, 165, 233, 0.2);
  }
  span {
    font-size: 0.85rem;
    color: var(--text-muted);
    font-weight: 600;
  }
}

.comments-section {
  background: var(--surface);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-2xl);
  box-shadow: var(--shadow-sm);
  padding: var(--space-2xl);
  h3 {
    font-size: 1.25rem;
    font-weight: 700;
    margin-bottom: var(--space-xl);
    color: var(--text-primary);
  }
}

.comment-form {
  margin-bottom: var(--space-xl);
  padding-bottom: var(--space-xl);
  border-bottom: 1px solid var(--border-light);
}

.form-actions { display: flex; justify-content: flex-end; margin-top: var(--space-md); }

.comment-list { display: flex; flex-direction: column; gap: var(--space-xl); }

.comment-item {
  padding-bottom: var(--space-lg);
  border-bottom: 1px solid var(--border-light);
  &:last-child { border-bottom: none; }
}

.comment-header { display: flex; align-items: center; gap: 12px; margin-bottom: 10px; }
.comment-meta { display: flex; flex-direction: column; gap: 2px; }
.comment-author { font-size: 0.9rem; font-weight: 600; color: var(--text-primary); }
.comment-time { font-size: 0.78rem; color: var(--text-muted); }
.comment-content { font-size: 0.9375rem; color: var(--text-secondary); line-height: 1.65; margin-bottom: 10px; padding-left: 52px; }
.comment-actions { padding-left: 52px; }
.comment-replies { margin-top: var(--space-lg); padding-left: 52px; border-left: 2px solid var(--primary-100); }
.reply-item { padding: var(--space-sm) 0; }
.reply-item .comment-content { padding-left: 44px; }
</style>
