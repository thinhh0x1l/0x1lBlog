<template>
  <div>
    <!-- Breadcrumb điều hướng -->
    <Breadcrumb parentTitle="Quản lý blog"/>

    <el-card>
      <el-table :data="momentList" border stripe>
        <el-table-column label="STT" type="index" width="50"></el-table-column>
        <el-table-column label="Nội dung" prop="content"></el-table-column>
        <el-table-column label="Trạng thái" width="80">
          <template #default="scope">
            <el-switch v-model="scope.row.published"
                       @change="momentPublishedChanged(scope.row)"/>
          </template>
        </el-table-column>
        <el-table-column label="Lượt thích" prop="likes" width="80"></el-table-column>
        <el-table-column label="Ngày tạo" width="170">
          <template #default="scope">{{ formatDate(scope.row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="Thao tác" width="200">
          <template #default="scope">
            <el-button type="primary" :icon="Edit" size="small" @click="goMomentEditPage(scope.row.id)">Sửa</el-button>
            <el-popconfirm title="Xác nhận xóa?" :icon="Delete" icon-color="red" @confirm="deleteMomentById(scope.row.id)">
              <el-button size="small" type="danger" :icon="Delete" slot="reference">Xóa</el-button>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <!-- Phân trang -->
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          v-model:current-page="queryInfo.pageNum"
          :page-sizes="[5, 10, 15, 20]"
          :page-size="queryInfo.pageSize"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          background>
      </el-pagination>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Edit, Delete } from '@element-plus/icons-vue'
import Breadcrumb from "@/components/Breadcrumb.vue"
import { getMomentListByQuery, updatePublished } from "@/api/moment"
import {formatDate} from "@/util/dateTimeFormatUtils.js";
import type {Moment} from "@/types/momentType";

const router = useRouter()

const queryInfo = reactive({
  pageNum: 1,
  pageSize: 5
})
const momentList = ref<Moment[]>([])
const total = ref(0)


const msgSuccess = (msg) => {
  ElMessage.success(msg)
}


const msgError = (msg) => {
  ElMessage.error(msg)
}

const getMomentList = () => {
  getMomentListByQuery(queryInfo).then(res => {
    console.log(res)
    if (res.code === 200) {
      momentList.value = res.data.list
      total.value = res.data.total
      console.log(res.data)
      msgSuccess(res.msg)
    } else {
      msgError(res.msg)
    }
  }).catch(() => {
    msgError('Yêu cầu thất bại')
  })
}

const getData = () => {
  getMomentList()
}

const handleSizeChange = (newSize: number) => {
  queryInfo.pageSize = newSize
  getData()
}

const handleCurrentChange = (newPage: number) => {
  queryInfo.pageNum = newPage
  getData()
}

const momentPublishedChanged = (row: { id: number; published: boolean }) => {
  updatePublished(row.id, row.published).then(res => {
    if (res.code === 200) {
      msgSuccess(res.msg)
    } else {
      msgError(res.msg)
    }
  }).catch(() => {
    msgError("Yêu cầu thất bại")
  })
}

const goMomentEditPage = (id) => {
  router.push(`/moment/edit/${id}`)
}

// Xóa bài viết
const deleteMomentById = (id) => {
  // TODO: Gọi API xóa
  console.log('Xóa id:', id)
}

onMounted(() => {
  getMomentList()
})
</script>

<style scoped>
.el-button + span {
  margin-left: 10px;
}
</style>