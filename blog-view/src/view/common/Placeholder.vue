<template>
  <div class="page-placeholder">
    <h1>{{ title }}</h1>
    <p>{{ desc }}</p>
  </div>
</template>
<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
const route = useRoute()
const title = computed(() => {
  const name = route.name
  if (name === 'archives') return 'Lưu trữ'
  if (name === 'about') return 'Về tôi'
  if (name === 'tag') return `Tag: #${route.params.id}`
  if (name === 'category') return `Danh mục: ${route.params.name}`
  if (name === 'search') return `Tìm kiếm: ${route.query.q}`
  if (name === 'notifications') return 'Thông báo'
  if (name === 'bookmarks') return 'Bài viết đã lưu'
  return name
})
const desc = computed(() => 'Tính năng đang được phát triển')
</script>
<style scoped lang="scss">
.page-placeholder { background: var(--surface); border-radius: var(--radius-lg); box-shadow: var(--shadow); padding: var(--space-2xl); text-align: center; }
.page-placeholder h1 { font-size: 1.5rem; font-weight: 700; margin-bottom: 8px; }
.page-placeholder p { color: var(--text-muted); }
</style>
