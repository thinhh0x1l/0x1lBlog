<template>
  <div class="related-posts" v-if="posts.length">
    <h3 class="section-title">Bài viết liên quan</h3>
    <div class="related-grid">
      <NuxtLink v-for="post in posts.slice(0, 3)" :key="post.id" :to="`/blog/${post.id}`" class="related-card">
        <div class="related-cover" v-if="post.coverImage">
          <img :src="post.coverImage" :alt="post.title" />
        </div>
        <div class="related-body">
          <h4 class="related-title">{{ post.title }}</h4>
          <span class="related-meta">{{ formatDate(post.publishedAt) }} · {{ post.readTime }} phút đọc</span>
        </div>
      </NuxtLink>
    </div>
  </div>
</template>
<script setup lang="ts">
defineProps({ posts: { type: Array, default: () => [] } })
const formatDate = (d: string) => d ? new Date(d).toLocaleDateString('vi-VN') : ''
</script>
<style scoped lang="scss">
.related-posts { margin-top: 24px; }
.section-title { font-size: 1.1rem; font-weight: 700; margin-bottom: 16px; }
.related-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 16px; }
.related-card { border-radius: var(--radius-lg); overflow: hidden; border: 1px solid var(--border-light); background: var(--surface); text-decoration: none; transition: all 0.2s ease; }
.related-card:hover { transform: translateY(-2px); box-shadow: var(--shadow-md); }
.related-cover { aspect-ratio: 16/9; overflow: hidden; }
.related-cover img { width: 100%; height: 100%; object-fit: cover; transition: transform 0.3s ease; }
.related-card:hover .related-cover img { transform: scale(1.05); }
.related-body { padding: 12px; }
.related-title { font-size: 0.85rem; font-weight: 600; color: var(--text-primary); line-height: 1.4; overflow: hidden; text-overflow: ellipsis; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; margin-bottom: 4px; }
.related-meta { font-size: 0.72rem; color: var(--text-muted); }
</style>
