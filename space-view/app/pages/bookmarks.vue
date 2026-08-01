<template>
  <OneColumnLayout>
    <div class="bookmarks-page">
      <div class="page-header">
        <h1>Bài viết đã lưu</h1>
        <span class="count">{{ bookmarks.length }} bài viết</span>
      </div>
      <div class="bookmarks-list">
        <div v-for="bm in bookmarks" :key="bm.id" class="bookmark-item">
          <div class="bookmark-left">
            <NuxtLink :to="`/blog/${bm.blogId}`" class="bookmark-title">{{ bm.blog?.title || `Blog #${bm.blogId}` }}</NuxtLink>
            <p class="bookmark-desc" v-if="bm.blog?.description">{{ bm.blog.description }}</p>
            <div class="bookmark-meta">
              <span class="bookmark-collection" v-if="bm.collection">{{ bm.collection }}</span>
              <span class="bookmark-time">{{ formatDate(bm.createdAt) }}</span>
            </div>
          </div>
          <button class="remove-btn" @click="removeBookmark(bm.id)" title="Xóa">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>
          </button>
        </div>
      </div>
      <el-empty v-if="bookmarks.length === 0" description="Chưa lưu bài viết nào" />
    </div>
  </OneColumnLayout>
</template>
<script setup>
import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'
import 'dayjs/locale/vi'

dayjs.extend(relativeTime)
dayjs.locale('vi')

definePageMeta({ layout: 'default', ssr: false })
useHead({ title: 'Đã lưu - 0x1lBlog' })

const bookmarks = ref([])
const formatDate = (d) => dayjs(d).fromNow()
const removeBookmark = (id) => { bookmarks.value = bookmarks.value.filter(b => b.id !== id) }
</script>
<style scoped lang="scss">
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-header h1 { font-size: 1.25rem; font-weight: 700; color: var(--text-primary); }
.count { font-size: 0.85rem; color: var(--text-muted); }
.bookmarks-list { display: flex; flex-direction: column; gap: 8px; }
.bookmark-item {
  display: flex; align-items: flex-start; gap: 16px; padding: 16px;
  background: var(--surface); border: 1px solid var(--border-light); border-radius: 8px;
  transition: border-color 0.12s;
  &:hover { border-color: var(--border); }
}
.bookmark-left { flex: 1; min-width: 0; }
.bookmark-title { font-size: 0.95rem; font-weight: 600; color: var(--text-primary); text-decoration: none; display: block; margin-bottom: 4px; }
.bookmark-title:hover { color: var(--primary); }
.bookmark-desc { font-size: 0.85rem; color: var(--text-secondary); margin: 4px 0 8px; line-height: 1.5; }
.bookmark-meta { display: flex; gap: 12px; font-size: 0.78rem; color: var(--text-muted); }
.bookmark-collection { color: var(--primary); font-weight: 500; }
.remove-btn {
  background: none; border: none; color: var(--text-muted); cursor: pointer;
  padding: 6px; border-radius: 6px; transition: all 0.12s;
  &:hover { color: var(--danger); background: rgba(239, 68, 68, 0.05); }
}
</style>
