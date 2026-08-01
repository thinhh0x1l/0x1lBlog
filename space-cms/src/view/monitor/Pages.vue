<template>
  <div class="monitor-page">
    <div class="page-head">
      <div>
        <h2>Pages</h2>
        <p>Content analytics: traffic, stay time, behavior density va failure rate theo page.</p>
      </div>
      <el-input v-model="keyword" clearable :prefix-icon="Search" placeholder="Search page" />
    </div>

    <div class="chart-grid">
      <el-card shadow="never" class="chart-card">
        <template #header>Top pages by PV</template>
        <MonitorChart :option="topPagesOption" />
      </el-card>
      <el-card shadow="never" class="chart-card">
        <template #header>Avg stay vs failures</template>
        <MonitorChart :option="stayOption" />
      </el-card>
    </div>

    <el-card shadow="never">
      <el-table :data="pagedRows" stripe height="520" @row-click="openPage">
        <el-table-column label="Page" prop="page" min-width="220" show-overflow-tooltip />
        <el-table-column label="Views" prop="views" width="90" align="center" />
        <el-table-column label="Unique" prop="uniqueGuests" width="90" align="center" />
        <el-table-column label="Avg stay" width="100">
          <template #default="{ row }">{{ row.avgStay }}s</template>
        </el-table-column>
        <el-table-column label="Events" prop="events" width="90" align="center" />
        <el-table-column label="Comment loads" prop="commentLoads" width="130" align="center" />
        <el-table-column label="Likes" prop="likes" width="80" align="center" />
        <el-table-column label="Fail rate" width="100">
          <template #default="{ row }">
            <el-tag :type="row.failRate > 10 ? 'danger' : row.failRate > 0 ? 'warning' : 'success'">
              {{ row.failRate }}%
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Avg response" width="120">
          <template #default="{ row }">{{ row.avgResponse }}ms</template>
        </el-table-column>
        <el-table-column label="Action" width="170" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" :icon="View" @click.stop="openPage(row)">Inspect</el-button>
            <el-button link type="primary" @click.stop="router.push(`/monitor/pages/${encodeURIComponent(row.page)}`)">Detail</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pager">
        <el-pagination
          v-model:current-page="page"
          v-model:page-size="pageSize"
          :page-sizes="[5, 10, 20]"
          :total="filteredRows.length"
          layout="total, sizes, prev, pager, next"
          background
        />
      </div>
    </el-card>

    <el-drawer v-model="drawerVisible" size="660px" :with-header="false">
      <div v-if="selected" class="drawer-body">
        <div class="drawer-head">
          <div>
            <h3>{{ selected.page }}</h3>
            <p>{{ selected.views }} views / {{ selected.uniqueGuests }} guests / {{ selected.events }} events</p>
          </div>
          <el-tag :type="selected.failRate > 10 ? 'danger' : 'success'">{{ selected.failRate }}% fail</el-tag>
        </div>

        <el-card shadow="never" class="chart-box">
          <template #header>Behavior breakdown</template>
          <MonitorChart :option="behaviorOption" />
        </el-card>

        <el-divider />
        <h4>Recent events on this page</h4>
        <el-table :data="pageEvents" size="small">
          <el-table-column label="Behavior" min-width="150">
            <template #default="{ row }">
              <el-tag size="small" :type="behaviorType(row.behavior)">{{ row.behavior }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="Source" prop="source" width="90" />
          <el-table-column label="Status" width="90">
            <template #default="{ row }">
              <el-tag size="small" :type="statusType(row.success, row.statusCode)">{{ row.statusCode }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="Response" width="90">
            <template #default="{ row }">{{ row.responseTimeMs }}ms</template>
          </el-table-column>
        </el-table>
      </div>
    </el-drawer>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import { Search, View } from '@element-plus/icons-vue'
import MonitorChart from './MonitorChart.vue'
import { aggregateBehaviors, aggregatePages, behaviorType, events, getPageEvents, statusType } from './monitorData'

const keyword = ref('')
const page = ref(1)
const pageSize = ref(10)
const drawerVisible = ref(false)
const selected = ref(null)
const rows = aggregatePages()

const filteredRows = computed(() => {
  const key = keyword.value.trim().toLowerCase()
  return key ? rows.filter((row) => row.page.toLowerCase().includes(key)) : rows
})

const pagedRows = computed(() => {
  const start = (page.value - 1) * pageSize.value
  return filteredRows.value.slice(start, start + pageSize.value)
})

const pageEvents = computed(() => selected.value ? getPageEvents(selected.value.page).slice(0, 20) : [])

const topPagesOption = computed(() => ({
  tooltip: { trigger: 'axis' },
  grid: { top: 24, right: 20, bottom: 60, left: 38 },
  xAxis: { type: 'category', data: rows.slice(0, 8).map((item) => item.page), axisLabel: { rotate: 30 } },
  yAxis: { type: 'value' },
  series: [{ type: 'bar', data: rows.slice(0, 8).map((item) => item.views), itemStyle: { color: '#409eff' } }]
}))

const stayOption = computed(() => ({
  tooltip: { trigger: 'axis' },
  legend: { top: 0 },
  grid: { top: 42, right: 20, bottom: 60, left: 38 },
  xAxis: { type: 'category', data: rows.slice(0, 8).map((item) => item.page), axisLabel: { rotate: 30 } },
  yAxis: [{ type: 'value' }, { type: 'value' }],
  series: [
    { name: 'Avg stay', type: 'bar', data: rows.slice(0, 8).map((item) => item.avgStay) },
    { name: 'Fail rate', type: 'line', yAxisIndex: 1, data: rows.slice(0, 8).map((item) => item.failRate) }
  ]
}))

const behaviorOption = computed(() => ({
  tooltip: { trigger: 'item' },
  legend: { bottom: 0, type: 'scroll' },
  series: [{
    type: 'pie',
    radius: ['40%', '68%'],
    data: selected.value ? aggregateBehaviors(events.filter((item) => item.page === selected.value.page)) : []
  }]
}))

function openPage(row) {
  selected.value = row
  drawerVisible.value = true
}
</script>

<style scoped>
.monitor-page { display: flex; flex-direction: column; gap: 16px; }
.page-head { display: flex; justify-content: space-between; gap: 16px; align-items: flex-start; }
.page-head h2, .drawer-head h3 { margin: 0 0 6px; color: #1f2937; }
.page-head p, .drawer-head p { margin: 0; color: #6b7280; }
.chart-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
.chart-card, .chart-box { height: 340px; }
.pager { padding: 16px; display: flex; justify-content: flex-end; border-top: 1px solid #ebeef5; }
.drawer-body { padding: 22px; }
.drawer-head { display: flex; justify-content: space-between; gap: 14px; align-items: flex-start; margin-bottom: 16px; }
@media (max-width: 900px) { .chart-grid, .page-head, .drawer-head { grid-template-columns: 1fr; flex-direction: column; } }
</style>
