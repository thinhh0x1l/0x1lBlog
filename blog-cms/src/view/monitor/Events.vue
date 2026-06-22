<template>
  <div class="monitor-page">
    <div class="page-head">
      <div>
        <h2>Events</h2>
        <p>Audit log cho hanh vi USER/SYSTEM, response time va status bat thuong.</p>
      </div>
    </div>

    <el-card shadow="never" class="filter-card">
      <el-form :inline="true" class="filter-form">
        <el-form-item label="Behavior">
          <el-select v-model="filters.behavior" clearable placeholder="All" style="width: 180px">
            <el-option v-for="item in behaviorOptions" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="Source">
          <el-select v-model="filters.source" clearable placeholder="All" style="width: 130px">
            <el-option label="USER" value="USER" />
            <el-option label="SYSTEM" value="SYSTEM" />
          </el-select>
        </el-form-item>
        <el-form-item label="Result">
          <el-select v-model="filters.success" clearable placeholder="All" style="width: 130px">
            <el-option label="Success" :value="true" />
            <el-option label="Failed" :value="false" />
          </el-select>
        </el-form-item>
        <el-form-item label="Slow >">
          <el-input-number v-model="filters.minResponse" :min="0" :step="100" controls-position="right" />
        </el-form-item>
        <el-form-item label="Keyword">
          <el-input v-model="filters.keyword" clearable :prefix-icon="Search" placeholder="URI, page, content" style="width: 240px" />
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never">
      <el-table :data="pagedRows" stripe height="620" @row-click="openEvent">
        <el-table-column label="Time" width="150">
          <template #default="{ row }">{{ formatDateTime(row.createdAt) }}</template>
        </el-table-column>
        <el-table-column label="Behavior" min-width="150">
          <template #default="{ row }">
            <el-tag :type="behaviorType(row.behavior)" effect="plain">{{ row.behavior }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Source" width="100">
          <template #default="{ row }">
            <el-tag :type="row.source === 'USER' ? 'primary' : 'info'">{{ row.source }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Status" width="90">
          <template #default="{ row }">
            <el-tag :type="statusType(row.success, row.statusCode)">{{ row.statusCode }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Method" prop="method" width="90" />
        <el-table-column label="URI" prop="uri" min-width="210" show-overflow-tooltip />
        <el-table-column label="Page" prop="page" min-width="160" show-overflow-tooltip />
        <el-table-column label="Guest" width="90">
          <template #default="{ row }">#{{ row.guestId }}</template>
        </el-table-column>
        <el-table-column label="Response" width="110">
          <template #default="{ row }">
            <span :class="{ slow: row.responseTimeMs > 800 }">{{ row.responseTimeMs }}ms</span>
          </template>
        </el-table-column>
        <el-table-column label="Content / Remark" min-width="220" show-overflow-tooltip>
          <template #default="{ row }">{{ row.remark || row.content }}</template>
        </el-table-column>
        <el-table-column label="Action" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click.stop="openEvent(row)">Preview</el-button>
            <el-button link type="primary" @click.stop="router.push(`/monitor/events/${row.id}`)">Detail</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pager">
        <el-pagination
          v-model:current-page="page"
          v-model:page-size="pageSize"
          :page-sizes="[20, 50, 100]"
          :total="filteredRows.length"
          layout="total, sizes, prev, pager, next, jumper"
          background
        />
      </div>
    </el-card>

    <el-drawer v-model="drawerVisible" size="560px" :with-header="false">
      <div v-if="selected" class="drawer-body">
        <div class="drawer-head">
          <div>
            <h3>{{ selected.behavior }}</h3>
            <p>{{ selected.method }} {{ selected.uri }}</p>
          </div>
          <el-tag :type="statusType(selected.success, selected.statusCode)" effect="dark">{{ selected.statusCode }}</el-tag>
        </div>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="Page">{{ selected.page }}</el-descriptions-item>
          <el-descriptions-item label="Source">{{ selected.source }}</el-descriptions-item>
          <el-descriptions-item label="Response">{{ selected.responseTimeMs }}ms</el-descriptions-item>
          <el-descriptions-item label="Content">{{ selected.content }}</el-descriptions-item>
          <el-descriptions-item label="Remark">{{ selected.remark || '-' }}</el-descriptions-item>
          <el-descriptions-item label="Param">{{ selected.param }}</el-descriptions-item>
        </el-descriptions>
        <div class="drawer-actions">
          <el-button type="primary" @click="router.push(`/monitor/events/${selected.id}`)">Open event detail</el-button>
          <el-button @click="router.push(`/monitor/visits/${selected.visitId}`)">Visit detail</el-button>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script setup>
import { computed, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { Search } from '@element-plus/icons-vue'
import { behaviorType, events, formatDateTime, statusType } from './monitorData'

const page = ref(1)
const pageSize = ref(50)
const router = useRouter()
const drawerVisible = ref(false)
const selected = ref(null)
const filters = reactive({
  behavior: '',
  source: '',
  success: '',
  minResponse: 0,
  keyword: ''
})

const behaviorOptions = computed(() => [...new Set(events.map((item) => item.behavior))])
const filteredRows = computed(() => {
  const key = filters.keyword.trim().toLowerCase()
  return events.filter((row) => {
    if (filters.behavior && row.behavior !== filters.behavior) return false
    if (filters.source && row.source !== filters.source) return false
    if (filters.success !== '' && row.success !== filters.success) return false
    if (filters.minResponse && row.responseTimeMs < filters.minResponse) return false
    if (!key) return true
    return [row.uri, row.page, row.content, row.remark, row.param].join(' ').toLowerCase().includes(key)
  })
})

const pagedRows = computed(() => {
  const start = (page.value - 1) * pageSize.value
  return filteredRows.value.slice(start, start + pageSize.value)
})

function openEvent(row) {
  selected.value = row
  drawerVisible.value = true
}
</script>

<style scoped>
.monitor-page { display: flex; flex-direction: column; gap: 16px; }
.page-head h2 { margin: 0 0 6px; color: #1f2937; }
.page-head p { margin: 0; color: #6b7280; }
.filter-form { display: flex; flex-wrap: wrap; gap: 8px 12px; }
.filter-form :deep(.el-form-item) { margin-bottom: 0; }
.pager { padding: 16px; display: flex; justify-content: flex-end; border-top: 1px solid #ebeef5; }
.slow { color: #dc2626; font-weight: 700; }
.drawer-body { padding: 22px; }
.drawer-head { display: flex; justify-content: space-between; gap: 14px; align-items: flex-start; margin-bottom: 16px; }
.drawer-head h3 { margin: 0 0 6px; color: #1f2937; }
.drawer-head p { margin: 0; color: #6b7280; }
.drawer-actions { display: flex; gap: 10px; margin-top: 16px; }
</style>
