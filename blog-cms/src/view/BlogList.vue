<template>
  <div>
    <!-- Breadcrumb Navigation -->
    <el-breadcrumb :separator-icon="ArrowRight">
      <el-breadcrumb-item :to="{ path: '/home' }">Trang chủ</el-breadcrumb-item>
      <el-breadcrumb-item>Quản lý Blog</el-breadcrumb-item>
      <el-breadcrumb-item>Danh sách Blog</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card>
      <!-- Search -->
      <el-row>
        <el-col :span="8">
          <el-input
              placeholder="Nhập tiêu đề"
              v-model="queryInfo.query"
              :clearable="true"
              @clear=""
          >
            <template #prepend>
              <el-select
                  v-model="queryInfo.typeId"
                  placeholder="Chọn danh mục"
                  :clearable="true"
                  style="width: 160px"
              >
                <el-option label="Danh mục 1" value="1"></el-option>
                <el-option label="Danh mục 2" value="2"></el-option>
                <el-option label="Danh mục 3" value="3"></el-option>
                <el-option label="Danh mục 4" value="4"></el-option>
              </el-select>
            </template>
            <template #append>
              <el-button
                  :icon="Search"
                  @click=""
              >
                Tìm kiếm
              </el-button>
            </template>
          </el-input>
        </el-col>
      </el-row>

      <el-table :data="blogList" border stripe>
        <el-table-column label="STT" type="index" width="55"></el-table-column>
        <el-table-column label="Tiêu đề" prop="title"></el-table-column>
        <el-table-column label="Danh mục" prop="category.name" width="150"></el-table-column>
        <el-table-column label="Đề xuất" width="80">
          <template #default="scope">
            <el-switch
                v-model="scope.row.recommend"
                @change="blogRecommendChanged(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="Trạng thái" width="80">
          <template #default="scope">
            <el-switch
                v-model="scope.row.published"
                @change="blogPublishedChanged(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="Ngày tạo" width="170">
          <template #default="scope">{{ formatDate(scope.row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="Cập nhật" width="170">
          <template #default="scope">{{ formatDate(scope.row.updateTime) }}</template>
        </el-table-column>
        <el-table-column label="Thao tác" width="200">
          <template #default="scope">
            <el-button type="primary" :icon="Edit" size="small" @click="goBlogEditPage(scope.row.id)">Sửa</el-button>
            <el-popconfirm title="Xác nhận xóa?" @confirm="removeBlogById(scope.row.id)" confirm-button-text="Xóa" cancel-button-text="Hủy">
              <template #reference>
                <el-button size="small" type="danger" :icon="Delete">Xóa</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <!-- Pagination -->
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="queryInfo.pageNum"
          :page-sizes="[5, 10, 15, 20]"
          :page-size="queryInfo.pageSize"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          background
      />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowRight, Search, Edit, Delete } from '@element-plus/icons-vue'
import { blogs, deleteBlogById } from '@/network/blog'
import { formatDate } from '@/util/dateTimeFormatUtils.js'

const router = useRouter()

const queryInfo = reactive({
  query: '',
  typeId: null,
  pageNum: 1,
  pageSize: 2
})

const blogList = ref([])
const total = ref(0)

const getBlogList = async () => {
  try {
    const res = await blogs(queryInfo.query, queryInfo.typeId, queryInfo.pageNum, queryInfo.pageSize)
    console.log(res)
    if (res.code === 200) {
      ElMessage.success(res.msg)
      blogList.value = res.data.blogs.list
      total.value = res.data.blogs.total
    } else {
      ElMessage.error(res.msg)
    }
  } catch {
    ElMessage.error("Yêu cầu thất bại")
  }
}

const blogRecommendChanged = () => {

}

const blogPublishedChanged = () => {

}

const handleSizeChange = (newSize) => {
  queryInfo.pageSize = newSize
  getBlogList()
}

const handleCurrentChange = (newPage) => {
  queryInfo.pageNum = newPage
  getBlogList()
}

const goBlogEditPage = (id) => {
  router.push(`/blogs/edit/${id}`)
}

const removeBlogById = async (id) => {
  try {
    const res = await deleteBlogById(id)
    console.log(res)
    if (res.code === 200) {
      ElMessage.success(res.msg)
      await getBlogList()
    } else {
      ElMessage.error(res.msg)
    }
  } catch {
    ElMessage.error("Thao tác thất bại")
  }
}

onMounted(() => {
  getBlogList()
})
</script>

<style scoped>
.el-button {
  margin-right: 10px;
}
</style>