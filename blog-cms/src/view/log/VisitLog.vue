<template>
  <div>
    <!-- Tìm kiếm -->
    <el-form inline>
      <el-form-item label="Mã khách truy cập">
        <el-input
            v-model="queryInfo.uuid"
            clearable
            size="small"
            @keyup.enter="search"
            placeholder="Nhập mã khách truy cập"
            style="min-width: 300px">
        </el-input>
      </el-form-item>
      <el-form-item label="Thời gian truy cập">
        <DateTimeRangePicker :date="queryInfo.date" :setDate="setDate"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="small" :icon="Search" @click="search">Tìm kiếm</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="logList" border>
      <el-table-column type="expand">
        <template #default="props">
          <el-form label-position="left" class="table-expand">
            <el-form-item label="Mã khách truy cập">
              <span>{{ props.row.uuid }}</span>
            </el-form-item>
            <el-form-item label="Phương thức yêu cầu">
              <span>{{ props.row.method }}</span>
            </el-form-item>
            <el-form-item label="API yêu cầu">
              <span>{{ props.row.uri }}</span>
            </el-form-item>
            <el-form-item label="Tham số yêu cầu">
              <span>{{ props.row.param }}</span>
            </el-form-item>
            <el-form-item label="Ghi chú">
              <span>{{ props.row.remark }}</span>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column label="STT" type="index" width="50"></el-table-column>
      <el-table-column label="Mã khách truy cập" width="100" show-overflow-tooltip>
        <template #default="scope">
          <el-link type="primary" :underline="false" @click.prevent="showThis(scope.row.uuid)">
            {{ scope.row.uuid }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column label="Hành vi truy cập" prop="behavior"></el-table-column>
      <el-table-column label="Nội dung truy cập" prop="content" show-overflow-tooltip></el-table-column>
      <el-table-column label="IP" prop="ip"></el-table-column>
      <el-table-column label="Nguồn gốc IP" prop="ipSource" show-overflow-tooltip></el-table-column>
      <el-table-column label="Hệ điều hành" prop="os" show-overflow-tooltip></el-table-column>
      <el-table-column label="Trình duyệt" prop="browser" show-overflow-tooltip></el-table-column>
      <el-table-column label="Thời gian truy cập" width="170">
        <template #default="scope">{{ formatDate(scope.row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="Thao tác" width="120">
        <template #default="scope">
          <el-popconfirm
              title="Xác nhận xóa?"
              icon="Delete"
              icon-color="red"
              @confirm="deleteLogById(scope.row.id)">
            <template #reference>
              <el-button size="small" type="danger" :icon="Delete">Xóa</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <!-- Phân trang -->
    <el-pagination
        v-model:current-page="queryInfo.pageNum"
        v-model:page-size="queryInfo.pageSize"
        :page-sizes="[10, 20, 30, 50]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange">
    </el-pagination>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Search, Delete } from '@element-plus/icons-vue'
import { getVisitLogList, deleteVisitLogById } from "@/api/visitLog"
import DateTimeRangePicker from "@/components/DateTimeRangePicker.vue"
import dayjs from 'dayjs'

// Router
const route = useRoute()

// Dữ liệu tìm kiếm
const queryInfo = reactive({
  uuid: '',
  date: [],
  pageNum: 1,
  pageSize: 10
})

const logList = ref([])
const total = ref(0)

// Hàm định dạng ngày tháng
const formatDate = (date) => {
  if (!date) return ''
  return dayjs(date).format('DD/MM/YYYY HH:mm:ss')
}

// Lấy dữ liệu từ API
const getData = async () => {
  try {
    let query = { ...queryInfo }
    if (query.date && query.date.length === 2) {
      query.date = query.date[0] + ',' + query.date[1]
    }
    const res = await getVisitLogList(query)
    logList.value = res.data.list
    total.value = res.data.total
  } catch (error) {
    ElMessage.error('Lấy dữ liệu thất bại')
  }
}

// Thay đổi số lượng bản ghi mỗi trang
const handleSizeChange = (newSize) => {
  queryInfo.pageSize = newSize
  getData()
}

// Thay đổi trang hiện tại
const handleCurrentChange = (newPage) => {
  queryInfo.pageNum = newPage
  getData()
}

// Xóa log theo ID
const deleteLogById = async (id) => {
  try {
    const res = await deleteVisitLogById(id)
    ElMessage.success(res.msg || 'Xóa thành công')
    getData()
  } catch (error) {
    ElMessage.error('Xóa thất bại')
  }
}

// Tìm kiếm
const search = () => {
  queryInfo.pageNum = 1
  queryInfo.pageSize = 10
  getData()
}

// Hiển thị log theo uuid
const showThis = (uuid) => {
  queryInfo.uuid = uuid
  search()
}

// Set giá trị cho date range
const setDate = (value) => {
  queryInfo.date = value
}

// Lifecycle - khi component được mount
onMounted(() => {
  if (route.query.uuid) {
    queryInfo.uuid = route.query.uuid
  }
  getData()
})
</script>

<style scoped>
.el-form--inline .el-form-item {
  margin-bottom: 0;
}
</style>