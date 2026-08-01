<template>
  <div class="monitor-page">
    <div class="page-head">
      <div>
        <h2>Visits</h2>
        <p>Session explorer: entry, exit, duration, PV, event health va journey strip.</p>
      </div>
      <div class="toolbar">
        <el-switch v-model="activeOnly" active-text="Active only" />
        <el-input v-model="keyword" clearable :prefix-icon="Search" placeholder="Guest, IP, page" />
      </div>
    </div>

    <el-card shadow="never">
      <el-table :data="pagedRows" stripe height="620" @row-click="openVisit">
        <el-table-column label="Guest" min-width="150">
          <template #default="{ row }">
            <div class="strong">{{ row.guestHash }}</div>
            <div class="muted">{{ row.ipSource }}</div>
          </template>
        </el-table-column>
        <el-table-column label="Device" min-width="170">
          <template #default="{ row }">{{ row.os }} / {{ row.browser }}</template>
        </el-table-column>
        <el-table-column label="Journey" min-width="250" show-overflow-tooltip>
          <template #default="{ row }">{{ row.entryPage }} -> {{ row.exitPage }}</template>
        </el-table-column>
        <el-table-column label="PV" prop="pv" width="70" align="center" />
        <el-table-column label="Events" width="90" align="center">
          <template #default="{ row }">{{ visitEvents(row.id).length }}</template>
        </el-table-column>
        <el-table-column label="Failed" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="failedCount(row.id) ? 'danger' : 'success'">{{ failedCount(row.id) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Duration" width="110">
          <template #default="{ row }">{{ formatDuration(row.startedAt, row.lastActivity) }}</template>
        </el-table-column>
        <el-table-column label="Started" width="160">
          <template #default="{ row }">{{ formatDateTime(row.startedAt) }}</template>
        </el-table-column>
        <el-table-column label="Status" width="100">
          <template #default="{ row }">
            <el-tag :type="row.active ? 'success' : 'info'">{{ row.active ? 'Active' : 'Closed' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Action" width="180" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" :icon="View" @click.stop="openVisit(row)">Playback</el-button>
            <el-button link type="primary" @click.stop="router.push(`/monitor/visits/${row.id}`)">Detail</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pager">
        <el-pagination
          v-model:current-page="page"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 30, 50]"
          :total="filteredRows.length"
          layout="total, sizes, prev, pager, next, jumper"
          background
        />
      </div>
    </el-card>

    <el-drawer v-model="drawerVisible" size="700px" :with-header="false">
      <div v-if="selected" class="drawer-body">
        <div class="drawer-head">
          <div>
            <h3>{{ selected.guestHash }}</h3>
            <p>{{ selected.ip }} - {{ selected.os }} / {{ selected.browser }}</p>
          </div>
          <el-tag :type="failedCount(selected.id) ? 'warning' : 'success'">
            {{ failedCount(selected.id) ? 'Review session' : 'Healthy session' }}
          </el-tag>
        </div>

        <div class="mini-grid">
          <div><span>Duration</span><strong>{{ formatDuration(selected.startedAt, selected.lastActivity) }}</strong></div>
          <div><span>PV</span><strong>{{ selected.pv }}</strong></div>
          <div><span>Events</span><strong>{{ visitEvents(selected.id).length }}</strong></div>
          <div><span>Failed</span><strong>{{ failedCount(selected.id) }}</strong></div>
        </div>

        <div class="journey-strip">
          <template v-for="(pageView, index) in selectedPageViews" :key="pageView.id">
            <div class="journey-node">
              <strong>{{ pageView.page }}</strong>
              <span>{{ pageView.stayDurationSeconds }}s</span>
            </div>
            <el-icon v-if="index < selectedPageViews.length - 1"><Right /></el-icon>
          </template>
        </div>

        <el-timeline>
          <el-timeline-item
            v-for="pageView in selectedPageViews"
            :key="pageView.id"
            :timestamp="formatDateTime(pageView.createdAt)"
            placement="top"
          >
            <div class="page-block">
              <div class="page-title">
                <strong>{{ pageView.page }}</strong>
                <span>Referrer: {{ pageView.referrer || 'Direct' }}</span>
              </div>
              <div v-for="event in pageEvents(pageView.id)" :key="event.id" class="event-row">
                <div>
                  <el-tag size="small" :type="behaviorType(event.behavior)">{{ event.behavior }}</el-tag>
                  <span>{{ event.content }}</span>
                </div>
                <div>
                  <el-tag size="small" :type="statusType(event.success, event.statusCode)">{{ event.statusCode }}</el-tag>
                  <span>{{ event.responseTimeMs }}ms</span>
                </div>
              </div>
            </div>
          </el-timeline-item>
        </el-timeline>
      </div>
    </el-drawer>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import { Right, Search, View } from '@element-plus/icons-vue'
import {
  behaviorType,
  events,
  formatDateTime,
  formatDuration,
  getVisitEvents,
  getVisitPageViews,
  statusType,
  visits
} from './monitorData'

const keyword = ref('')
const router = useRouter()
const activeOnly = ref(false)
const page = ref(1)
const pageSize = ref(20)
const drawerVisible = ref(false)
const selected = ref(null)

const filteredRows = computed(() => {
  const key = keyword.value.trim().toLowerCase()
  return visits.filter((row) => {
    if (activeOnly.value && !row.active) return false
    if (!key) return true
    return [row.guestHash, row.ip, row.ipSource, row.entryPage, row.exitPage, row.os, row.browser].join(' ').toLowerCase().includes(key)
  })
})

const pagedRows = computed(() => {
  const start = (page.value - 1) * pageSize.value
  return filteredRows.value.slice(start, start + pageSize.value)
})

const selectedPageViews = computed(() => selected.value ? getVisitPageViews(selected.value.id) : [])

function openVisit(row) {
  selected.value = row
  drawerVisible.value = true
}

function visitEvents(visitId) {
  return getVisitEvents(visitId)
}

function failedCount(visitId) {
  return visitEvents(visitId).filter((item) => !item.success).length
}

function pageEvents(pageViewId) {
  return events.filter((item) => item.pageViewId === pageViewId)
}
</script>

<style scoped>
.monitor-page { display: flex; flex-direction: column; gap: 16px; }
.page-head { display: flex; justify-content: space-between; gap: 16px; align-items: flex-start; }
.page-head h2, .drawer-head h3 { margin: 0 0 6px; color: #1f2937; }
.page-head p, .drawer-head p, .muted { margin: 0; color: #6b7280; font-size: 13px; }
.toolbar { display: flex; align-items: center; gap: 16px; min-width: 420px; }
.strong { font-weight: 650; color: #1f2937; }
.pager { padding: 16px; display: flex; justify-content: flex-end; border-top: 1px solid #ebeef5; }
.drawer-body { padding: 22px; }
.drawer-head { display: flex; justify-content: space-between; gap: 14px; align-items: flex-start; margin-bottom: 16px; }
.mini-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 10px; margin-bottom: 16px; }
.mini-grid div { border: 1px solid #ebeef5; border-radius: 8px; padding: 12px; background: #fafafa; }
.mini-grid span { display: block; color: #6b7280; font-size: 12px; margin-bottom: 6px; }
.journey-strip { display: flex; align-items: center; gap: 8px; overflow-x: auto; padding: 14px; border: 1px solid #ebeef5; border-radius: 8px; margin-bottom: 22px; }
.journey-node { min-width: 130px; padding: 10px; background: #f8fafc; border-radius: 8px; }
.journey-node span { display: block; color: #6b7280; font-size: 12px; margin-top: 4px; }
.page-block { border: 1px solid #ebeef5; border-radius: 8px; overflow: hidden; }
.page-title { padding: 12px; background: #f8fafc; display: flex; justify-content: space-between; gap: 10px; }
.page-title span { color: #6b7280; }
.event-row { display: flex; justify-content: space-between; gap: 12px; padding: 10px 12px; border-top: 1px solid #f0f2f5; }
.event-row div { display: flex; align-items: center; gap: 8px; }
@media (max-width: 760px) { .page-head, .toolbar, .drawer-head, .event-row { flex-direction: column; } .toolbar { min-width: 0; align-items: stretch; } .mini-grid { grid-template-columns: 1fr 1fr; } }
</style>
