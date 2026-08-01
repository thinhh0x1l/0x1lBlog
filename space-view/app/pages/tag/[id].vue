<template>
  <TwoColumnLayout>
    <template #sidebar-left>
      <AppSidebar />
    </template>
    <div class="tag-page">
      <h1>Tag: #{{ tag?.name || route.params.id }}</h1>
      <p class="page-desc">Bài viết liên quan đến tag này</p>
      <div class="tag-blogs">
        <div v-for="blog in tagBlogs" :key="blog.id" class="tag-blog-item">
          <NuxtLink :to="`/blog/${blog.id}`" class="tag-blog-title">{{ blog.title }}</NuxtLink>
          <p class="tag-blog-desc">{{ blog.description }}</p>
          <div class="tag-blog-meta">{{ blog.views }} lượt xem · {{ blog.readTime }} phút đọc</div>
        </div>
      </div>
      <el-empty v-if="tagBlogs.length === 0" description="Chưa có bài viết" />
    </div>
  </TwoColumnLayout>
</template>
<script setup>
import { blogs as mockBlogs, hashtags } from '~/utils/dummy'

definePageMeta({ layout: 'default' })

const route = useRoute()

const tag = hashtags.find(t => t.id === Number(route.params.id))
const tagBlogs = mockBlogs.filter(b => b.status === 'PUBLISHED').slice(0, 10)

useHead({ title: tag?.name ? `#${tag.name} - 0x1lBlog` : `Tag #${route.params.id} - 0x1lBlog` })
</script>
<style scoped lang="scss">
.tag-page h1 { font-size: 1.5rem; font-weight: 800; margin-bottom: 4px; }
.page-desc { color: var(--text-muted); margin-bottom: var(--space-xl); }
.tag-blogs { display: flex; flex-direction: column; gap: var(--space-sm); }
.tag-blog-item { background: var(--surface); border: 1px solid var(--border-light); border-radius: var(--radius-xl); padding: var(--space-lg); box-shadow: var(--shadow-sm); transition: all var(--duration-fast) ease; }
.tag-blog-item:hover { border-color: var(--primary); box-shadow: var(--shadow-md); transform: translateY(-1px); }
.tag-blog-title { font-size: 1rem; font-weight: 600; color: var(--text-primary); text-decoration: none; display: block; margin-bottom: 4px; }
.tag-blog-title:hover { color: var(--primary); }
.tag-blog-desc { font-size: 0.85rem; color: var(--text-secondary); margin-bottom: 4px; }
.tag-blog-meta { font-size: 0.78rem; color: var(--text-muted); }
</style>
