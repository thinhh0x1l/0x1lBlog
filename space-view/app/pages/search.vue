<template>
  <TwoColumnLayout>
    <template #sidebar-left>
      <AppSidebar />
    </template>
    <div class="search-page">
      <div class="search-header">
        <h1>Kết quả tìm kiếm</h1>
        <div class="search-box-lg">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg>
          <input v-model="query" type="text" placeholder="Tìm kiếm bài viết, tag, tác giả..." @keyup.enter="search" autofocus />
          <button class="search-btn" @click="search">Tìm kiếm</button>
        </div>
        <div class="search-filters">
          <button v-for="f in filters" :key="f.key" :class="['filter-btn', { active: activeFilter === f.key }]" @click="activeFilter = f.key">{{ f.label }}</button>
        </div>
      </div>
      <div class="search-results" v-if="results.length">
        <div v-for="blog in results" :key="blog.id" class="result-item">
          <NuxtLink :to="`/blog/${blog.id}`" class="result-title">{{ blog.title }}</NuxtLink>
          <p class="result-desc">{{ blog.description }}</p>
          <div class="result-meta">
            <span>{{ blog.authorName }}</span> · <span>{{ blog.views }} lượt xem</span> · <span>{{ blog.readTime }} phút đọc</span>
          </div>
        </div>
      </div>
      <el-empty v-else-if="searched" description="Không tìm thấy kết quả" />
    </div>
  </TwoColumnLayout>
</template>
<script setup>
import { blogs as mockBlogs } from '~/utils/dummy'

definePageMeta({ layout: 'default', ssr: false })
useHead({ title: 'Tìm kiếm - 0x1lBlog' })

const route = useRoute()
const query = ref(route.query.q || '')
const results = ref([])
const searched = ref(false)
const activeFilter = ref('all')
const filters = [{ key: 'all', label: 'Tất cả' }, { key: 'blog', label: 'Bài viết' }, { key: 'author', label: 'Tác giả' }, { key: 'tag', label: 'Tag' }]

const search = () => {
  if (!query.value.trim()) return
  searched.value = true
  results.value = mockBlogs.filter(b => b.title.toLowerCase().includes(query.value.toLowerCase()))
}
if (query.value) search()
</script>
<style scoped lang="scss">
.search-header { margin-bottom: 20px; }
.search-header h1 { font-size: 1.25rem; font-weight: 700; color: var(--text-primary); margin-bottom: 16px; }
.search-box-lg {
  display: flex; align-items: center; gap: 10px; background: var(--surface);
  border: 1px solid var(--border); border-radius: 8px; padding: 10px 16px;
  transition: border-color 0.15s;
  &:focus-within { border-color: var(--primary); box-shadow: 0 0 0 2px var(--primary-50); }
  input { flex: 1; border: none; background: none; font-size: 0.9rem; color: var(--text-primary); outline: none; font-family: inherit; }
  svg { color: var(--text-muted); flex-shrink: 0; }
}
.search-btn {
  background: var(--primary); color: white; border: none; padding: 8px 20px;
  border-radius: 6px; font-weight: 600; font-size: 0.85rem; cursor: pointer;
  transition: opacity 0.12s;
  &:hover { opacity: 0.9; }
}
.search-filters { display: flex; gap: 6px; margin-top: 12px; }
.filter-btn {
  padding: 6px 14px; border: 1px solid var(--border-light); background: var(--surface);
  border-radius: 6px; font-size: 0.8rem; color: var(--text-secondary); cursor: pointer; font-weight: 500;
  transition: all 0.12s;
  &.active { background: var(--primary); color: white; border-color: var(--primary); }
}
.result-item {
  padding: 16px; background: var(--surface); border: 1px solid var(--border-light);
  border-radius: 8px; margin-bottom: 8px; transition: border-color 0.12s;
  &:hover { border-color: var(--border); }
}
.result-title { font-size: 0.95rem; font-weight: 600; color: var(--text-primary); text-decoration: none; display: block; margin-bottom: 4px; }
.result-title:hover { color: var(--primary); }
.result-desc { font-size: 0.85rem; color: var(--text-secondary); margin: 4px 0 8px; line-height: 1.5; }
.result-meta { font-size: 0.78rem; color: var(--text-muted); }
</style>
