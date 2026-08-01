<template>
  <div class="monitor-page">
    <div class="page-head">
      <div>
        <h2>Active Window</h2>
        <p>Hoat dong trong cua so gan day, khong phai realtime tuyet doi. Mac dinh xem nhu 30 phut active.</p>
      </div>
      <el-segmented v-model="windowSize" :options="['5m', '15m', '30m']" />
    </div>

    <div class="layout-grid">
      <el-card shadow="never">
        <template #header>Active guests</template>
        <div v-for="guest in activeGuests" :key="guest.id" class="active-row">
          <div>
            <strong>{{ guest.guestHash }}</strong>
            <span>{{ guest.ipSource }} / {{ guest.os }} / {{ guest.browser }}</span>
          </div>
          <el-button link type="primary" @click="router.push(`/monitor/guests/${guest.id}`)">Detail</el-button>
        </div>
      </el-card>

      <el-card shadow="never">
        <template #header>Active visits</template>
        <div v-for="visit in activeVisits" :key="visit.id" class="active-row">
          <div>
            <strong>{{ visit.guestHash }}</strong>
            <span>{{ visit.entryPage }} -> {{ visit.exitPage }}</span>
          </div>
          <el-button link type="primary" @click="router.push(`/monitor/visits/${visit.id}`)">Detail</el-button>
        </div>
      </el-card>

      <el-card shadow="never" class="chart-panel">
        <template #header>Hot pages in window</template>
        <MonitorChart :option="hotPagesOption" />
      </el-card>

      <el-card shadow="never" class="stream-card">
        <template #header>Activity stream</template>
        <div v-for="event in liveEvents" :key="event.id" class="stream-row">
          <div class="stream-top">
            <el-tag size="small" :type="behaviorType(event.behavior)">{{ event.behavior }}</el-tag>
            <strong>{{ event.page }}</strong>
            <span>{{ formatDateTime(event.createdAt) }}</span>
          </div>
          <div class="stream-bottom">
            <span>{{ event.method }} {{ event.uri }}</span>
            <div>
              <el-tag size="small" :type="statusType(event.success, event.statusCode)">{{ event.statusCode }}</el-tag>
              <span :class="{ slow: event.responseTimeMs > 800 }">{{ event.responseTimeMs }}ms</span>
              <el-button link type="primary" @click="router.push(`/monitor/events/${event.id}`)">Detail</el-button>
            </div>
          </div>
        </div>
      </el-card>

      <el-card shadow="never">
        <template #header>Slow and failed</template>
        <div v-for="event in slowFailedEvents" :key="event.id" class="alert-row">
          <div>
            <strong>{{ event.behavior }}</strong>
            <span>{{ event.remark || event.content }}</span>
          </div>
          <el-tag :type="statusType(event.success, event.statusCode)">{{ event.statusCode }}</el-tag>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import MonitorChart from './MonitorChart.vue'
import { aggregatePages, behaviorType, events, formatDateTime, guests, statusType, visits } from './monitorData'

const router = useRouter()
const windowSize = ref('15m')
const activeVisits = computed(() => visits.filter((item) => item.active).slice(0, 12))
const activeGuestIds = computed(() => new Set(activeVisits.value.map((item) => item.guestId)))
const activeGuests = computed(() => guests.filter((item) => activeGuestIds.value.has(item.id)).slice(0, 12))
const liveEvents = computed(() => [...events].sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt)).slice(0, 24))
const slowFailedEvents = computed(() => events.filter((item) => !item.success || item.responseTimeMs > 800).slice(0, 12))
const hotPages = computed(() => aggregatePages().slice(0, 7))

const hotPagesOption = computed(() => ({
  tooltip: { trigger: 'axis' },
  grid: { top: 20, right: 20, bottom: 48, left: 38 },
  xAxis: { type: 'category', data: hotPages.value.map((item) => item.page), axisLabel: { rotate: 30 } },
  yAxis: { type: 'value' },
  series: [{ type: 'bar', data: hotPages.value.map((item) => item.views), itemStyle: { color: '#67c23a' } }]
}))
</script>

<style scoped>
.monitor-page { display: flex; flex-direction: column; gap: 16px; }
.page-head { display: flex; justify-content: space-between; gap: 16px; align-items: flex-start; }
.page-head h2 { margin: 0 0 6px; color: #1f2937; }
.page-head p { margin: 0; color: #6b7280; }
.layout-grid { display: grid; grid-template-columns: .8fr .8fr 1.2fr; gap: 16px; }
.stream-card { grid-row: span 2; }
.chart-panel { grid-column: span 2; }
.active-row, .alert-row { display: flex; justify-content: space-between; gap: 12px; padding: 10px 0; border-bottom: 1px solid #f0f2f5; }
.active-row div, .alert-row div { display: flex; flex-direction: column; gap: 4px; }
.active-row span, .alert-row span, .stream-top span, .stream-bottom { color: #6b7280; font-size: 12px; }
.stream-row { padding: 12px 0; border-bottom: 1px solid #f0f2f5; }
.stream-top, .stream-bottom { display: flex; justify-content: space-between; align-items: center; gap: 10px; }
.stream-top { margin-bottom: 6px; }
.stream-bottom div { display: flex; align-items: center; gap: 8px; }
.slow { color: #dc2626; font-weight: 700; }
@media (max-width: 1200px) { .layout-grid { grid-template-columns: 1fr 1fr; } .stream-card, .chart-panel { grid-row: auto; grid-column: auto; } }
@media (max-width: 760px) { .layout-grid { grid-template-columns: 1fr; } .page-head { flex-direction: column; } }
</style>
