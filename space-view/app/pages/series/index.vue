<template>
  <TwoColumnLayout>
    <template #sidebar-left>
      <AppSidebar />
    </template>
    <div class="series-page">
      <div class="page-header">
        <h1>Series</h1>
        <button class="create-btn" @click="console.log('Coming soon')">+ Tạo series</button>
      </div>
      <div class="series-grid">
        <div v-for="s in seriesList" :key="s.id" class="series-card" @click="navigateTo(`/series/${s.id}`)">
          <div class="series-cover" :style="{ background: `linear-gradient(135deg, ${colors[s.id % colors.length][0]}, ${colors[s.id % colors.length][1]})` }">
            <span class="series-post-badge">{{ s.postCount }} bài</span>
          </div>
          <div class="series-info">
            <h3>{{ s.name }}</h3>
            <p>{{ s.description }}</p>
            <div class="series-meta">
              <span class="series-subscribers">{{ s.subscriberCount }} theo dõi</span>
              <span class="series-status" :class="s.status?.toLowerCase()">{{ formatStatus(s.status) }}</span>
            </div>
          </div>
        </div>
      </div>
      <el-empty v-if="seriesList.length === 0" description="Chưa có series nào" />
    </div>
  </TwoColumnLayout>
</template>
<script setup>
import { blogSeries as mockSeries } from '~/utils/dummy'

definePageMeta({ layout: 'default' })
useHead({ title: 'Series - 0x1lBlog' })

const seriesList = ref(mockSeries || [])
const colors = [['#0ea5e9','#8b5cf6'],['#f97316','#f59e0b'],['#10b981','#06b6d4'],['#ec4899','#f43f5e'],['#6366f1','#a855f7']]
const formatStatus = (s) => ({ ACTIVE: 'Đang hoạt động', COMPLETED: 'Hoàn thành', ARCHIVED: 'Đã lưu trữ' }[s] || s)
</script>
<style scoped lang="scss">
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-header h1 { font-size: 1.25rem; font-weight: 700; color: var(--text-primary); }
.create-btn {
  background: var(--primary); color: white; border: none; padding: 8px 16px;
  border-radius: 6px; font-size: 0.85rem; cursor: pointer; font-weight: 500;
  transition: opacity 0.12s;
  &:hover { opacity: 0.9; }
}
.series-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 12px; }
.series-card {
  background: var(--surface); border: 1px solid var(--border-light);
  border-radius: 8px; overflow: hidden; cursor: pointer; transition: all 0.12s;
  &:hover { border-color: var(--primary); }
}
.series-cover { height: 100px; position: relative; padding: 8px 12px; display: flex; align-items: flex-end; }
.series-post-badge { background: rgba(0,0,0,0.5); color: white; font-size: 0.72rem; padding: 2px 10px; border-radius: 10px; }
.series-info { padding: 14px; }
.series-info h3 { font-size: 0.95rem; font-weight: 600; color: var(--text-primary); margin-bottom: 4px; }
.series-info p { font-size: 0.8rem; color: var(--text-secondary); margin-bottom: 8px; line-height: 1.4; }
.series-meta { display: flex; justify-content: space-between; align-items: center; }
.series-subscribers { font-size: 0.75rem; color: var(--text-muted); }
.series-status { font-size: 0.7rem; padding: 2px 8px; border-radius: 10px; font-weight: 500; }
.series-status.active { background: #dcfce7; color: #16a34a; }
.series-status.completed { background: #dbeafe; color: #2563eb; }
</style>
