<template>
  <OneColumnLayout>
    <div class="archives-page">
      <h1>Lưu trữ</h1>
      <p class="page-desc">Danh sách bài viết theo thời gian</p>
      <div class="archives-timeline">
        <div v-for="(group, idx) in archiveGroups" :key="idx" class="archive-group">
          <h3 class="group-title">{{ group.month }}</h3>
          <div class="group-blogs">
            <NuxtLink v-for="blog in group.blogs" :key="blog.id" :to="`/blog/${blog.id}`" class="archive-item">
              <span class="archive-date">{{ blog.day }}</span>
              <span class="archive-title">{{ blog.title }}</span>
            </NuxtLink>
          </div>
        </div>
      </div>
    </div>
  </OneColumnLayout>
</template>
<script setup>
definePageMeta({ layout: 'default' })
useHead({ title: 'Lưu trữ - 0x1lBlog' })

const archiveGroups = ref([
  { month: 'Tháng 6 / 2026', blogs: [{ id: 1, day: '21', title: 'Hướng dẫn Spring Boot từ A-Z' }, { id: 2, day: '18', title: 'Vue 3 Composition API Guide' }] },
  { month: 'Tháng 5 / 2026', blogs: [{ id: 3, day: '15', title: 'PostgreSQL optimization tips' }, { id: 4, day: '10', title: 'Docker Compose cho microservices' }] },
])
</script>
<style scoped lang="scss">
.archives-page h1 { font-size: 1.25rem; font-weight: 700; color: var(--text-primary); margin-bottom: 4px; }
.page-desc { color: var(--text-muted); font-size: 0.85rem; margin-bottom: 24px; }
.archive-group { margin-bottom: 24px; }
.group-title {
  font-size: 0.9rem; font-weight: 700; color: var(--primary); margin-bottom: 12px;
  padding-bottom: 8px; border-bottom: 1px solid var(--border-light);
}
.archive-item {
  display: flex; align-items: center; gap: 16px; padding: 14px 16px;
  text-decoration: none; background: var(--surface); border: 1px solid var(--border-light);
  border-radius: 8px; transition: all 0.12s; margin-bottom: 6px;
  &:hover { border-color: var(--primary); }
}
.archive-date { font-size: 0.85rem; font-weight: 600; color: var(--text-muted); width: 30px; flex-shrink: 0; }
.archive-title { font-size: 0.9rem; color: var(--text-primary); }
.archive-item:hover .archive-title { color: var(--primary); }
</style>
