<template>
  <div v-if="visit" class="detail-page">
    <div class="detail-head">
      <div>
        <el-button link type="primary" :icon="ArrowLeft" @click="router.push('/monitor/visits')">Visits</el-button>
        <h2>Visit #{{ visit.id }}</h2>
        <p>{{ visit.guestHash }} - {{ visit.ip }} - {{ visit.os }} / {{ visit.browser }}</p>
      </div>
      <el-tag :type="visit.active ? 'success' : 'info'">{{ visit.active ? 'Active window' : 'Closed' }}</el-tag>
    </div>

    <div class="metric-grid">
      <el-card v-for="item in metrics" :key="item.label" shadow="never">
        <span>{{ item.label }}</span>
        <strong>{{ item.value }}</strong>
      </el-card>
    </div>

    <el-card shadow="never" class="journey-card">
      <template #header>Journey playback</template>
      <div class="journey-strip">
        <template v-for="(pageView, index) in visitPageViews" :key="pageView.id">
          <button class="journey-node" @click="selectedPageViewId = pageView.id">
            <strong>{{ pageView.page }}</strong>
            <span>{{ pageView.stayDurationSeconds }}s / {{ pageEvents(pageView.id).length }} events</span>
          </button>
          <el-icon v-if="index < visitPageViews.length - 1"><Right /></el-icon>
        </template>
      </div>
    </el-card>

    <div class="chart-grid">
      <el-card shadow="never" class="chart-card">
        <template #header>Behavior in this visit</template>
        <MonitorChart :option="behaviorOption" />
      </el-card>
      <el-card shadow="never" class="chart-card">
        <template #header>Response timeline</template>
        <MonitorChart :option="responseOption" />
      </el-card>
    </div>

    <el-card shadow="never">
      <template #header>PageView timeline</template>
      <el-timeline>
        <el-timeline-item
          v-for="pageView in visitPageViews"
          :key="pageView.id"
          :timestamp="formatDateTime(pageView.createdAt)"
          placement="top"
        >
          <div class="page-block">
            <div class="page-headline">
              <div>
                <strong>{{ pageView.page }}</strong>
                <p>Referrer: {{ pageView.referrer || 'Direct' }}</p>
              </div>
              <el-tag type="info">{{ pageView.stayDurationSeconds }}s</el-tag>
            </div>
            <div v-for="event in pageEvents(pageView.id)" :key="event.id" class="event-row">
              <div>
                <el-tag size="small" :type="behaviorType(event.behavior)">{{ event.behavior }}</el-tag>
                <span>{{ event.content }}</span>
              </div>
              <div>
                <el-tag size="small" :type="statusType(event.success, event.statusCode)">{{ event.statusCode }}</el-tag>
                <el-button link type="primary" @click="router.push(`/monitor/events/${event.id}`)">Detail</el-button>
              </div>
            </div>
          </div>
        </el-timeline-item>
      </el-timeline>
    </el-card>
  </div>
  <el-empty v-else description="Visit not found" />
</template>

<script setup>
import { computed, ref, watchEffect } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ArrowLeft, Right } from '@element-plus/icons-vue'
import MonitorChart from './MonitorChart.vue'
import {
  aggregateBehaviors,
  behaviorType,
  events,
  formatDateTime,
  formatDuration,
  getVisitEvents,
  getVisitPageViews,
  statusType,
  visits
} from './monitorData'

const route = useRoute()
const router = useRouter()
const selectedPageViewId = ref(null)
const visit = computed(() => visits.find((item) => item.id === Number(route.params.id)))
const visitEvents = computed(() => visit.value ? getVisitEvents(visit.value.id) : [])
const visitPageViews = computed(() => visit.value ? getVisitPageViews(visit.value.id) : [])

watchEffect(() => {
  if (!selectedPageViewId.value && visitPageViews.value.length) {
    selectedPageViewId.value = visitPageViews.value[0].id
  }
})

const metrics = computed(() => [
  { label: 'Duration', value: visit.value ? formatDuration(visit.value.startedAt, visit.value.lastActivity) : '-' },
  { label: 'Page views', value: visitPageViews.value.length },
  { label: 'Events', value: visitEvents.value.length },
  { label: 'Failed', value: visitEvents.value.filter((item) => !item.success).length },
  { label: 'Entry', value: visit.value?.entryPage || '-' },
  { label: 'Exit', value: visit.value?.exitPage || '-' }
])

const behaviorOption = computed(() => ({
  tooltip: { trigger: 'item' },
  legend: { bottom: 0, type: 'scroll' },
  series: [{ type: 'pie', radius: ['40%', '68%'], data: aggregateBehaviors(visitEvents.value) }]
}))

const responseOption = computed(() => ({
  tooltip: { trigger: 'axis' },
  grid: { top: 24, right: 20, bottom: 50, left: 42 },
  xAxis: { type: 'category', data: visitEvents.value.map((item) => item.behavior), axisLabel: { rotate: 25 } },
  yAxis: { type: 'value' },
  series: [{ type: 'line', smooth: true, data: visitEvents.value.map((item) => item.responseTimeMs) }]
}))

function pageEvents(pageViewId) {
  return events.filter((item) => item.pageViewId === pageViewId)
}
</script>

<style scoped>
.detail-page { display: flex; flex-direction: column; gap: 16px; }
.detail-head { display: flex; justify-content: space-between; gap: 16px; align-items: flex-start; }
.detail-head h2 { margin: 4px 0 6px; color: #1f2937; }
.detail-head p, .page-headline p { margin: 0; color: #6b7280; }
.metric-grid { display: grid; grid-template-columns: repeat(6, 1fr); gap: 12px; }
.metric-grid :deep(.el-card__body) { display: flex; flex-direction: column; gap: 8px; }
.metric-grid span { color: #6b7280; font-size: 12px; }
.metric-grid strong { font-size: 18px; color: #111827; word-break: break-word; }
.journey-strip { display: flex; align-items: center; gap: 8px; overflow-x: auto; }
.journey-node { min-width: 150px; border: 1px solid #dcdfe6; border-radius: 8px; background: #f8fafc; padding: 10px; text-align: left; cursor: pointer; }
.journey-node span { display: block; color: #6b7280; font-size: 12px; margin-top: 4px; }
.chart-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
.chart-card { height: 340px; }
.page-block { border: 1px solid #ebeef5; border-radius: 8px; overflow: hidden; }
.page-headline { display: flex; justify-content: space-between; gap: 12px; padding: 12px; background: #f8fafc; }
.event-row { display: flex; justify-content: space-between; gap: 12px; padding: 10px 12px; border-top: 1px solid #f0f2f5; }
.event-row div { display: flex; align-items: center; gap: 8px; }
@media (max-width: 1100px) { .metric-grid, .chart-grid { grid-template-columns: 1fr 1fr; } }
@media (max-width: 760px) { .metric-grid, .chart-grid, .detail-head, .event-row { grid-template-columns: 1fr; flex-direction: column; } }
</style>
