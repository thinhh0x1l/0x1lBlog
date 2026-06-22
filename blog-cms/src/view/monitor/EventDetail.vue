<template>
  <div v-if="event" class="detail-page">
    <div class="detail-head">
      <div>
        <el-button link type="primary" :icon="ArrowLeft" @click="router.push('/monitor/events')">Events</el-button>
        <h2>{{ event.behavior }} #{{ event.id }}</h2>
        <p>{{ event.method }} {{ event.uri }} - {{ event.page }}</p>
      </div>
      <el-tag :type="statusType(event.success, event.statusCode)" effect="dark">{{ event.statusCode }}</el-tag>
    </div>

    <div class="metric-grid">
      <el-card shadow="never"><span>Source</span><strong>{{ event.source }}</strong></el-card>
      <el-card shadow="never"><span>Success</span><strong>{{ event.success ? 'Yes' : 'No' }}</strong></el-card>
      <el-card shadow="never"><span>Response</span><strong>{{ event.responseTimeMs }}ms</strong></el-card>
      <el-card shadow="never"><span>Guest</span><strong>#{{ event.guestId }}</strong></el-card>
      <el-card shadow="never"><span>Visit</span><strong>#{{ event.visitId }}</strong></el-card>
      <el-card shadow="never"><span>PageView</span><strong>#{{ event.pageViewId }}</strong></el-card>
    </div>

    <div class="detail-grid">
      <el-card shadow="never">
        <template #header>Request context</template>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="Created">{{ formatDateTime(event.createdAt) }}</el-descriptions-item>
          <el-descriptions-item label="Content">{{ event.content }}</el-descriptions-item>
          <el-descriptions-item label="Remark">{{ event.remark || '-' }}</el-descriptions-item>
          <el-descriptions-item label="Param">{{ event.param }}</el-descriptions-item>
        </el-descriptions>
      </el-card>

      <el-card shadow="never">
        <template #header>Related navigation</template>
        <div class="link-list">
          <el-button type="primary" plain @click="router.push(`/monitor/guests/${event.guestId}`)">Guest detail</el-button>
          <el-button type="primary" plain @click="router.push(`/monitor/visits/${event.visitId}`)">Visit detail</el-button>
          <el-button type="primary" plain @click="router.push(`/monitor/pages/${encodeURIComponent(event.page)}`)">Page detail</el-button>
        </div>
      </el-card>
    </div>

    <el-card shadow="never">
      <template #header>Nearby event timeline</template>
      <el-timeline>
        <el-timeline-item
          v-for="item in nearbyEvents"
          :key="item.id"
          :timestamp="formatDateTime(item.createdAt)"
          placement="top"
        >
          <div class="nearby-row" :class="{ current: item.id === event.id }">
            <div>
              <el-tag size="small" :type="behaviorType(item.behavior)">{{ item.behavior }}</el-tag>
              <strong>{{ item.content }}</strong>
            </div>
            <div>
              <el-tag size="small" :type="statusType(item.success, item.statusCode)">{{ item.statusCode }}</el-tag>
              <span>{{ item.responseTimeMs }}ms</span>
            </div>
          </div>
        </el-timeline-item>
      </el-timeline>
    </el-card>
  </div>
  <el-empty v-else description="Event not found" />
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import { behaviorType, events, formatDateTime, statusType } from './monitorData'

const route = useRoute()
const router = useRouter()
const event = computed(() => events.find((item) => item.id === Number(route.params.id)))
const nearbyEvents = computed(() => {
  if (!event.value) return []
  return events
    .filter((item) => item.visitId === event.value.visitId)
    .sort((a, b) => new Date(a.createdAt) - new Date(b.createdAt))
})
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
.detail-grid { display: grid; grid-template-columns: 1.2fr .8fr; gap: 16px; }
.link-list { display: flex; flex-direction: column; gap: 10px; align-items: stretch; }
.link-list :deep(.el-button) { margin-left: 0; }
.nearby-row { display: flex; justify-content: space-between; gap: 12px; border: 1px solid #ebeef5; border-radius: 8px; padding: 12px; }
.nearby-row.current { border-color: #409eff; background: #ecf5ff; }
.nearby-row div { display: flex; align-items: center; gap: 8px; }
.nearby-row span { color: #6b7280; }
@media (max-width: 1100px) { .metric-grid, .detail-grid { grid-template-columns: 1fr 1fr; } }
@media (max-width: 760px) { .metric-grid, .detail-grid, .detail-head, .nearby-row { grid-template-columns: 1fr; flex-direction: column; align-items: stretch; } }
</style>
