<template>
  <TwoColumnLayout>
    <template #sidebar-left>
      <AppSidebar />
    </template>
    <div class="series-page">
      <div class="page-header">
        <h1>Series</h1>
        <button class="create-btn" @click="ElMessage.info('Tính năng tạo series sẽ sớm có')">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/></svg>
          Tạo series
        </button>
      </div>
      <div class="series-grid">
        <div v-for="s in seriesList" :key="s.id" class="series-card" @click="$router.push(`/series/${s.id}`)">
          <div class="series-cover" :style="{ background: `linear-gradient(135deg, ${colors[s.id % colors.length][0]}, ${colors[s.id % colors.length][1]})` }">
            <div class="series-cover-overlay">
              <span class="series-post-badge">{{ s.postCount }} bài</span>
            </div>
          </div>
          <div class="series-info">
            <h3>{{ s.name }}</h3>
            <p>{{ s.description }}</p>
            <div class="series-meta">
              <span class="series-subscribers">
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M22 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/></svg>
                {{ s.subscriberCount }} theo dõi
              </span>
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
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { seriesApi } from '@/api'
import TwoColumnLayout from '@/components/layouts/TwoColumnLayout.vue'
import AppSidebar from '@/components/layout/AppSidebar.vue'
const seriesList = ref([])
const colors = [['#0ea5e9','#8b5cf6'],['#f97316','#f59e0b'],['#10b981','#06b6d4'],['#ec4899','#f43f5e'],['#6366f1','#a855f7']]
const formatStatus = (s) => ({ ACTIVE: 'Đang hoạt động', COMPLETED: 'Hoàn thành', ARCHIVED: 'Đã lưu trữ' }[s] || s)
onMounted(async () => { const res = await seriesApi.getAll(); seriesList.value = res.data || [] })
</script>
<style scoped lang="scss">
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: var(--space-lg); flex-wrap: wrap; gap: var(--space-sm); }
.page-header h1 { font-size: 1.5rem; font-weight: 800; }
.create-btn { display: flex; align-items: center; gap: 4px; background: var(--primary); color: white; border: none; padding: 8px 16px; border-radius: var(--radius); font-size: 0.85rem; cursor: pointer; transition: all var(--duration-fast) ease; }
.create-btn:hover { opacity: 0.9; }
.series-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: var(--space-md); }
.series-card { background: var(--surface); border: 1px solid var(--border-light); border-radius: var(--radius-xl); overflow: hidden; cursor: pointer; transition: all var(--duration-fast) ease; }
.series-card:hover { border-color: var(--primary); box-shadow: var(--shadow-md); transform: translateY(-2px); }
.series-cover { height: 120px; position: relative; }
.series-cover-overlay { position: absolute; bottom: 8px; right: 8px; }
.series-post-badge { background: rgba(0,0,0,0.5); color: white; font-size: 0.75rem; padding: 2px 10px; border-radius: var(--radius-full); }
.series-info { padding: var(--space-md); }
.series-info h3 { font-size: 1rem; font-weight: 600; margin-bottom: 4px; }
.series-info p { font-size: 0.8rem; color: var(--text-secondary); margin-bottom: var(--space-sm); }
.series-meta { display: flex; justify-content: space-between; align-items: center; }
.series-subscribers { display: flex; align-items: center; gap: 2px; font-size: 0.78rem; color: var(--text-muted); }
.series-status { font-size: 0.7rem; padding: 2px 8px; border-radius: var(--radius-full); }
.series-status.active { background: #dcfce7; color: #16a34a; }
.series-status.completed { background: #dbeafe; color: #2563eb; }
</style>
