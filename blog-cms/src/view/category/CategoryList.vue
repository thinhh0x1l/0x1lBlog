<template>
  <div>

    <el-card  >
      <el-row :gutter="10" >
        <el-col :span="6">
          <el-button type="primary" @click="addDialogVisible=true" :icon="Plus" >
            Thêm thể loại
          </el-button>

        </el-col>
      </el-row>
      <el-table :data="categoryList" border stripe style="min-width: 600px">
        <el-table-column label="STT" type="index" width="70"></el-table-column>onMounted
        <el-table-column label="Tên" prop="name" ></el-table-column>
        <el-table-column label="Thao tác">
          <template v-slot="scope">
            <el-button type="primary" :icon="Edit" size="small" @click="showEditDialog(scope.row)">
              Chỉnh sửa
            </el-button>
            <el-popconfirm title="Bạn có chắc chắn muốn xóa không?" :icon="Delete" icon-color="red"
                           @confirm="deleteCategoryByIdVue(scope.row.id)"
                           confirm-button-text="Xóa" confirm-button-type="danger" cancel-button-text="Hủy">
              <template #reference  >
                <el-button size="small" type="danger"  :icon="Delete">Xóa</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
<!--      Dialog thêm danh mục-->
      <el-dialog title="Thêm thể loai" width="50%" v-model="addDialogVisible"
        :close-on-click-modal="false" @close="addDialogClose">
        <el-form :model="addForm" :rules="formRules" ref="addFormRef"  label-width="90">
          <el-form-item label="Tên thể loai" prop="name">
            <el-input v-model="addForm.name" placeholder="Nhập tên thể loại"/>
          </el-form-item>
          <el-button @click="addDialogClose">Hủy</el-button>
          <el-button type="primary" @click="addCategory(addForm.name)">Tạo</el-button>
        </el-form>
      </el-dialog>
      <!--      Dialog thêm danh mục-->
      <el-dialog title="Chỉnh sửa thể loại" width="50%" v-model="editDialogVisible"
                 :close-on-click-modal="false" @close="editDialogClose">
        <el-form :model="editForm" :rules="formRules" ref="editFormRef" label-width="90">
          <el-form-item label="Id" prop="id">
             <el-input readonly v-model="editForm.id" />
          </el-form-item>
          <el-form-item label="Tên thể loai" prop="name">
            <el-input v-model="editForm.name" placeholder="Nhập tên thể loại"/>
          </el-form-item>
          <el-button @click="editDialogClose">Hủy</el-button>
          <el-button type="primary" @click="editCategory">Chỉnh sửa</el-button>
        </el-form>
      </el-dialog>

  </div>
</template>

<script setup>
import { ref , reactive, onMounted} from "vue";
import {getDataQuery, deleteCategoryById, createCategory, updateCategoryById} from "@/api/category.js";
import {Edit, Delete, Plus} from "@element-plus/icons-vue";
import { getCurrentInstance } from "vue";

const { proxy } = getCurrentInstance()

const categoryList = ref([])
const total = ref(0)

const addDialogVisible = ref(false)
const editDialogVisible = ref(false)

const addForm = reactive({name: ''})
const editForm = reactive({})
const formRules = reactive({
  name: [{required: true, message: 'Vui lòng nhập tên thể loại', trigger: 'blur'}]
})
const queryInfo = reactive({
  pageNum: 1,
  pageSize: 10
})

const addFormRef = ref(null)
const editFormRef = ref(null)

const getData = async () =>{
  try{
    const res = await getDataQuery(queryInfo)
    console.log(res)
    if(res.code === 200){
      console.log(res.msg)
      categoryList.value = res.data.list
      total.value = res.data.total
    }else
      proxy.$msgError(res.msg)
  }catch (e){
    proxy.$msgError("Yêu cầu thất bại!!!")
  }
}

const showEditDialog = (row) => {
  console.log(row)
  Object.assign(editForm,row)
  editDialogVisible.value = true
}

const addCategory = async (name) => {
  try{
    await addFormRef.value.validate()
    const res = await createCategory(name)
    if(res.code === 200){
      addDialogClose()
      await getData()
      proxy.$msgSuccess(res.msg)
    }else
      proxy.$msgError(res.msg)
  }catch (error){
    console.log(error.response.data)
    proxy.$msgError(error.response.data.msg)
  }
}

const editCategory = async () => {
  try{
    await editFormRef.value.validate()
    const res = await updateCategoryById(editForm.id, editForm.name)
    console.log(res)
    if(res.code === 200){
      editDialogClose()
      await getData()
      proxy.$msgSuccess(res.msg)
    }else
      proxy.$msgError(res.msg)
  }catch (error){
    console.log(error.response.data.msg)
    proxy.$msgError(error.response.data.msg)
  }
}

const deleteCategoryByIdVue = async (id) => {
  try {
    const res = await deleteCategoryById(id)
    if(res.code === 200){
      await getData()
      proxy.$msgSuccess(res.msg)
    }

    else
      proxy.$msgError(res.msg)
  }catch (error){
    console.log(error.response.data)
    proxy.$msgError(error.response.data.msg)
  }
}
const addDialogClose = () => {
  addDialogVisible.value = false
  addFormRef.value.resetFields()
}

const editDialogClose = () => {
  editDialogVisible.value = false;
  editFormRef.value.resetFields()
}

onMounted(()=>{
  getData()
})
</script>


<style scoped>
.el-button{
  margin-left: 10px;
}

</style>