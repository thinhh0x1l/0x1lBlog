<template>
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
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { blogApi } from '@/api'
const period = ref('hot')
const periods = [{ key: 'hot', label: '24h' }, { key: 'weekly', label: 'Tuần' }, { key: 'monthly', label: 'Tháng' }]
const trendingBlogs = ref([])
const formatCount = (n) => n >= 1000 ? (n / 1000).toFixed(1) + 'k' : n
onMounted(async () => { const res = await blogApi.trending(20); trendingBlogs.value = res.data || [] })
</script>

<style scoped lang="scss">
.trending-page { max-width: 800px; margin: 0 auto; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: var(--space-lg); }
.page-header h1 { font-size: 1.5rem; font-weight: 700; }
.period-tabs { display: flex; gap: 4px; }
.period-btn { padding: 8px 16px; border: 1px solid var(--border); background: var(--surface); border-radius: var(--radius-full); font-size: 0.85rem; cursor: pointer; transition: all var(--duration-fast) ease; }
.period-btn:hover { border-color: var(--primary); }
.period-btn.active { background: var(--primary); color: white; border-color: var(--primary); }
.trending-list { display: flex; flex-direction: column; gap: var(--space-sm); }
.trend-card { display: flex; align-items: flex-start; gap: var(--space-md); padding: var(--space-lg); background: var(--surface); border: 1px solid var(--border-light); border-radius: var(--radius-xl); transition: all var(--duration-fast) ease; }
.trend-card:hover { border-color: var(--primary); box-shadow: var(--shadow-md); transform: translateY(-2px); }
.trend-rank { width: 40px; height: 40px; border-radius: var(--radius-md); display: flex; align-items: center; justify-content: center; font-size: 1.1rem; font-weight: 800; flex-shrink: 0; background: var(--bg-secondary); color: var(--text-muted); }
.trend-rank.rank-1 { background: linear-gradient(135deg, #f97316, #f59e0b); color: white; }
.trend-rank.rank-2 { background: linear-gradient(135deg, #8b5cf6, #a78bfa); color: white; }
.trend-rank.rank-3 { background: linear-gradient(135deg, #06b6d4, #22d3ee); color: white; }
.trend-content { flex: 1; min-width: 0; }
.trend-title { font-size: 1.05rem; font-weight: 600; color: var(--text-primary); text-decoration: none; display: block; margin-bottom: 4px; line-height: 1.4; }
.trend-title:hover { color: var(--primary); }
.trend-desc { font-size: 0.85rem; color: var(--text-secondary); margin-bottom: 8px; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; line-height: 1.5; }
.trend-stats { display: flex; align-items: center; gap: var(--space-md); font-size: 0.78rem; color: var(--text-muted); }
.trend-stats span { display: flex; align-items: center; gap: 4px; }
.trend-stats svg { opacity: 0.6; }
.trend-author { margin-left: auto; font-weight: 500; color: var(--text-secondary); }
.trend-cover { width: 140px; height: 90px; border-radius: var(--radius-md); object-fit: cover; flex-shrink: 0; }
</style>
