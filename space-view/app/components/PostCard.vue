<template>
  <NuxtLink :to="`/blog/${post.id}`" class="post-card">
    <div class="post-cover" v-if="post.coverImage">
      <img :src="post.coverImage" :alt="post.title" />
    </div>
    <div class="post-body">
      <div class="post-meta">
        <span class="post-category" v-if="post.categoryName">{{ post.categoryName }}</span>
        <span class="post-date">{{ formatDate(post.publishedAt) }}</span>
      </div>
      <h3 class="post-title">{{ post.title }}</h3>
      <p class="post-excerpt" v-if="post.excerpt">{{ post.excerpt }}</p>
      <div class="post-footer">
        <div class="post-author">
          <img v-if="post.authorAvatar" :src="post.authorAvatar" class="author-avatar" />
          <span class="author-name">{{ post.authorName }}</span>
        </div>
        <div class="post-stats">
          <span class="stat-item">{{ post.viewCount || 0 }} views</span>
          <span class="stat-item">{{ post.readTime || 0 }} phút đọc</span>
        </div>
      </div>
    </div>
  </NuxtLink>
</template>

<script setup lang="ts">
defineProps({ post: { type: Object, required: true } })

const formatDate = (d: string) => d ? new Date(d).toLocaleDateString('vi-VN') : ''
</script>

<style scoped lang="scss">
.post-card {
  display: flex;
  flex-direction: column;
  border-radius: var(--radius-lg);
  overflow: hidden;
  border: 1px solid var(--border-light);
  background: var(--surface);
  text-decoration: none;
  transition: all 0.2s ease;

  &:hover {
    transform: translateY(-2px);
    box-shadow: var(--shadow-md);
  }
}

.post-cover {
  aspect-ratio: 16/9;
  overflow: hidden;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: transform 0.3s ease;
  }
}

.post-card:hover .post-cover img {
  transform: scale(1.05);
}

.post-body {
  padding: 16px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.post-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}

.post-category {
  font-size: 0.72rem;
  font-weight: 600;
  color: var(--primary);
  background: var(--primary-50);
  padding: 2px 8px;
  border-radius: var(--radius-full);
}

.post-date {
  font-size: 0.72rem;
  color: var(--text-muted);
}

.post-title {
  font-size: 1rem;
  font-weight: 700;
  color: var(--text-primary);
  line-height: 1.4;
  margin-bottom: 8px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.post-excerpt {
  font-size: 0.85rem;
  color: var(--text-secondary);
  line-height: 1.5;
  margin-bottom: 12px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.post-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: auto;
}

.post-author {
  display: flex;
  align-items: center;
  gap: 8px;
}

.author-avatar {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  object-fit: cover;
}

.author-name {
  font-size: 0.78rem;
  font-weight: 500;
  color: var(--text-secondary);
}

.post-stats {
  display: flex;
  gap: 12px;
}

.stat-item {
  font-size: 0.72rem;
  color: var(--text-muted);
}
</style>
