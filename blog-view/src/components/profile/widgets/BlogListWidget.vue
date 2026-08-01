<template>
  <div class="widget-card">
    <div class="widget-header"><span class="widget-title">Bài viết gần đây</span></div>
    <div class="widget-body">
      <div v-for="blog in blogs" :key="blog.id" class="blog-item">
        <router-link :to="`/blog/${blog.id}`" class="blog-title">{{ blog.title }}</router-link>
        <span class="blog-date">{{ formatDate(blog.publishedAt) }}</span>
      </div>
      <div v-if="!blogs?.length" class="empty-state">Chưa có bài viết nào</div>
    </div>
  </div>
</template>
<script setup>
defineProps({
  blogs: { type: Array, default: () => [] },
})
const formatDate = (d) => d ? new Date(d).toLocaleDateString('vi-VN', { year: 'numeric', month: 'short', day: 'numeric' }) : ''
</script>
<style scoped lang="scss">
.widget-card { background: var(--surface); border-radius: var(--radius-xl); border: 1px solid var(--border-light); overflow: hidden; }
.widget-header { padding: 14px 16px; border-bottom: 1px solid var(--border-light); }
.widget-title { font-size: 0.85rem; font-weight: 700; }
.widget-body { padding: 12px 16px; }
.blog-item { padding: 8px 0; border-bottom: 1px solid var(--border-light); }
.blog-item:last-child { border-bottom: none; }
.blog-title { display: block; font-size: 0.82rem; font-weight: 500; color: var(--text-primary); text-decoration: none; margin-bottom: 2px; }
.blog-title:hover { color: var(--primary); }
.blog-date { font-size: 0.68rem; color: var(--text-muted); }
.empty-state { font-size: 0.8rem; color: var(--text-muted); text-align: center; padding: 12px 0; }
</style>
