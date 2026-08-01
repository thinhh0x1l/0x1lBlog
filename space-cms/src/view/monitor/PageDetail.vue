<template>
  <div v-if="pageInfo" class="detail-page">
    <div class="detail-head">
      <div>
        <el-button link type="primary" :icon="ArrowLeft" @click="router.push('/monitor/pages')">Pages</el-button>
        <h2>{{ pageInfo.page }}</h2>
        <p>Content performance, behavior density va visitor context.</p>
      </div>
      <el-tag :type="pageInfo.failRate > 10 ? 'danger' : 'success'">{{ pageInfo.failRate }}% fail</el-tag>
    </div>

    <div class="metric-grid">
      <el-card v-for="item in metrics" :key="item.label" shadow="never">
        <span>{{ item.label }}</span>
        <strong>{{ item.value }}</strong>
      </el-card>
    </div>

    <div class="chart-grid">
      <el-card shadow="never" class="chart-card">
        <template #header>Behavior breakdown</template>
        <MonitorChart :option="behaviorOption" />
      </el-card>
      <el-card shadow="never" class="chart-card">
        <template #header>Response distribution</template>
        <MonitorChart :option="responseOption" />
      </el-card>
    </div>

    <el-card shadow="never">
      <template #header>Page timeline</template>
      <el-timeline>
        <el-timeline-item
          v-for="event in pageEvents"
          :key="event.id"
          :timestamp="formatDateTime(event.createdAt)"
          placement="top"
        >
          <div class="timeline-card">
            <div>
              <el-tag size="small" :type="behaviorType(event.behavior)">{{ event.behavior }}</el-tag>
              <strong>{{ event.content }}</strong>
            </div>
            <div>
              <el-tag size="small" :type="statusType(event.success, event.statusCode)">{{ event.statusCode }}</el-tag>
              <span>{{ event.responseTimeMs }}ms</span>
              <el-button link type="primary" @click="router.push(`/monitor/events/${event.id}`)">Event detail</el-button>
            </div>
          </div>
        </el-timeline-item>
      </el-timeline>
    </el-card>
  </div>
  <el-empty v-else description="Page not found" />
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import MonitorChart from './MonitorChart.vue'
import {
  aggregateBehaviors,
  aggregatePages,
  behaviorType,
  formatDateTime,
  getPageEvents,
  statusType
} from './monitorData'

const route = useRoute()
const router = useRouter()
const pagePath = computed(() => decodeURIComponent(route.params.pageKey || ''))
const pageInfo = computed(() => aggregatePages().find((item) => item.page === pagePath.value))
const pageEvents = computed(() => pageInfo.value ? getPageEvents(pageInfo.value.page) : [])

const metrics = computed(() => [
  { label: 'Views', value: pageInfo.value?.views || 0 },
  { label: 'Unique guests', value: pageInfo.value?.uniqueGuests || 0 },
  { label: 'Avg stay', value: `${pageInfo.value?.avgStay || 0}s` },
  { label: 'Events', value: pageInfo.value?.events || 0 },
  { label: 'Comment loads', value: pageInfo.value?.commentLoads || 0 },
  { label: 'Avg response', value: `${pageInfo.value?.avgResponse || 0}ms` }
])

const behaviorOption = computed(() => ({
  tooltip: { trigger: 'item' },
  legend: { bottom: 0, type: 'scroll' },
  series: [{ type: 'pie', radius: ['40%', '68%'], data: aggregateBehaviors(pageEvents.value) }]
}))

const responseOption = computed(() => ({
  tooltip: { trigger: 'axis' },
  grid: { top: 24, right: 20, bottom: 50, left: 42 },
  xAxis: { type: 'category', data: pageEvents.value.map((item) => item.behavior), axisLabel: { rotate: 25 } },
  yAxis: { type: 'value' },
  series: [{ type: 'bar', data: pageEvents.value.map((item) => item.responseTimeMs), itemStyle: { color: '#409eff' } }]
}))
</script>

<style scoped>
.detail-page { display: flex; flex-direction: column; gap: 16px; }
.detail-head { display: flex; justify-content: space-between; gap: 16px; align-items: flex-start; }
.detail-head h2 { margin: 4px 0 6px; color: #1f2937; }
.detail-head p { margin: 0; color: #6b7280; }
.metric-grid { display: grid; grid-template-columns: repeat(6, 1fr); gap: 12px; }
.metric-grid :deep(.el-card__body) { display: flex; flex-direction: column; gap: 8px; }
.metric-grid span { color: #6b7280; font-size: 12px; }
.metric-grid strong { font-size: 18px; color: #111827; }
.chart-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
.chart-card { height: 340px; }
.timeline-card { display: flex; justify-content: space-between; gap: 12px; border: 1px solid #ebeef5; border-radius: 8px; padding: 12px; }
.timeline-card div { display: flex; align-items: center; gap: 8px; }
.timeline-card span { color: #6b7280; }
@media (max-width: 1100px) { .metric-grid, .chart-grid { grid-template-columns: 1fr 1fr; } }
@media (max-width: 760px) { .metric-grid, .chart-grid, .detail-head, .timeline-card { grid-template-columns: 1fr; flex-direction: column; align-items: stretch; } }
</style>
