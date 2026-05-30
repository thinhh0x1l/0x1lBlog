<template>
  <div class="">

    <div class="page-header">
      <h2 class="page-title">Quản lý khách truy cập</h2>
      <p class="page-description">Theo dõi và quản lý thông tin khách truy cập website</p>
    </div>


    <el-card class="filter-card" shadow="never">
      <el-form :inline="true" class="filter-form">
        <el-form-item label="Thời gian truy cập cuối">
          <DateTimeRangePicker
              :date="queryInfo.date"
              :setDate="setDate"
              class="date-picker"
          />
        </el-form-item>

        <el-form-item>
          <el-button
              type="primary"
              :icon="Search"
              @click="search"
              class="search-btn"
          >
            Tìm kiếm
          </el-button>
        </el-form-item>

        <!-- Switch toggle với animation đẹp -->
        <div class="toggle-switch">
          <div
              class="toggle-switch__container"
              @click="currently = !currently"
          >
            <div
                class="toggle-switch__slider"
                :class="{ 'toggle-switch__slider--active': currently }"
            >
              <span class="toggle-switch__text">Hiện tại</span>
              <span class="toggle-switch__text">Toàn bộ</span>
            </div>
          </div>
          <span class="toggle-switch__label">
            {{ currently ? 'Hiển thị khách đang hoạt động' : 'Hiển thị tất cả khách' }}
          </span>
        </div>
      </el-form>
    </el-card>

    <!-- Bảng dữ liệu với animation -->
    <el-card class="table-card" shadow="never">
      <transition name="fade" mode="out-in">
        <el-table
            :data="visitorList"
            stripe
            border
            highlight-current-row
            class="visitor-table"
            :key="currently"
            v-loading="loading"
        >
          <el-table-column
              label="Mã định danh khách"
              min-width="100"
              show-overflow-tooltip
          >
            <template #default="scope">
              <div class="guest-id">
                <span class="guest-id__hash">{{ scope.row.guestIdHash }}</span>
                <span v-if="scope.row.u" class="badge badge--self">Bạn</span>
              </div>
            </template>
          </el-table-column>

          <el-table-column label="IP" prop="ip" min-width="120">
            <template #default="scope">
              <span class="ip-address">{{ scope.row.ip }}</span>
            </template>
          </el-table-column>

          <el-table-column label="Nguồn IP" prop="ipSource" min-width="140" show-overflow-tooltip />

          <el-table-column label="Hệ điều hành" prop="os" min-width="120">
            <template #default="scope">
              <div class="os-info">
                <el-icon><Monitor /></el-icon>
                <span>{{ scope.row.os || 'Unknown' }}</span>
              </div>
            </template>
          </el-table-column>

          <el-table-column label="Trình duyệt" prop="browser" min-width="120">
            <template #default="scope">
              <div class="browser-info">
                <el-icon><ChromeFilled /></el-icon>
                <span>{{ scope.row.browser || 'Unknown' }}</span>
              </div>
            </template>
          </el-table-column>

          <el-table-column label="Truy cập lần đầu" width="180">
            <template #default="scope">
              <div class="date-info">
                <el-icon><Calendar /></el-icon>
                <span>{{ formatDate(scope.row.startedAt) }}</span>
              </div>
            </template>
          </el-table-column>

          <el-table-column width="180">
            <template #header>
              <div class="header-with-tooltip">
                <span>Truy cập cuối</span>
                <el-tooltip
                    effect="dark"
                    content="Tự động cập nhật hàng ngày lúc 0h"
                    placement="top"
                >
                  <el-icon class="help-icon"><QuestionFilled /></el-icon>
                </el-tooltip>
              </div>
            </template>
            <template #default="scope">
              <div class="date-info">
                <el-icon><Clock /></el-icon>
                <span :class="currently&&isOnline(scope.row.lastActivity)?'is-online':''"  >{{
                    currently&&formatRelativeTimeOrDate(scope.row.lastActivity) ||
                    formatDate(scope.row.lastActivity)
                  }}</span>
              </div>
            </template>
          </el-table-column>

          <el-table-column prop="pv" width="80" align="center">
            <template #header>
              <div class="header-with-tooltip">
                <span>PV</span>
                <el-tooltip
                    effect="dark"
                    content="Tổng số lượt xem của khách, tự động cập nhật hàng ngày"
                    placement="top"
                >
                  <el-icon class="help-icon"><QuestionFilled /></el-icon>
                </el-tooltip>
              </div>
            </template>
            <template #default="scope">
              <el-tag size="small" type="primary" effect="dark">
                {{ scope.row.pv }}
              </el-tag>
            </template>
          </el-table-column>

          <el-table-column label="Thao tác" width="220" :fixed="false">
            <template #default="scope">
              <div class="action-buttons">
                <el-button
                    type="primary"
                    :icon="View"
                    size="small"
                    link
                    @click="showLog(scope.row.guestIdHash)"
                >
                  Lịch sử
                </el-button>
                <el-popconfirm
                    title="Xác nhận xóa?"
                    icon-color="#f56c6c"
                    confirm-button-text="Xóa"
                    cancel-button-text="Hủy"
                    @confirm="deleteVisitorById(scope.row)"
                >
                  <template #reference>
                    <el-button
                        size="small"
                        type="danger"
                        :icon="Delete"
                        link
                    >
                      Xóa
                    </el-button>
                  </template>
                </el-popconfirm>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </transition>

      <div class="pagination-wrapper">
        <el-pagination
            v-model:current-page="queryInfo.pageNum"
            v-model:page-size="queryInfo.pageSize"
            :page-sizes="[10, 20, 30, 50, 100]"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
            background
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  Search,
  QuestionFilled,
  View,
  Delete,
  Monitor,
  ChromeFilled,
  Calendar,
  Clock
} from '@element-plus/icons-vue'
import DateTimeRangePicker from '@/components/data_time_picker/DateTimeRangePicker.vue'
import { fVisit } from "@/api/visit.js"
import {formatRelativeTimeOrDate,formatDate, isOnline} from "@/util/dateTimeFormatUtils.js";

const router = useRouter()

// State
const loading = ref(false)
const visitorList = ref([])
const total = ref(0)
const currently = ref(true)
const dataVisitFull = ref({})

const queryInfo = reactive({
  date: [],
  pageNum: 1,
  pageSize: 10
})


const getData = async () => {
  loading.value = true
  let query = { ...queryInfo }

  if (query.date && query.date.length === 2) {
    query.date = query.date[0] + ',' + query.date[1]
  }

  try {
    const res = await fVisit(query)
    dataVisitFull.value = res.data

    setTimeout(() => {
      if (currently.value) {
        visitorList.value = res.data.currently || []
        total.value = res.data.currently.length || 0
      } else {
        visitorList.value = res.data.nonCurrently?.items || []
        total.value = res.data.nonCurrently?.totalElements || 0
      }
      loading.value = false
    }, 300)
  } catch (error) {
    ElMessage.error('Lỗi khi tải dữ liệu')
    loading.value = false
  }
}

const handleSizeChange = (newSize) => {
  queryInfo.pageSize = newSize
  queryInfo.pageNum = 1
  getData()
}

const handleCurrentChange = (newPage) => {
  queryInfo.pageNum = newPage
  getData()
}

const deleteVisitorById = async (visitor) => {
  try {
    // await deleteVisitor(visitor.id, visitor.uuid)
    ElMessage.success('Xóa thành công')
    getData()
  } catch (error) {
    ElMessage.error('Xóa thất bại')
  }
}

const showLog = (guestId) => {
  router.push({
    path: '/log/visit',
    query: { guestId }
  })
}

const search = () => {
  queryInfo.pageNum = 1
  queryInfo.pageSize = 10
  getData()
}

const setDate = (value) => {
  queryInfo.date = value
}

watch(currently, (newVal, oldVal) => {
  if (oldVal !== undefined) {
    if (newVal) {
      visitorList.value = dataVisitFull.value.currently || []
      total.value = dataVisitFull.value.currently.length || 0
    } else {
      visitorList.value = dataVisitFull.value.nonCurrently?.items || []
      total.value = dataVisitFull.value.nonCurrently?.totalElements || 0
    }
  }
})

onMounted(() => {
  getData()
})
</script>

<style scoped>
.visitor-management {
  padding: 20px;
  background: #f0f2f5;
  min-height: 100vh;
}

.page-header {
  margin-bottom: 24px;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  color: #1a1a1a;
  margin: 0 0 8px 0;
}

.page-description {
  font-size: 14px;
  color: #666;
  margin: 0;
}

.filter-card {
  margin-bottom: 20px;
  border-radius: 12px;
}

.filter-card :deep(.el-card__body) {
  padding: 20px;
}

.filter-form {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 16px;
}

.date-picker {
  width: 320px;
}

.search-btn {
  border-radius: 8px;
  padding: 8px 20px;
}


.toggle-switch {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-left: auto;
}

.toggle-switch__container {
  cursor: pointer;
  background: #e4e7ed;
  border-radius: 40px;
  padding: 4px;
  width: 160px;
  transition: all 0.3s ease;
}

.toggle-switch__slider {
  display: flex;
  justify-content: space-between;
  background: #fff;
  border-radius: 36px;
  transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.toggle-switch__slider--active {
  transform: translateX(0);
}

.toggle-switch__slider--active .toggle-switch__text:first-child {
  background: #409eff;
  color: white;
  border-radius: 36px;
}

.toggle-switch__slider--active .toggle-switch__text:last-child {
  background: transparent;
  color: #606266;
}

.toggle-switch__text {
  flex: 1;
  text-align: center;
  padding: 8px 16px;
  font-size: 13px;
  font-weight: 500;
  transition: all 0.3s ease;
  cursor: pointer;
}

.toggle-switch__text:first-child {
  background: transparent;
  color: #606266;
  border-radius: 36px;
}

.toggle-switch__text:last-child {
  background: #409eff;
  color: white;
  border-radius: 36px;
}

.toggle-switch__label {
  font-size: 13px;
  color: #606266;

}

.table-card {
}

.table-card :deep(.el-card__body) {
  padding: 0;
}

.visitor-table {
  width: 100%;
}

.guest-id {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

.guest-id__hash {
  font-family: monospace;
  font-size: 12px;
  color: #409eff;
}

/* Badge */
.badge {
  display: inline-block;
  padding: 2px 8px;
  border-radius: 5px;
  font-size: 11px;
  font-weight: 500;
}

.badge--self {
  background: linear-gradient(135deg, #fb4868 0%, #764ba2 100%);
  color: white;
}

.ip-address {
  font-family: monospace;
  font-weight: 500;
}

.os-info, .browser-info, .date-info {
  display: flex;
  align-items: center;
  gap: 6px;
}

.os-info .el-icon,
.browser-info .el-icon,
.date-info .el-icon {
  font-size: 14px;
  color: #909399;
}

/* Header with tooltip */
.header-with-tooltip {
  display: flex;
  align-items: center;
  gap: 6px;
}

.help-icon {
  font-size: 14px;
  color: #909399;
  cursor: help;
}

/* Action buttons */
.action-buttons {
  display: flex;
  gap: 8px;
}

.pagination-wrapper {
  padding: 20px;
  display: flex;
  justify-content: flex-end;
  border-top: 1px solid #ebeef5;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease, transform 0.3s ease;
}

.fade-enter-from {
  opacity: 0;
  transform: translateY(10px);
}

.fade-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}


@media (max-width: 768px) {
  .visitor-management {
    padding: 12px;
  }

  .filter-form {
    flex-direction: column;
    align-items: stretch;
  }

  .toggle-switch {
    margin-left: 0;
    justify-content: space-between;
  }

  .date-picker {
    width: 100%;
  }

  .action-buttons {
    flex-direction: column;
    gap: 4px;
  }
}

.is-online{
  background: linear-gradient(135deg, #d3ff82 0%, #00fb3b 100%);
  color: white;
  border-radius: 6px;
  padding: 3px 7px;
}

:deep(.el-loading-mask) {
  border-radius: 12px;
}
</style>