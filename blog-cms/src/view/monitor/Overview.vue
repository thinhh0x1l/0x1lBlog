<template>
  <div class="monitor-page">
    <div class="page-head">
      <div>
        <h2>Monitor Overview</h2>
        <p>Mini analytics dashboard cho traffic, behavior va health cua blog.</p>
      </div>
      <el-tag type="success" effect="dark">Dummy data</el-tag>
    </div>

    <div class="metric-grid">
      <el-card v-for="item in metrics" :key="item.label" shadow="never" class="metric-card">
        <div class="metric-label">{{ item.label }}</div>
        <div class="metric-value">{{ item.value }}</div>
        <div class="metric-hint" :class="item.tone">{{ item.hint }}</div>
      </el-card>
    </div>

    <div class="chart-grid">
      <el-card shadow="never" class="chart-card chart-card--wide">
        <template #header>Traffic trend</template>
        <MonitorChart :option="trafficOption" />
      </el-card>
      <el-card shadow="never" class="chart-card">
        <template #header>Behavior mix</template>
        <MonitorChart :option="behaviorOption" />
      </el-card>
      <el-card shadow="never" class="chart-card">
        <template #header>Health</template>
        <MonitorChart :option="healthOption" />
      </el-card>
    </div>

    <div class="content-grid">
      <el-card shadow="never">
        <template #header>Top pages</template>
        <el-table :data="topPages" height="330">
          <el-table-column label="Page" prop="page" min-width="170" show-overflow-tooltip />
          <el-table-column label="Views" prop="views" width="80" />
          <el-table-column label="Avg stay" width="90">
            <template #default="{ row }">{{ row.avgStay }}s</template>
          </el-table-column>
          <el-table-column label="Fail" width="80">
            <template #default="{ row }">
              <el-tag :type="row.failRate > 10 ? 'danger' : 'success'">{{ row.failRate }}%</el-tag>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <el-card shadow="never">
        <template #header>Recent activity</template>
        <el-timeline class="activity-timeline">
          <el-timeline-item
            v-for="event in recentEvents"
            :key="event.id"
            :timestamp="formatDateTime(event.createdAt)"
            placement="top"
          >
            <div class="activity-item">
              <div class="activity-main">
                <el-tag size="small" :type="behaviorType(event.behavior)">{{ event.behavior }}</el-tag>
                <span>{{ event.page }}</span>
              </div>
              <div class="activity-meta">
                {{ event.source }} / {{ event.statusCode }} / {{ event.responseTimeMs }}ms
              </div>
            </div>
          </el-timeline-item>
        </el-timeline>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import MonitorChart from './MonitorChart.vue'
import {
  aggregateBehaviors,
  aggregatePages,
  aggregateTraffic,
  behaviorType,
  events,
  formatDateTime,
  guests,
  pageViews,
  visits
} from './monitorData'

const traffic = aggregateTraffic()
const pageStats = aggregatePages()
const failedEvents = computed(() => events.filter((item) => !item.success))
const avgResponse = computed(() => Math.round(events.reduce((sum, item) => sum + item.responseTimeMs, 0) / events.length))
const p95Response = computed(() => {
  const sorted = [...events].map((item) => item.responseTimeMs).sort((a, b) => a - b)
  return sorted[Math.floor(sorted.length * 0.95)]
})

const metrics = computed(() => [
  { label: 'Active guests', value: visits.filter((item) => item.active).length, hint: 'live-like sessions', tone: 'good' },
  { label: 'Visits', value: visits.length, hint: `${guests.length} unique guests`, tone: 'neutral' },
  { label: 'Page views', value: pageViews.length, hint: `${(pageViews.length / visits.length).toFixed(1)} pv/visit`, tone: 'neutral' },
  { label: 'Events', value: events.length, hint: 'system + user actions', tone: 'neutral' },
  { label: 'Failed events', value: failedEvents.value.length, hint: `${Math.round(failedEvents.value.length / events.length * 100)}% fail rate`, tone: 'bad' },
  { label: 'Avg response', value: `${avgResponse.value}ms`, hint: `P95 ${p95Response.value}ms`, tone: p95Response.value > 1000 ? 'bad' : 'good' },
  { label: 'Avg stay', value: `${Math.round(pageViews.reduce((sum, item) => sum + item.stayDurationSeconds, 0) / pageViews.length)}s`, hint: 'per page view', tone: 'neutral' },
  { label: 'Top page', value: pageStats[0].page, hint: `${pageStats[0].views} views`, tone: 'neutral' }
])

const topPages = computed(() => pageStats.slice(0, 8))
const recentEvents = computed(() => [...events].sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt)).slice(0, 8))

const trafficOption = computed(() => ({
  tooltip: { trigger: 'axis' },
  legend: { top: 0 },
  grid: { top: 42, right: 20, bottom: 28, left: 38 },
  xAxis: { type: 'category', data: traffic.map((item) => item.label), boundaryGap: false },
  yAxis: { type: 'value' },
  series: [
    { name: 'Visits', type: 'line', smooth: true, data: traffic.map((item) => item.visits) },
    { name: 'Page views', type: 'line', smooth: true, data: traffic.map((item) => item.pageViews) },
    { name: 'Events', type: 'line', smooth: true, data: traffic.map((item) => item.events) },
    { name: 'Failed', type: 'bar', data: traffic.map((item) => item.failed) }
  ]
}))

const behaviorOption = computed(() => ({
  tooltip: { trigger: 'item' },
  legend: { bottom: 0, type: 'scroll' },
  series: [
    {
      name: 'Behavior',
      type: 'pie',
      radius: ['42%', '70%'],
      center: ['50%', '44%'],
      data: aggregateBehaviors()
    }
  ]
}))

const healthOption = computed(() => ({
  tooltip: { trigger: 'item' },
  xAxis: { type: 'category', data: ['2xx', '4xx', '5xx'] },
  yAxis: { type: 'value' },
  grid: { top: 28, right: 20, bottom: 28, left: 36 },
  series: [
    {
      type: 'bar',
      data: [
        events.filter((item) => item.statusCode >= 200 && item.statusCode < 300).length,
        events.filter((item) => item.statusCode >= 400 && item.statusCode < 500).length,
        events.filter((item) => item.statusCode >= 500).length
      ],
      itemStyle: {
        color: (params) => ['#67c23a', '#e6a23c', '#f56c6c'][params.dataIndex]
      }
    }
  ]
}))
</script>

<style scoped>
.monitor-page {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.page-head {
  display: flex;
  justify-content: space-between;
  gap: 16px;
  align-items: flex-start;
}

.page-head h2 {
  margin: 0 0 6px;
  color: #1f2937;
}

.page-head p,
.activity-meta {
  margin: 0;
  color: #6b7280;
}

.metric-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(160px, 1fr));
  gap: 12px;
}

.metric-card {
  border-radius: 8px;
}

.metric-label,
.metric-hint {
  color: #6b7280;
  font-size: 13px;
}

.metric-value {
  margin: 8px 0 4px;
  font-size: 24px;
  font-weight: 700;
  color: #111827;
  word-break: break-word;
}

.metric-hint.good {
  color: #059669;
}

.metric-hint.bad {
  color: #dc2626;
}

.chart-grid {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr;
  gap: 16px;
}

.chart-card {
  min-height: 340px;
}

.content-grid {
  display: grid;
  grid-template-columns: 1.1fr .9fr;
  gap: 16px;
}

.activity-timeline {
  padding-left: 4px;
}

.activity-main {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 600;
}

@media (max-width: 1200px) {
  .metric-grid,
  .chart-grid,
  .content-grid {
    grid-template-columns: 1fr 1fr;
  }

  .chart-card--wide {
    grid-column: 1 / -1;
  }
}

@media (max-width: 760px) {
  .metric-grid,
  .chart-grid,
  .content-grid {
    grid-template-columns: 1fr;
  }
}
</style>
