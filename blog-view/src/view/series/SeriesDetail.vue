<template>
  <div class="series-detail">
    <div class="series-header">
      <div class="series-cover" :style="{ background: `linear-gradient(135deg, #0ea5e9, #8b5cf6)` }"></div>
      <div class="series-info">
        <h1>{{ series.name }}</h1>
        <p class="series-desc">{{ series.description }}</p>
        <div class="series-meta">
          <span>📖 {{ series.postCount }} bài viết</span>
          <span>👤 {{ series.subscriberCount }} theo dõi</span>
          <span class="series-status" :class="series.status?.toLowerCase()">{{ series.status }}</span>
        </div>
        <div class="series-actions">
          <el-button type="primary" round>Đăng ký theo dõi</el-button>
          <el-button round>Chia sẻ</el-button>
        </div>
      </div>
    </div>
    <div class="series-blogs">
      <h2>Các bài viết trong series</h2>
      <div v-for="(blog, idx) in blogs" :key="blog.id" class="series-blog-item">
        <span class="blog-order">{{ idx + 1 }}</span>
        <router-link :to="`/blog/${blog.id}`" class="blog-link">
          <h3>{{ blog.title }}</h3>
          <span class="blog-meta">{{ blog.readTime }} phút đọc · {{ blog.views }} lượt xem</span>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { seriesApi, blogApi } from '@/api'
const route = useRoute()
const series = ref({})
const blogs = ref([])
onMounted(async () => {
  const res = await seriesApi.getById(route.params.id)
  series.value = res.data || {}
  // Mock blogs for this series
  const blogRes = await blogApi.trending(5)
  blogs.value = blogRes.data || []
})
</script>

<style scoped lang="scss">
.series-detail { max-width: 800px; margin: 0 auto; }
.series-header { background: var(--surface); border-radius: var(--radius-xl); overflow: hidden; margin-bottom: var(--space-xl); border: 1px solid var(--border-light); }
.series-cover { height: 150px; }
.series-info { padding: var(--space-xl); }
.series-info h1 { font-size: 1.5rem; font-weight: 700; margin-bottom: 8px; }
.series-desc { color: var(--text-secondary); margin-bottom: var(--space-md); }
.series-meta { display: flex; gap: var(--space-lg); font-size: 0.85rem; color: var(--text-muted); margin-bottom: var(--space-md); }
.series-status { padding: 2px 10px; border-radius: var(--radius-full); font-weight: 600; }
.series-status.active { background: rgba(16,185,129,0.1); color: #10b981; }
.series-actions { display: flex; gap: var(--space-sm); }
.series-blogs h2 { font-size: 1.1rem; font-weight: 700; margin-bottom: var(--space-md); }
.series-blog-item { display: flex; align-items: flex-start; gap: var(--space-md); padding: var(--space-md); background: var(--surface); border: 1px solid var(--border-light); border-radius: var(--radius-lg); margin-bottom: var(--space-sm); transition: all var(--duration-fast) ease; }
.series-blog-item:hover { border-color: var(--primary); }
.blog-order { width: 32px; height: 32px; border-radius: var(--radius-md); display: flex; align-items: center; justify-content: center; font-weight: 700; background: var(--bg-secondary); color: var(--text-muted); flex-shrink: 0; }
.blog-link { text-decoration: none; }
.blog-link h3 { font-size: 1rem; font-weight: 600; color: var(--text-primary); margin-bottom: 4px; }
.blog-meta { font-size: 0.8rem; color: var(--text-muted); }
</style>
