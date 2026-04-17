<template>
  <div>
    <Breadcrumb parent-title="Quản lý bình luận"/>

    <el-card>
      <el-row>
        <el-col :span="6">
          <el-select
            v-model="pageId"
            placeholder="Chọn trang"
            filterable
            clearable
            @change="search"
          >
            <el-option
              v-for="item in blogList"
              :key="item.id"
              :label="item.title"
              :value="item.id"/>
          </el-select>
        </el-col>
      </el-row>

      <el-table
        :data="commentList"
        row-key="id"
        :tree-props="{ children : 'replyComments'}"
        border
        stripe
      >
        <el-table-column label="STT" type="index" width="60"/>
        <el-table-column label="Tên" prop="nickname">
          <template #default="scope">
            {{scope.row.nickname}}
            <el-tag
              v-if="scope.row.adminComment"
              size="small"
              effect="dark"
              style="margin-left: 5px">
              Tôi
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Avatar" width="80">
          <template #default="scope">
            <el-avatar
              shape="square"
              :size="60"
              fit="contain"
              :src="getAvatarUrl(scope.row.avatar)"/>
          </template>
        </el-table-column>
        <el-table-column label="Email" prop="email"/>
        <el-table-column label="IP" prop="ip" width="130"/>
        <el-table-column label="Nội dung" prop="content"/>
        <el-table-column label="Trang">
          <template #default="scope">
            <el-link
              type="success"
              :href="'/blog/'+scope.row.blog.id"
              target="_blank"
              v-if="scope.row.page === 0"
            >
              {{ scope.row.blog?.title }}
            </el-link>
            <el-link
              type="success"
              :href="'/about'"
              target="_blank"
              v-if="scope.row.page === 1"
            >
              Về tôi
            </el-link>
          </template>
        </el-table-column>
        <el-table-column label="Thời gian" width="170">
          <template #default="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="Công khai" width="80">
          <template #default="scope">
            <el-switch
              v-model="scope.row.published"
              @change="commentPublishedChanged(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="Thông báo email" width="80">
          <template #default="scope">
            <el-switch
                v-model="scope.row.notice"
                @change="commentNoticeChanged(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="Thao tác" width="190">
          <template #default="scope">
            <el-button
                type="primary"
                :icon="Edit"
                size="small"
                @click="showEditDialog(scope.row)"
            >
              Sửa
            </el-button>
            <el-popconfirm
                title="Bạn có chắc chắn muốn xóa không?"
                :icon="Delete"
                icon-color="red"
                confirm-button-text="Xóa" confirm-button-type="danger" cancel-button-text="Hủy"
                @confirm="deleteComment(scope.row.id)"
            >
              <template #reference>
                <el-button
                    size="small"
                    type="danger"
                    :icon="Delete"
                >
                  Xóa
                </el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="queryInfo.pageNum"
          :page-size="queryInfo.pageSize"
          :page-sizes="[5, 10, 20, 50]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          background/>
    </el-card>

    <el-dialog
      title="Chỉnh sửa Comment"
      width="50%"
      v-model="editDialogVisible"
      :close-on-click-modal="false"
      @close="editDialogClose"
    >
      <el-form ref="editFormRel" :model="editForm" :rules="formRules" label-width="80px">
        <el-form-item label="Nickname" prop="nickname">
          <el-input v-model="editForm.nickname"/>
        </el-form-item>
        <el-form-item label="Email" prop="email">
          <el-input v-model="editForm.email"/>
        </el-form-item>
        <el-form-item label="IP" prop="ip">
          <el-input v-model="editForm.ip"/>
        </el-form-item>
        <el-form-item label="Nội dung" prop="content">
          <el-input v-model="editForm.content" type="textarea" maxlength="255" show-word-limit/>
        </el-form-item>
      </el-form>
      <template #footer>
        <span >
          <el-button @click="editDialogClose">Huỷ</el-button>
          <el-button @click="submitEditComment" type="primary">Chỉnh sửa</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import Breadcrumb from "@/components/Breadcrumb.vue";
import { getCurrentInstance } from "vue";
import { ref , reactive , onMounted } from "vue";
import { formatDate } from '@/util/dateTimeFormatUtils.js'
import {
  deleteCommentById,
  getBlogList,
  getCommentListByQuery, updateComment,
  updateCommentNoticeById,
  updateCommentPublishedById
} from "@/api/comment.js";
import {Delete, Edit} from "@element-plus/icons-vue";
import {checkEmail} from "@/common/reg.js";

const { proxy } = getCurrentInstance()
const pageId = ref(null)
const queryInfo = reactive({
  page: 0,
  blogId: null,
  pageNum: 1,
  pageSize: 10
})
const total = ref(0)
const commentList = ref([])
const blogList = ref([])
const editDialogVisible = ref(false);
const editFormRel = ref(null)
const editForm = reactive({})
const formRules = {
  content: [{required: true, message: 'Vui lòng nhập bình luận', trigger:'blur'}],
  ip:[{required: true, message: 'Vui lòng nhập địa chỉ IP', trigger:'blur'}],
  nickname:[{required: true, message: 'Vui lòng nhập Nickname', trigger:'blur'}],
  email:[{validator: checkEmail,  trigger:'blur'}],
}

const submitEditComment = async () => {
  await editFormRel.value.validate()
  try{
    const res = await updateComment(editForm)
    if(res.code === 200){
      editDialogClose()
      await getCommentList()
      await getBlogListData()
      proxy.$msgSuccess(res.msg)
    }

  }catch (error){
    proxy.$msgError(error.response.data.msg)
  }
}

const handleSizeChange = (newSize) =>{
  queryInfo.pageSize = newSize
  getCommentList()
}
const handleCurrentChange = (newPage) => {
  queryInfo.pageNum = newPage
  getCommentList()
}
const getCommentList = async () => {
  try {
    const res = await getCommentListByQuery(queryInfo)
    if (res.code === 200) {
      console.log(res.msg)
      console.log( res.data)
      commentList.value = res.data.list
      total.value = res.data.total
    } else {
      proxy.$msgError(res.msg)
    }
  } catch (error) {
    proxy.$msgError(error.response.data.msg)
  }
}
const getBlogListData = async () =>{
  try{
    const res = await getBlogList()
    if(res.code === 200){
      console.log(res.msg)
      blogList.value = [{id: -1, title: 'Về tôi'},...res.data]
    }else
      proxy.$msgError(res.msg)
  }catch (error) {
    proxy.$msgError(error.response.data.msg)
  }
}
const search = () => {
  if (pageId.value === -1) {
    queryInfo.page = 1
    queryInfo.blogId = null
  } else {
    queryInfo.page = 0
    queryInfo.blogId = pageId.value
  }
  queryInfo.pageNum = 1
  queryInfo.pageSize = 10
  getCommentList()
}

const showEditDialog = (row) => {
  Object.assign(editForm ,row)
  editDialogVisible.value = true
}

const editDialogClose = () => {
  editFormRel.value.resetFields()
  editDialogVisible.value = false;
}
const commentPublishedChanged = async (row) => {
  try{
    const res = await updateCommentPublishedById(row.id, row.published)
    await getCommentList()
    proxy.$msgSuccess(res.msg)
  }catch (error){
    proxy.$msgError(error.response.data.msg);
  }
}
const commentNoticeChanged = async (row) => {
  try{
    const res = await updateCommentNoticeById(row.id, row.notice)
    await getCommentList()
    proxy.$msgSuccess(res.msg)
  }catch (error){
    proxy.$msgError(error.response.data.msg);
  }
}
const deleteComment = async (id) => {
  try{
    const res = await deleteCommentById(id)
    await getCommentList()
    proxy.$msgSuccess(res.msg)
  }catch (error){
    proxy.$msgError(error.response.data.msg);
  }
}

const getAvatarUrl = (avatar) => {
  return `/img/comment-avatar/${avatar}`
}
onMounted(async ()=> {
  await getCommentList()
  await getBlogListData()
})
</script>

<style scoped>
.el-button{
  margin-left: 10px;
}

</style>