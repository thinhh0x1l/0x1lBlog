<template>
  <div class="monitor-page">
    <div class="page-head">
      <div>
        <h2>Guests</h2>
        <p>Phan tich visitor an danh theo tan suat, hanh vi va risk signal.</p>
      </div>
      <el-input v-model="keyword" clearable :prefix-icon="Search" placeholder="Search guest, IP, location" />
    </div>

    <el-card shadow="never">
      <el-table :data="pagedRows" stripe height="620" @row-click="openGuest">
        <el-table-column label="Guest" min-width="170">
          <template #default="{ row }">
            <div class="identity">
              <el-avatar>{{ row.label }}</el-avatar>
              <div>
                <div class="strong">{{ row.guestHash }}</div>
                <div class="muted">ID {{ row.id }}</div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="Last IP / Location" min-width="190">
          <template #default="{ row }">
            <div class="mono">{{ row.lastIp }}</div>
            <div class="muted">{{ row.ipSource }}</div>
          </template>
        </el-table-column>
        <el-table-column label="Sessions" width="100" align="center" prop="sessions" />
        <el-table-column label="PV" width="90" align="center" prop="pv" />
        <el-table-column label="Events" width="100" align="center" prop="eventCount" />
        <el-table-column label="Failed" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.failed ? 'danger' : 'success'">{{ row.failed }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Device" min-width="170">
          <template #default="{ row }">{{ row.os }} / {{ row.browser }}</template>
        </el-table-column>
        <el-table-column label="Status" width="110">
          <template #default="{ row }">
            <el-tag :type="row.status === 'Risk' ? 'danger' : row.status === 'Returning' ? 'warning' : 'info'">
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Last seen" width="160">
          <template #default="{ row }">{{ formatDateTime(row.lastSeen) }}</template>
        </el-table-column>
        <el-table-column label="Action" width="170" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" :icon="View" @click.stop="openGuest(row)">Profile</el-button>
            <el-button link type="primary" @click.stop="router.push(`/monitor/guests/${row.id}`)">Detail</el-button>
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

    <el-drawer v-model="drawerVisible" size="620px" :with-header="false">
      <div v-if="selected" class="drawer-body">
        <div class="drawer-head">
          <div class="identity">
            <el-avatar :size="44">{{ selected.label }}</el-avatar>
            <div>
              <h3>{{ selected.guestHash }}</h3>
              <p>{{ selected.lastIp }} - {{ selected.ipSource }}</p>
            </div>
          </div>
          <el-tag :type="selected.status === 'Risk' ? 'danger' : 'success'">{{ selected.status }}</el-tag>
        </div>

        <div class="mini-grid">
          <div><span>Sessions</span><strong>{{ selected.sessions }}</strong></div>
          <div><span>PV</span><strong>{{ selected.pv }}</strong></div>
          <div><span>Events</span><strong>{{ selected.eventCount }}</strong></div>
          <div><span>Failed</span><strong>{{ selected.failed }}</strong></div>
        </div>

        <el-card shadow="never" class="chart-box">
          <template #header>Behavior profile</template>
          <MonitorChart :option="behaviorOption" />
        </el-card>

        <el-divider />
        <h4>Recent visits</h4>
        <el-table :data="guestVisitRows" size="small">
          <el-table-column label="Started" width="150">
            <template #default="{ row }">{{ formatDateTime(row.startedAt) }}</template>
          </el-table-column>
          <el-table-column label="Journey" min-width="220">
            <template #default="{ row }">{{ row.entryPage }} -> {{ row.exitPage }}</template>
          </el-table-column>
          <el-table-column label="PV" prop="pv" width="60" />
        </el-table>

        <el-divider />
        <h4>Failed events</h4>
        <div v-for="event in guestFailedEvents" :key="event.id" class="event-card">
          <div>
            <strong>{{ event.behavior }}</strong>
            <span>{{ event.page }}</span>
          </div>
          <el-tag :type="statusType(event.success, event.statusCode)">{{ event.statusCode }}</el-tag>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import { Search, View } from '@element-plus/icons-vue'
import MonitorChart from './MonitorChart.vue'
import {
  aggregateBehaviors,
  events,
  formatDateTime,
  getGuestEvents,
  getGuestVisits,
  guests,
  statusType
} from './monitorData'

const keyword = ref('')
const router = useRouter()
const page = ref(1)
const pageSize = ref(20)
const drawerVisible = ref(false)
const selected = ref(null)

const rows = computed(() => guests.map((guest) => {
  const guestVisits = getGuestVisits(guest.id)
  const guestEvents = getGuestEvents(guest.id)
  return {
    ...guest,
    sessions: guestVisits.length,
    pv: guestVisits.reduce((sum, item) => sum + item.pv, 0),
    eventCount: guestEvents.length,
    failed: guestEvents.filter((item) => !item.success).length
  }
}))

const filteredRows = computed(() => {
  const key = keyword.value.trim().toLowerCase()
  if (!key) return rows.value
  return rows.value.filter((row) => [row.guestHash, row.lastIp, row.ipSource, row.os, row.browser].join(' ').toLowerCase().includes(key))
})

const pagedRows = computed(() => {
  const start = (page.value - 1) * pageSize.value
  return filteredRows.value.slice(start, start + pageSize.value)
})

const guestVisitRows = computed(() => selected.value ? getGuestVisits(selected.value.id).slice(0, 8) : [])
const guestFailedEvents = computed(() => selected.value ? getGuestEvents(selected.value.id).filter((item) => !item.success).slice(0, 8) : [])
const behaviorOption = computed(() => ({
  tooltip: { trigger: 'item' },
  legend: { bottom: 0, type: 'scroll' },
  series: [{
    type: 'pie',
    radius: ['40%', '68%'],
    data: selected.value ? aggregateBehaviors(events.filter((item) => item.guestId === selected.value.id)) : []
  }]
}))

function openGuest(row) {
  selected.value = row
  drawerVisible.value = true
}
</script>

<style scoped>
.monitor-page { display: flex; flex-direction: column; gap: 16px; }
.page-head { display: flex; justify-content: space-between; gap: 16px; align-items: flex-start; }
.page-head h2, .drawer-head h3 { margin: 0 0 6px; color: #1f2937; }
.page-head p, .drawer-head p, .muted { margin: 0; color: #6b7280; font-size: 13px; }
.identity { display: flex; align-items: center; gap: 10px; }
.strong { font-weight: 650; color: #1f2937; }
.mono { font-family: Consolas, Monaco, monospace; }
.pager { padding: 16px; display: flex; justify-content: flex-end; border-top: 1px solid #ebeef5; }
.drawer-body { padding: 22px; }
.drawer-head { display: flex; justify-content: space-between; gap: 14px; align-items: flex-start; margin-bottom: 16px; }
.mini-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 10px; margin-bottom: 16px; }
.mini-grid div { border: 1px solid #ebeef5; border-radius: 8px; padding: 12px; background: #fafafa; }
.mini-grid span { display: block; color: #6b7280; font-size: 12px; margin-bottom: 6px; }
.mini-grid strong { font-size: 20px; }
.chart-box { height: 340px; }
.event-card { display: flex; justify-content: space-between; gap: 10px; padding: 10px 0; border-bottom: 1px solid #f0f2f5; }
.event-card span { display: block; color: #6b7280; font-size: 12px; margin-top: 3px; }
@media (max-width: 760px) { .page-head, .drawer-head { flex-direction: column; } .mini-grid { grid-template-columns: 1fr 1fr; } }
</style>
