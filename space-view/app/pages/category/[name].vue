<template>
  <TwoColumnLayout>
    <template #sidebar-left>
      <AppSidebar />
    </template>
    <div class="category-page">
      <h1>Danh mục: {{ category?.name || route.params.name }}</h1>
      <p class="page-desc">Bài viết trong danh mục này</p>
      <div class="category-blogs">
        <div v-for="blog in categoryBlogs" :key="blog.id" class="category-blog-item">
          <NuxtLink :to="`/blog/${blog.id}`" class="category-blog-title">{{ blog.title }}</NuxtLink>
          <p class="category-blog-desc">{{ blog.description }}</p>
          <div class="category-blog-meta">{{ blog.views }} lượt xem · {{ blog.readTime }} phút đọc</div>
        </div>
      </div>
      <el-empty v-if="categoryBlogs.length === 0" description="Chưa có bài viết" />
    </div>
  </TwoColumnLayout>
</template>
<script setup>
import { blogs as mockBlogs, categories as mockCategories } from '~/utils/dummy'

definePageMeta({ layout: 'default' })

const route = useRoute()

const category = mockCategories.find(c => c.slug === route.params.name)
const categoryBlogs = mockBlogs.filter(b => b.categoryId === category?.id && b.status === 'PUBLISHED')

useHead({ title: `${category?.name || route.params.name} - 0x1lBlog` })
</script>
<style scoped lang="scss">
.category-page h1 { font-size: 1.5rem; font-weight: 800; margin-bottom: 4px; }
.page-desc { color: var(--text-muted); margin-bottom: var(--space-xl); }
.category-blogs { display: flex; flex-direction: column; gap: var(--space-sm); }
.category-blog-item { background: var(--surface); border: 1px solid var(--border-light); border-radius: var(--radius-xl); padding: var(--space-lg); box-shadow: var(--shadow-sm); transition: all var(--duration-fast) ease; }
.category-blog-item:hover { border-color: var(--primary); box-shadow: var(--shadow-md); transform: translateY(-1px); }
.category-blog-title { font-size: 1rem; font-weight: 600; color: var(--text-primary); text-decoration: none; display: block; margin-bottom: 4px; }
.category-blog-title:hover { color: var(--primary); }
.category-blog-desc { font-size: 0.85rem; color: var(--text-secondary); margin-bottom: 4px; }
.category-blog-meta { font-size: 0.78rem; color: var(--text-muted); }
</style>
