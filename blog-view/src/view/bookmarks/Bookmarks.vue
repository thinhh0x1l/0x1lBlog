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
            <router-link :to="`/blog/${bm.blogId}`" class="bookmark-title">{{ bm.blog?.title || `Blog #${bm.blogId}` }}</router-link>
            <p class="bookmark-desc" v-if="bm.blog?.description">{{ bm.blog.description }}</p>
            <div class="bookmark-meta">
              <span class="bookmark-collection" v-if="bm.collection">
                <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M22 19a2 2 0 0 1-2 2H4a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h5l2 3h9a2 2 0 0 1 2 2z"/></svg>
                {{ bm.collection }}
              </span>
              <span class="bookmark-note" v-if="bm.note">📝 {{ bm.note }}</span>
              <span class="bookmark-time">{{ formatDate(bm.createdAt) }}</span>
            </div>
          </div>
          <button class="remove-btn" @click="removeBookmark(bm.id)" title="Xóa">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>
          </button>
        </div>
      </div>
      <el-empty v-if="bookmarks.length === 0" description="Chưa lưu bài viết nào">
        <template #image>
          <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="#cbd5e1" stroke-width="1.5"><path d="M19 21l-7-5-7 5V5a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2z"/></svg>
        </template>
      </el-empty>
    </div>
  </OneColumnLayout>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { bookmarkApi } from '@/api'
import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'
import 'dayjs/locale/vi'
import OneColumnLayout from '@/components/layouts/OneColumnLayout.vue'
dayjs.extend(relativeTime)
dayjs.locale('vi')
const bookmarks = ref([])
const formatDate = (d) => dayjs(d).fromNow()
const loadBookmarks = async () => { try { const res = await bookmarkApi.getAll(); bookmarks.value = res.data || [] } catch (e) {} }
const removeBookmark = async (id) => { bookmarks.value = bookmarks.value.filter(b => b.id !== id); ElMessage.success('Đã xóa') }
onMounted(loadBookmarks)
</script>
<style scoped lang="scss">
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: var(--space-lg); }
.page-header h1 { font-size: 1.5rem; font-weight: 800; }
.count { font-size: 0.85rem; color: var(--text-muted); }
.bookmarks-list { display: flex; flex-direction: column; gap: var(--space-sm); }
.bookmark-item { display: flex; align-items: flex-start; gap: var(--space-md); padding: var(--space-md); background: var(--surface); border: 1px solid var(--border-light); border-radius: var(--radius-lg); transition: all var(--duration-fast) ease; }
.bookmark-item:hover { border-color: var(--primary-50); box-shadow: var(--shadow-sm); }
.bookmark-left { flex: 1; min-width: 0; }
.bookmark-title { font-size: 1rem; font-weight: 600; color: var(--text-primary); text-decoration: none; }
.bookmark-title:hover { color: var(--primary); }
.bookmark-desc { font-size: 0.85rem; color: var(--text-secondary); margin: 4px 0; }
.bookmark-meta { display: flex; gap: var(--space-sm); font-size: 0.78rem; color: var(--text-muted); flex-wrap: wrap; }
.bookmark-collection { display: flex; align-items: center; gap: 2px; color: var(--primary); }
.remove-btn { background: none; border: none; color: var(--text-muted); cursor: pointer; padding: 4px; border-radius: var(--radius); transition: all var(--duration-fast) ease; }
.remove-btn:hover { color: var(--danger); background: var(--danger-50); }
</style>
