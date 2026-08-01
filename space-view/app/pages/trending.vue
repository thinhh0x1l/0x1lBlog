<template>
  <TwoColumnLayout>
    <template #sidebar-left>
      <AppSidebar />
    </template>
    <div class="trending-page">
      <div class="page-header">
        <h1>Xu hướng</h1>
        <div class="period-tabs">
          <button v-for="p in periods" :key="p.key" :class="['period-btn', { active: period === p.key }]" @click="period = p.key">{{ p.label }}</button>
        </div>
      </div>
      <div class="trending-list">
        <div v-for="(blog, idx) in trendingBlogs" :key="blog.id" class="trend-card">
          <div class="trend-rank" :class="{ 'trend-rank--top': idx < 3 }">{{ idx + 1 }}</div>
          <div class="trend-content">
            <NuxtLink :to="`/blog/${blog.id}`" class="trend-title">{{ blog.title }}</NuxtLink>
            <p class="trend-desc">{{ blog.description }}</p>
            <div class="trend-stats">
              <span>{{ formatCount(blog.views) }} lượt xem</span>
              <span>{{ blog.likeCount }} thích</span>
              <span>{{ blog.commentCount }} bình luận</span>
              <span class="trend-author">{{ blog.authorName }}</span>
            </div>
          </div>
          <img v-if="blog.coverImage" :src="blog.coverImage" class="trend-cover" loading="lazy" />
        </div>
      </div>
    </div>
  </TwoColumnLayout>
</template>
<script setup>
import { blogs as mockBlogs } from '~/utils/dummy'

definePageMeta({ layout: 'default' })
useHead({ title: 'Xu hướng - 0x1lBlog' })

const period = ref('weekly')
const periods = [{ key: 'hot', label: '24h' }, { key: 'weekly', label: 'Tuần' }, { key: 'monthly', label: 'Tháng' }]
const trendingBlogs = mockBlogs.filter(b => b.status === 'PUBLISHED').sort((a, b) => b.views - a.views).slice(0, 20)
const formatCount = (n) => n >= 1000 ? (n / 1000).toFixed(1) + 'k' : n
</script>
<style scoped lang="scss">
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; flex-wrap: wrap; gap: 8px; }
.page-header h1 { font-size: 1.25rem; font-weight: 700; color: var(--text-primary); }
.period-tabs { display: flex; gap: 4px; }
.period-btn {
  padding: 6px 16px; border: 1px solid var(--border-light); background: var(--surface);
  border-radius: 6px; font-size: 0.8rem; color: var(--text-secondary); cursor: pointer; font-weight: 500;
  transition: all 0.12s;
  &:hover { border-color: var(--border); }
  &.active { background: var(--primary); color: white; border-color: var(--primary); }
}
.trending-list { display: flex; flex-direction: column; gap: 8px; }
.trend-card {
  display: flex; gap: 16px; padding: 16px; background: var(--surface);
  border: 1px solid var(--border-light); border-radius: 8px;
  transition: all 0.12s;
  &:hover { border-color: var(--border); }
}
.trend-rank {
  width: 28px; height: 28px; border-radius: 6px;
  display: flex; align-items: center; justify-content: center;
  font-size: 0.8rem; font-weight: 700; flex-shrink: 0;
  background: var(--bg-secondary); color: var(--text-muted);
  &.trend-rank--top { background: var(--primary); color: white; }
}
.trend-content { flex: 1; min-width: 0; }
.trend-title {
  font-size: 0.95rem; font-weight: 600; color: var(--text-primary); text-decoration: none;
  display: block; margin-bottom: 4px;
  &:hover { color: var(--primary); }
}
.trend-desc {
  font-size: 0.83rem; color: var(--text-secondary); margin-bottom: 8px; line-height: 1.5;
  display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden;
}
.trend-stats { display: flex; gap: 16px; font-size: 0.78rem; color: var(--text-muted); flex-wrap: wrap; }
.trend-author { color: var(--primary); font-weight: 500; }
.trend-cover { width: 80px; height: 60px; object-fit: cover; border-radius: 6px; flex-shrink: 0; }
</style>
