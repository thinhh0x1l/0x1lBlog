<template>
  <div>

    <el-card>
      <el-row :gutter="10">   <!-- :gutter = khoảng cách giữa các cột-->
        <el-col :span="6">   <!-- :span = độ rộng của cột -->
          <el-button type="primary" @click="addDialogVisible=true" :icon="Plus">
            Thêm Tag
          </el-button>
        </el-col>
      </el-row>

      <el-table :data="tagList" border stripe>
        <el-table-column label="STT" type="index" width="70"/>
        <el-table-column label="Tên">
          <template #default="scope">
              <Tag :tag="scope.row"/>
          </template>
        </el-table-column>
        <el-table-column label="Màu sắc" prop="tagColor">
          <template #default="scope">
            <div class="color-display-cell">
              <div
                  class="color-swatch"
                  :style="{ backgroundColor: scope.row.tagColor }"
              ></div>
              <el-text  >
                {{ scope.row.tagColor }}
              </el-text>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="Thao tác" >
          <template #default="{ row }">

            <el-button type="primary" :icon="Edit" size="small" @click="showEditDialog(row)" >Chỉnh sửa</el-button>
            <el-popconfirm title="Bạn có chắc chắn muốn xóa không?" :icon="Delete" icon-color="red"
                           @confirm="deleteTagByIdVue(row.tagId)"
                           confirm-button-text="Xóa" confirm-button-type="danger" cancel-button-text="Hủy">
              <template #reference>
                <el-button type="danger" size="small"  :icon="Delete">Xóa</el-button>
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


<!--    addTag-->
    <el-dialog
        title="Thêm tag"
        width="50%"
        v-model="addDialogVisible"
        :close-on-click-moda="false"
        @close="addDialogClose">
      <el-form ref="addFormRef" :model="addForm" :rules="formRules" label-width="90">
        <el-form-item label="Tên Tag" prop="tagName">
          <el-input v-model="addForm.tagName" placeholder="Nhập tên Tag"/>
        </el-form-item>
        <el-form-item label="Màu sắc" prop="tagColor" v-if="addForm.tagName">
          <el-color-picker v-model="addForm.tagColor"
                           color-format="hex" style="width: 100px"
                           @active-change="handleColorChangeAdd"
          />
          <Tag :tag="addForm"/>
        </el-form-item>
      </el-form>
      <template #footer >
        <span>
          <el-button @click="addDialogClose">Hủy</el-button>
          <el-button @click="addTag" type="primary">Tạo mới</el-button>
        </span>
      </template>
    </el-dialog>
<!--    editTag-->
    <el-dialog
      title="Chỉnh sửa Tag"
      width="50%"
      v-model="editDialogVisible"
      :close-on-click-modal="false"
      @close="editDialogClose">
      <el-form ref="editFormRef" :model="editForm" :rules="formRules" label-width="90">
        <el-form-item label="Id">
          <el-input v-model="editForm.tagId" readonly/>
        </el-form-item>
        <el-form-item label="Tên Tag" prop="tagName">
          <el-input v-model="editForm.tagName"/>
        </el-form-item>
        <el-form-item label="Màu sắc" prop="tagColor">
          <el-color-picker v-model="editForm.tagColor"
                           @active-change="handleColorChangeEdit"
                           color-format="hex" style="width: 100px"/>
          <Tag :tag="editForm"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <span>
          <el-button @click="editDialogClose">Hủy</el-button>
          <el-button @click="editTag" type="primary">Chỉnh sửa</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from "vue";
import {createTag, deleteTagById, getData, updateTag} from "@/api/tag.js";
import { getCurrentInstance } from "vue";
import {Delete, Edit, Plus} from "@element-plus/icons-vue";
import Tag from '@/components/tag/Tag.vue'
const { proxy } = getCurrentInstance()


const queryInfo = reactive({
  pageNum: 1,
  pageSize: 10,
})

const tagList = ref([])
const total = ref(0)
const addDialogVisible = ref(false)
const editDialogVisible = ref(false)
const addFormRef = ref(null)
const editFormRef = ref(null)
const addForm = ref({
  tagName: '',
  tagColor: '',
})
const  editForm = reactive({})
const formRules = {
  tagName: [{required: true, message: 'Vui lòng nhập tên tag', trigger:'blur'},],
  tagColor: [{required: true, message: 'Vui lòng nhập màu tag', trigger:'blur'},],
}

const fetchData = async () => {
  try{
    const res = await getData(queryInfo)
    if(res.code === 200){
      console.log(res.data)
      total.value = res.data.tags.total
      console.log(total.value)
      tagList.value = res.data.tags.list
    }else
      proxy.$msgError(res.msg)
  }catch (error){

  }
}

const handleSizeChange = (newSize) =>{
  queryInfo.pageSize = newSize
  fetchData()
}
const handleCurrentChange = (newPage) => {
  queryInfo.pageNum = newPage
  fetchData()
}
const addDialogClose = () => {
  addDialogVisible.value = false
  addFormRef.value = null
}
const editDialogClose = () => {
  editDialogVisible.value = false
  editFormRef.value = null
}
const showEditDialog = (row) => {
  Object.assign(editForm ,row)
  editDialogVisible.value = true
}

const addTag = async () => {
  try{
    await addFormRef.value.validate()
    const res = await createTag(addForm.value)

    if(res.code === 200){
      proxy.$msgSuccess(res.msg)
    }else
      proxy.$msgError(res.msg)
  }catch (error){
    proxy.$msgError(error.response.data.msg)
  }
  addDialogClose()
  await fetchData()
}

const editTag = async () => {
  try {
    await editFormRef.value.validate()
    const res = await updateTag(editForm)
    if(res.code === 200){
      proxy.$msgSuccess(res.msg)
    }else
      proxy.$msgError(res.msg)
  }catch (error){
    proxy.$msgError(error.response.data.msg)
  }
  editDialogClose()
  await fetchData()
}

const handleColorChangeAdd = (color) => {
  addForm.value.tagColor=color
};

const handleColorChangeEdit = (color) => {
  editForm.tagColor=color
};
const deleteTagByIdVue = async (id) => {
  try {
    const res = await deleteTagById(id)
    if(res.code === 200){
      proxy.$msgSuccess(res.msg)
    }else
      proxy.$msgError(res.msg)
  }catch (error){
    proxy.$msgError(error.response.data.msg)
  }
  await fetchData()
}

onMounted(() =>{
  fetchData()
})

</script>

<style scoped>
.color-display-cell {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 4px 0;
}

.color-swatch {
  width: 100px;
  height: 20px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
  flex-shrink: 0;
}

.el-button{
  margin-left: 10px;
}

</style>