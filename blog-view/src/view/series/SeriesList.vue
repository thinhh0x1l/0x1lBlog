<template>
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
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { seriesApi } from '@/api'
const seriesList = ref([])
const colors = [['#0ea5e9','#8b5cf6'],['#f97316','#f59e0b'],['#10b981','#06b6d4'],['#ec4899','#f43f5e'],['#6366f1','#a855f7']]
const formatStatus = (s) => ({ ACTIVE: 'Đang hoạt động', COMPLETED: 'Hoàn thành', ARCHIVED: 'Đã lưu trữ' }[s] || s)
onMounted(async () => { const res = await seriesApi.getByAuthor(1); seriesList.value = res.data || [] })
</script>

<style scoped lang="scss">
.series-page { max-width: 900px; margin: 0 auto; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: var(--space-lg); }
.page-header h1 { font-size: 1.5rem; font-weight: 700; }
.create-btn { display: flex; align-items: center; gap: 6px; padding: 10px 20px; background: var(--primary); color: white; border: none; border-radius: var(--radius-md); font-weight: 600; cursor: pointer; transition: all var(--duration-fast) ease; }
.create-btn:hover { background: var(--primary-dark); }
.series-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: var(--space-md); }
.series-card { background: var(--surface); border: 1px solid var(--border-light); border-radius: var(--radius-xl); overflow: hidden; cursor: pointer; transition: all var(--duration-fast) ease; }
.series-card:hover { border-color: var(--primary); box-shadow: var(--shadow-md); transform: translateY(-2px); }
.series-cover { height: 100px; position: relative; }
.series-cover-overlay { position: absolute; top: 0; right: 0; padding: 10px; }
.series-post-count { padding: 4px 10px; background: rgba(0,0,0,0.4); color: white; border-radius: var(--radius-full); font-size: 0.75rem; font-weight: 600; backdrop-filter: blur(4px); }
.series-info { padding: var(--space-md); }
.series-info h3 { font-size: 1rem; font-weight: 700; margin-bottom: 4px; }
.series-info p { font-size: 0.85rem; color: var(--text-secondary); margin-bottom: 8px; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; line-height: 1.5; }
.series-meta { display: flex; justify-content: space-between; align-items: center; }
.series-subscribers { display: flex; align-items: center; gap: 4px; font-size: 0.78rem; color: var(--text-muted); }
.series-status { padding: 2px 8px; border-radius: var(--radius-full); font-size: 0.72rem; font-weight: 600; }
.series-status.active { background: rgba(16,185,129,0.1); color: #10b981; }
.series-status.completed { background: rgba(99,102,241,0.1); color: #6366f1; }
.series-status.archived { background: rgba(148,163,184,0.1); color: #64748b; }
</style>
