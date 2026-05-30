<template>
  <div>
    <!-- Breadcrumb điều hướng -->
    <Breadcrumb parentTitle="Quản lý blog" />

    <el-card>
      <el-form :model="form" label-position="top">
        <el-form-item label="Nội dung động" prop="content">
          <mavon-editor
              class="mavon-editor"
              v-model="form.content"
              language="en"
              placeholder="Nội dung moment"
              :ishljs="false"
          />
        </el-form-item>

        <el-form-item label="Số lượt thích" prop="likes" style="width: 50%">
          <el-input readonly disabled
              v-model="form.likes" type="number" />
        </el-form-item>

        <el-form-item label="Thời gian tạo" prop="createTime">
          <el-date-picker
              v-model="form.createTime"
              type="datetime"
              placeholder="Tùy chọn, mặc định là thời điểm hiện tại"
              :editable="false"
          />
        </el-form-item>

        <el-form-item style="text-align: right">
          <el-button type="info" @click="submit(false)">Lưu bản nháp</el-button>
          <el-button type="primary" @click="submit(true)">Đăng bài viết</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import {ref, reactive, onMounted, watch} from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import Breadcrumb from '@/components/Breadcrumb.vue'
import {getMomentById, saveMoment, updateMoment} from "@/api/moment";
import type {ApiResponse} from "@/plugins/axios2";
import type {Moment} from "@/types/momentType";
// import { getMomentById, saveMoment, updateMoment } from '@/api/moment'


// Router và Route
const route = useRoute()
const router = useRouter()

// Form data
const form = reactive<Moment>({
  id: null,
  content: '',
  createTime: null,
  likes: 0,
  published: false
})




const msgSuccess = (message: string) => {
  ElMessage.success(message)
}


const msgError = (message: string) => {
  ElMessage.error(message)
}

const getMoment = async (id: number) => {
  try {
     const res: ApiResponse<Moment>  = await getMomentById(id)

    if (res.code === 200) {
      Object.assign(form, res.data)
    } else {
      msgError(res.msg)
    }
  } catch (error) {
    msgError('Yêu cầu thất bại')
  }
}
watch(() => route.params.id, (id) => {
  if (typeof id === "string") {
    getMoment(parseInt(id))
  }
}, {immediate: true})

const submit = async (published: boolean) => {
  form.published = published
  console.log(form)
  try {
    let res:ApiResponse<Moment>
    if (route.params.id) {
      res = await updateMoment(form)
    } else {
      res = await saveMoment(form)
    }

    if (res.code === 200) {
      msgSuccess(res.msg)
      Object.assign(form,res.data)
      await router.push('/moments')
    } else {
      msgError(res.msg)
    }
  } catch (error) {
    console.log(error)
    msgError('Viết Moment thất bại')
  }
}

// Lifecycle
onMounted(() => {

})
</script>

<style scoped>
/* Bạn có thể thêm style tùy chỉnh tại đây */
</style>