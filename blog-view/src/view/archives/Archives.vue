<template>
  <OneColumnLayout>
    <div class="archives-page">
      <h1>Lưu trữ</h1>
      <p class="page-desc">Danh sách bài viết theo thời gian</p>
      <div class="archives-timeline">
        <div v-for="(group, idx) in archiveGroups" :key="idx" class="archive-group">
          <h3 class="group-title">{{ group.month }}</h3>
          <div class="group-blogs">
            <router-link v-for="blog in group.blogs" :key="blog.id" :to="`/blog/${blog.id}`" class="archive-item">
              <span class="archive-date">{{ blog.day }}</span>
              <span class="archive-title">{{ blog.title }}</span>
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </OneColumnLayout>
</template>
<script setup>
import { ref } from 'vue'
import OneColumnLayout from '@/components/layouts/OneColumnLayout.vue'
const archiveGroups = ref([
  { month: 'Tháng 6 / 2026', blogs: [{ id: 1, day: '21', title: 'Hướng dẫn Spring Boot từ A-Z' }, { id: 2, day: '18', title: 'Vue 3 Composition API Guide' }] },
  { month: 'Tháng 5 / 2026', blogs: [{ id: 3, day: '15', title: 'PostgreSQL optimization tips' }, { id: 4, day: '10', title: 'Docker Compose cho microservices' }] },
])
</script>
<style scoped lang="scss">
.archives-page h1 { font-size: 1.5rem; font-weight: 800; margin-bottom: 4px; }
.page-desc { color: var(--text-muted); margin-bottom: var(--space-xl); }
.archive-group { margin-bottom: var(--space-xl); }
.group-title { font-size: 1rem; font-weight: 700; color: var(--primary); margin-bottom: var(--space-md); padding-bottom: 8px; border-bottom: 2px solid var(--primary-50); }
.archive-item { display: flex; align-items: center; gap: var(--space-md); padding: var(--space-md); text-decoration: none; background: var(--surface); border: 1px solid var(--border-light); border-radius: var(--radius-lg); transition: all var(--duration-fast) ease; }
.archive-item:hover { border-color: var(--primary); box-shadow: var(--shadow-md); transform: translateY(-1px); }
.archive-date { font-size: 0.85rem; font-weight: 600; color: var(--text-muted); width: 30px; flex-shrink: 0; }
.archive-title { font-size: 0.9rem; color: var(--text-primary); transition: color var(--duration-fast) ease; }
.archive-title:hover { color: var(--primary); }
</style>
