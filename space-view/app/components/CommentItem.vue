<template>
  <div class="comment-item">
    <img :src="comment.authorAvatar || fallbackAvatar" class="comment-avatar" />
    <div class="comment-body">
      <div class="comment-header">
        <span class="comment-author">{{ comment.authorName }}</span>
        <span class="comment-time">{{ fromNow(comment.createdAt) }}</span>
      </div>
      <p class="comment-text">{{ comment.content }}</p>
      <div class="comment-actions">
        <button class="action-btn" @click="$emit('reply', comment)">Trả lời</button>
        <button class="action-btn" @click="$emit('like', comment)">
          {{ comment.isLiked ? '❤️' : '🤍' }} {{ comment.likeCount || 0 }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { fromNow } from '~/utils/time'

defineProps({
  comment: { type: Object, required: true },
})

defineEmits(['reply', 'like'])

const fallbackAvatar = 'https://api.dicebear.com/7.x/avataaars/svg?seed=guest'
</script>

<style scoped lang="scss">
.comment-item {
  display: flex;
  gap: 12px;
  padding: 16px 0;
  border-bottom: 1px solid var(--border-light);

  &:last-child {
    border-bottom: none;
  }
}

.comment-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
  flex-shrink: 0;
}

.comment-body {
  flex: 1;
  min-width: 0;
}

.comment-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 4px;
}

.comment-author {
  font-size: 0.88rem;
  font-weight: 600;
  color: var(--text-primary);
}

.comment-time {
  font-size: 0.72rem;
  color: var(--text-muted);
}

.comment-text {
  font-size: 0.9rem;
  color: var(--text-secondary);
  line-height: 1.5;
  margin: 0 0 8px 0;
}

.comment-actions {
  display: flex;
  gap: 16px;
}

.action-btn {
  background: none;
  border: none;
  font-size: 0.78rem;
  color: var(--text-muted);
  cursor: pointer;
  padding: 0;

  &:hover {
    color: var(--primary);
  }
}
</style>
