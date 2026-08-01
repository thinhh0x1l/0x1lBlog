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
          <div class="trend-rank" :class="`rank-${idx + 1}`">{{ idx + 1 }}</div>
          <div class="trend-content">
            <router-link :to="`/blog/${blog.id}`" class="trend-title">{{ blog.title }}</router-link>
            <p class="trend-desc">{{ blog.description }}</p>
            <div class="trend-stats">
              <span><svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/></svg> {{ formatCount(blog.views) }}</span>
              <span><svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"/></svg> {{ blog.likeCount }}</span>
              <span><svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/></svg> {{ blog.commentCount }}</span>
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
import { ref, onMounted } from 'vue'
import { blogApi } from '@/api'
import TwoColumnLayout from '@/components/layouts/TwoColumnLayout.vue'
import AppSidebar from '@/components/layout/AppSidebar.vue'
const period = ref('hot')
const periods = [{ key: 'hot', label: '24h' }, { key: 'weekly', label: 'Tuần' }, { key: 'monthly', label: 'Tháng' }]
const trendingBlogs = ref([])
const formatCount = (n) => n >= 1000 ? (n / 1000).toFixed(1) + 'k' : n
onMounted(async () => { const res = await blogApi.trending(20); trendingBlogs.value = res.data || [] })
</script>
<style scoped lang="scss">
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: var(--space-lg); flex-wrap: wrap; gap: var(--space-sm); }
.page-header h1 { font-size: 1.5rem; font-weight: 800; }
.period-tabs { display: flex; gap: var(--space-xs); }
.period-btn { background: var(--bg-secondary); border: 1px solid var(--border-light); padding: 6px 16px; border-radius: var(--radius-full); font-size: 0.8rem; color: var(--text-secondary); cursor: pointer; transition: all var(--duration-fast) ease; }
.period-btn.active { background: var(--primary); color: white; border-color: var(--primary); }
.trending-list { display: flex; flex-direction: column; gap: var(--space-sm); }
.trend-card { display: flex; gap: var(--space-md); padding: var(--space-md); background: var(--surface); border: 1px solid var(--border-light); border-radius: var(--radius-lg); transition: all var(--duration-fast) ease; }
.trend-card:hover { border-color: var(--primary-50); box-shadow: var(--shadow-sm); }
.trend-rank { width: 32px; height: 32px; border-radius: var(--radius); display: flex; align-items: center; justify-content: center; font-size: 0.85rem; font-weight: 800; flex-shrink: 0; background: var(--bg); color: var(--text-muted); }
.trend-rank.rank-1 { background: linear-gradient(135deg, #f97316, #f59e0b); color: white; }
.trend-rank.rank-2 { background: linear-gradient(135deg, #8b5cf6, #a78bfa); color: white; }
.trend-rank.rank-3 { background: linear-gradient(135deg, #06b6d4, #22d3ee); color: white; }
.trend-content { flex: 1; min-width: 0; }
.trend-title { font-size: 1rem; font-weight: 600; color: var(--text-primary); text-decoration: none; }
.trend-title:hover { color: var(--primary); }
.trend-desc { font-size: 0.85rem; color: var(--text-secondary); margin: 4px 0; }
.trend-stats { display: flex; gap: var(--space-md); font-size: 0.78rem; color: var(--text-muted); flex-wrap: wrap; }
.trend-stats svg { width: 14px; height: 14px; vertical-align: middle; margin-right: 2px; }
.trend-author { color: var(--primary); font-weight: 500; }
.trend-cover { width: 80px; height: 60px; object-fit: cover; border-radius: var(--radius); flex-shrink: 0; }
</style>
