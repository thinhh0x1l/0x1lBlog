<template>
  <div class="comment-section">
    <h4 class="section-title">Bình luận</h4>

    <div class="comment-list" v-if="comments.length">
      <div v-for="c in comments" :key="c.id" class="comment-item">
        <img :src="c.authorAvatar || avatarFallback" class="comment-avatar" />
        <div class="comment-body">
          <span class="comment-author">{{ c.authorName }}</span>
          <p class="comment-text">{{ c.content }}</p>
          <span class="comment-time">{{ formatTime(c.createdAt) }}</span>

          <div v-for="child in c.children" :key="child.id" class="comment-item comment-reply">
            <img :src="child.authorAvatar || avatarFallback" class="comment-avatar comment-avatar-sm" />
            <div class="comment-body">
              <span class="comment-author">{{ child.authorName }}</span>
              <p class="comment-text">{{ child.content }}</p>
              <span class="comment-time">{{ formatTime(child.createdAt) }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-else class="comment-empty">Chưa có bình luận nào.</div>

    <div class="comment-compose" v-if="isLoggedIn">
      <textarea v-model="text" placeholder="Viết bình luận..." class="comment-input" rows="2"></textarea>
      <button class="comment-submit" @click="postComment" :disabled="!text.trim()">Gửi</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { fromNow } from '~/utils/time'

const props = defineProps({
  songId: { type: [Number, String], required: true },
  isLoggedIn: { type: Boolean, default: false },
})

const comments = ref<any[]>([])
const text = ref('')
const avatarFallback = 'https://api.dicebear.com/7.x/avataaars/svg?seed=guest'
const api = useApi()

const loadComments = async () => {
  try {
    const res: any = await api.get(`/music/comments?songId=${props.songId}`)
    comments.value = res.data || res || []
  } catch (e) { console.error(e) }
}

const postComment = async () => {
  if (!text.value.trim()) return
  try {
    await api.post(`/music/comments`, { songId: props.songId, content: text.value })
    text.value = ''
    await loadComments()
  } catch (e) { console.error(e) }
}

const formatTime = (d: string) => fromNow(d)

watch(() => props.songId, loadComments)
onMounted(loadComments)
</script>

<style lang="scss" scoped>
.comment-section { padding: 0; }

.section-title {
  font-size: 0.95rem;
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: var(--space-md);
  display: flex;
  align-items: center;
  gap: 6px;

  &::before {
    content: '';
    width: 3px;
    height: 18px;
    background: linear-gradient(180deg, var(--primary), var(--info));
    border-radius: var(--radius-full);
  }
}

.comment-list { display: flex; flex-direction: column; gap: 12px; margin-bottom: var(--space-md); }
.comment-item { display: flex; gap: 10px; }
.comment-reply { margin-top: 8px; margin-left: 36px; padding: 8px; background: var(--bg-secondary); border-radius: var(--radius-md); }
.comment-avatar { width: 36px; height: 36px; border-radius: 50%; object-fit: cover; flex-shrink: 0; }
.comment-avatar-sm { width: 28px; height: 28px; }
.comment-body { flex: 1; min-width: 0; }
.comment-author { font-size: 0.82rem; font-weight: 600; color: var(--text-primary); display: block; margin-bottom: 2px; }
.comment-text { font-size: 0.85rem; color: var(--text-secondary); line-height: 1.45; margin-bottom: 2px; }
.comment-time { font-size: 0.68rem; color: var(--text-muted); }
.comment-empty { text-align: center; padding: var(--space-lg) 0; color: var(--text-muted); font-size: 0.85rem; }
.comment-compose { display: flex; gap: 8px; align-items: flex-start; }
.comment-input { flex: 1; padding: 8px 12px; border: 1px solid var(--border-light); border-radius: var(--radius-md); background: var(--bg-secondary); color: var(--text-primary); font-size: 0.85rem; resize: none; font-family: inherit; &:focus { outline: none; border-color: var(--primary); } }
.comment-submit { padding: 8px 16px; background: var(--primary); color: white; border: none; border-radius: var(--radius-full); font-size: 0.82rem; font-weight: 600; cursor: pointer; transition: all 0.15s ease; &:hover { background: var(--primary-dark); } &:disabled { opacity: 0.5; cursor: not-allowed; } }
</style>
