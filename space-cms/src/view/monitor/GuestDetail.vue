<template>
  <div v-if="guest" class="detail-page">
    <div class="detail-head">
      <div>
        <el-button link type="primary" :icon="ArrowLeft" @click="router.push('/monitor/guests')">Guests</el-button>
        <h2>{{ guest.guestHash }}</h2>
        <p>{{ guest.lastIp }} - {{ guest.ipSource }} - {{ guest.os }} / {{ guest.browser }}</p>
      </div>
      <el-tag :type="guest.status === 'Risk' ? 'danger' : 'success'">{{ guest.status }}</el-tag>
    </div>

    <div class="metric-grid">
      <el-card v-for="item in metrics" :key="item.label" shadow="never">
        <span>{{ item.label }}</span>
        <strong>{{ item.value }}</strong>
      </el-card>
    </div>

    <div class="chart-grid">
      <el-card shadow="never" class="chart-card">
        <template #header>Behavior profile</template>
        <MonitorChart :option="behaviorOption" />
      </el-card>
      <el-card shadow="never" class="chart-card">
        <template #header>Visit duration</template>
        <MonitorChart :option="visitOption" />
      </el-card>
    </div>

    <el-card shadow="never">
      <template #header>Guest activity timeline</template>
      <el-timeline>
        <el-timeline-item
          v-for="visit in guestVisits"
          :key="visit.id"
          :timestamp="formatDateTime(visit.startedAt)"
          placement="top"
        >
          <div class="timeline-card">
            <div class="timeline-title">
              <strong>{{ visit.entryPage }} -> {{ visit.exitPage }}</strong>
              <el-tag :type="visit.active ? 'success' : 'info'">{{ visit.active ? 'Active' : 'Closed' }}</el-tag>
            </div>
            <div class="muted">{{ formatDuration(visit.startedAt, visit.lastActivity) }} / {{ visit.pv }} PV / {{ visitEvents(visit.id).length }} events</div>
            <div class="event-strip">
              <el-tag v-for="event in visitEvents(visit.id).slice(0, 8)" :key="event.id" size="small" :type="behaviorType(event.behavior)">
                {{ event.behavior }}
              </el-tag>
            </div>
          </div>
        </el-timeline-item>
      </el-timeline>
    </el-card>
  </div>
  <el-empty v-else description="Guest not found" />
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import MonitorChart from './MonitorChart.vue'
import {
  aggregateBehaviors,
  behaviorType,
  events,
  formatDateTime,
  formatDuration,
  getGuestEvents,
  getGuestVisits,
  getVisitEvents,
  guests
} from './monitorData'

const route = useRoute()
const router = useRouter()
const guest = computed(() => guests.find((item) => item.id === Number(route.params.id)))
const guestVisits = computed(() => guest.value ? getGuestVisits(guest.value.id) : [])
const guestEvents = computed(() => guest.value ? getGuestEvents(guest.value.id) : [])

const metrics = computed(() => [
  { label: 'Visits', value: guestVisits.value.length },
  { label: 'Page views', value: guestVisits.value.reduce((sum, item) => sum + item.pv, 0) },
  { label: 'Events', value: guestEvents.value.length },
  { label: 'Failed', value: guestEvents.value.filter((item) => !item.success).length },
  { label: 'First seen', value: guest.value ? formatDateTime(guest.value.firstSeen) : '-' },
  { label: 'Last seen', value: guest.value ? formatDateTime(guest.value.lastSeen) : '-' }
])

const behaviorOption = computed(() => ({
  tooltip: { trigger: 'item' },
  legend: { bottom: 0, type: 'scroll' },
  series: [{ type: 'pie', radius: ['40%', '68%'], data: aggregateBehaviors(guestEvents.value) }]
}))

const visitOption = computed(() => ({
  tooltip: { trigger: 'axis' },
  grid: { top: 24, right: 20, bottom: 50, left: 42 },
  xAxis: { type: 'category', data: guestVisits.value.map((item) => `#${item.id}`) },
  yAxis: { type: 'value' },
  series: [{ type: 'bar', data: guestVisits.value.map((item) => Math.round((new Date(item.lastActivity) - new Date(item.startedAt)) / 1000)) }]
}))

function visitEvents(visitId) {
  return getVisitEvents(visitId)
}
</script>

<style scoped>
.detail-page { display: flex; flex-direction: column; gap: 16px; }
.detail-head { display: flex; justify-content: space-between; gap: 16px; align-items: flex-start; }
.detail-head h2 { margin: 4px 0 6px; color: #1f2937; }
.detail-head p, .muted { margin: 0; color: #6b7280; }
.metric-grid { display: grid; grid-template-columns: repeat(6, 1fr); gap: 12px; }
.metric-grid :deep(.el-card__body) { display: flex; flex-direction: column; gap: 8px; }
.metric-grid span { color: #6b7280; font-size: 12px; }
.metric-grid strong { font-size: 18px; color: #111827; }
.chart-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
.chart-card { height: 340px; }
.timeline-card { border: 1px solid #ebeef5; border-radius: 8px; padding: 12px; }
.timeline-title { display: flex; justify-content: space-between; gap: 10px; }
.event-strip { display: flex; gap: 6px; flex-wrap: wrap; margin-top: 10px; }
@media (max-width: 1100px) { .metric-grid, .chart-grid { grid-template-columns: 1fr 1fr; } }
@media (max-width: 700px) { .metric-grid, .chart-grid, .detail-head { grid-template-columns: 1fr; flex-direction: column; } }
</style>
