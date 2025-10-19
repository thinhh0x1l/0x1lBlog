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
              clearable="clearable"
              @clear="search"
              @change="search"
              style="min-width: 500px"
          >
            <template #prepend>
              <el-select
                  v-model="queryInfo.categoryId"
                  placeholder="Chọn danh mục"
                  clearable="clearable"
                  @change="search"
                  style="width: 160px"
              >
                <el-option
                  v-for="item in categoryList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />

              </el-select>
            </template>
            <template #append>
              <el-button
                  :icon="Search"
                  @click="search"
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
        <el-table-column label="Danh mục" prop="categoryName" width="150"></el-table-column>
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
            <el-popconfirm title="Xác nhận xóa?" @confirm="handleDeleteBlogById(scope.row.id)" confirm-button-text="Xóa" cancel-button-text="Hủy">
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

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowRight, Search, Edit, Delete } from '@element-plus/icons-vue'
import {getDataQuery, deleteBlogById ,getCategoryAndTag, saveBlog} from '@/network/blog.js'
import { formatDate } from '@/util/dateTimeFormatUtils.js'
import  { getCurrentInstance } from 'vue'

const { proxy } = getCurrentInstance()

const router = useRouter()

const queryInfo = reactive({
  query: '',
  categoryId: null,
  pageNum: 1,
  pageSize: 10
})

const search = () =>{
  queryInfo.pageNum = 1;
  queryInfo.pageSize= 10;
  getData()
}

const blogList = ref([])
const categoryList = ref([])
const total = ref(0)

const getData = async () => {
  try {
    const res = await getDataQuery(queryInfo.query, queryInfo.categoryId, queryInfo.pageNum, queryInfo.pageSize)
    console.log(res)
    if (res.code === 200) {
      proxy.$msgSuccess(res.msg)
      blogList.value = res.data.blogs.list
      categoryList.value = res.data.categories
      total.value = res.data.blogs.total
    } else {
      proxy.$msgError(res.msg)
    }
  } catch {
    proxy.$msgError("Yêu cầu thất bại")
  }
}

const blogRecommendChanged = () => {

}

const blogPublishedChanged = () => {

}

const handleSizeChange = (newSize) => {
  queryInfo.pageSize = newSize
  getData()
}

const handleCurrentChange = (newPage) => {
  queryInfo.pageNum = newPage
  getData()
}

const goBlogEditPage = (id) => {
  router.push(`/blogs/edit/${id}`)
}

const handleDeleteBlogById = async (id) => {
  try {
    const res = await deleteBlogById(id)
    console.log(res)
    if (res.code === 200) {
      proxy.$msgSuccess(res.msg)
      await getData ()
    } else {
      proxy.$msgError(res.msg)
    }
  } catch {
    proxy.$msgError("Thao tác thất bại")
  }
}

onMounted(() => {
  getData()
})
</script>

<style scoped>
.el-button {
  margin-right: 10px;
}
</style>