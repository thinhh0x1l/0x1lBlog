<template>
  <div class="search-page">
    <div class="search-header">
      <h1>Kết quả tìm kiếm</h1>
      <div class="search-box-large">
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg>
        <input v-model="query" type="text" placeholder="Tìm kiếm bài viết, tag, tác giả..." @keyup.enter="search" autofocus />
        <button class="search-btn" @click="search">Tìm kiếm</button>
      </div>
      <div class="search-filters">
        <button v-for="f in filters" :key="f.key" :class="['filter-btn', { active: activeFilter === f.key }]" @click="activeFilter = f.key">{{ f.label }}</button>
      </div>
    </div>
    <div class="search-results" v-if="results.length">
      <div v-for="blog in results" :key="blog.id" class="result-item">
        <router-link :to="`/blog/${blog.id}`" class="result-title">{{ blog.title }}</router-link>
        <p class="result-desc">{{ blog.description }}</p>
        <div class="result-meta">
          <span>{{ blog.authorName }}</span> · <span>{{ blog.views }} lượt xem</span> · <span>{{ blog.readTime }} phút đọc</span>
        </div>
      </div>
    </div>
    <el-empty v-else-if="searched" description="Không tìm thấy kết quả" />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRoute } from 'vue-router'
import { blogApi } from '@/api'
const route = useRoute()
const query = ref(route.query.q || '')
const results = ref([])
const searched = ref(false)
const activeFilter = ref('all')
const filters = [{ key: 'all', label: 'Tất cả' }, { key: 'blog', label: 'Bài viết' }, { key: 'author', label: 'Tác giả' }, { key: 'tag', label: 'Tag' }]
const search = async () => { if (!query.value.trim()) return; searched.value = true; const res = await blogApi.search(query.value); results.value = res.data || [] }
if (query.value) search()
</script>

<style scoped lang="scss">
.search-page { max-width: 800px; margin: 0 auto; }
.search-header { margin-bottom: var(--space-xl); }
.search-header h1 { font-size: 1.5rem; font-weight: 700; margin-bottom: var(--space-md); }
.search-box-large { display: flex; align-items: center; gap: var(--space-sm); background: var(--surface); border: 1px solid var(--border); border-radius: var(--radius-lg); padding: 4px 4px 4px 16px; margin-bottom: var(--space-md); }
.search-box-large svg { color: var(--text-muted); flex-shrink: 0; }
.search-box-large input { flex: 1; border: none; outline: none; font-size: 1rem; padding: 10px 8px; background: transparent; }
.search-btn { padding: 10px 24px; background: var(--primary); color: white; border: none; border-radius: var(--radius-md); font-weight: 600; cursor: pointer; }
.search-filters { display: flex; gap: var(--space-sm); }
.filter-btn { padding: 8px 16px; border: 1px solid var(--border); background: var(--surface); border-radius: var(--radius-full); font-size: 0.85rem; cursor: pointer; transition: all var(--duration-fast) ease; }
.filter-btn:hover { border-color: var(--primary); color: var(--primary); }
.filter-btn.active { background: var(--primary); color: white; border-color: var(--primary); }
.search-results { display: flex; flex-direction: column; gap: var(--space-md); }
.result-item { background: var(--surface); border: 1px solid var(--border-light); border-radius: var(--radius-lg); padding: var(--space-lg); transition: all var(--duration-fast) ease; }
.result-item:hover { border-color: var(--primary); box-shadow: var(--shadow-sm); }
.result-title { font-size: 1.1rem; font-weight: 700; color: var(--text-primary); text-decoration: none; display: block; margin-bottom: 6px; }
.result-title:hover { color: var(--primary); }
.result-desc { font-size: 0.9rem; color: var(--text-secondary); line-height: 1.5; margin-bottom: 8px; }
.result-meta { font-size: 0.8rem; color: var(--text-muted); }
</style>
