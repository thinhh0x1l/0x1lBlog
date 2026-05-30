<template>
  <div>
    <Breadcrumb parentTitle="Quản lý trang" />

    <el-card>
      <el-form :model="form" :rules="formRules" ref="formRef" label-position="top">
        <el-form-item label="Tiêu đề" prop="title">
          <el-input v-model="form.title" placeholder="Vui lòng nhập tiêu đề"></el-input>
        </el-form-item>

        <el-form-item label="ID bài hát Mp3" prop="musicId">
          <el-input v-model="form.musicId" type="text" placeholder="Vui lòng nhập ID bài hát Mp3 (không bắt buộc)"></el-input>
        </el-form-item>

        <el-form-item label="Nội dung" prop="content">
          <mavon-editor
              class="mavon-editor"
              v-model="form.content"
              language="en"
              placeholder="Nội dung cho About"
              :ishljs="false"
          />
        </el-form-item>
        <el-form-item label="Bật/tắt comment" prop="commentEnabled">
          <el-switch
              v-model="form.commentEnabled"
              active-value="true"
              inactive-value="false"
              active-text="Bật"
              inactive-text="Tắt"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submit">Lưu</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import Breadcrumb from "@/components/Breadcrumb.vue"
import {fAbout, fUpdateAbout} from "@/api/about"
import { ElMessage } from 'element-plus'
import type {About} from "@/types/aboutType";
import type {ApiResponse} from "@/plugins/axios2";

const abouts = ref<About[]>([])
const formRef = ref(null)

const form = reactive({
  title: '',
  musicId: '',
  content: '',
  commentEnabled: ''
})

const formRules = reactive({
  title: [{ required: true, message: 'Vui lòng nhập tiêu đề', trigger: 'change' }],
})

const msgError = (message) => {
  ElMessage.error(message)
}

const msgSuccess = (message) => {
  ElMessage.success(message)
}


const getData = async () => {
    try{
      const res: ApiResponse<About[]> = await fAbout();
      if(res.code/100 === 2){
        abouts.value = res.data
        console.log(abouts.value)
        abouts.value.forEach(item => {
          if(item.nameEn in form){
            form[item.nameEn as keyof typeof form] = item.value
          }
        })
      }
    }catch (e){

    }
}
const submit = async () => {
  const valid = await formRef.value.validate().catch(() => false)

  if (!valid) {
    return msgError('Vui lòng điền đầy đủ thông tin bắt buộc')
  }

  try {
    abouts.value.forEach(item => {
      if(item.nameEn in form){
        item.value = form[item.nameEn as keyof typeof form]
      }
    })
    console.log(abouts.value)
    const res = await fUpdateAbout(abouts.value)

    if (res.code/100 === 2) {
      msgSuccess(res.msg)
    } else {
      msgError(res.msg)
    }
  } catch (e) {
    msgError("Yêu cầu thất bại")
  }
}

onMounted(() => {
  getData()
})
</script>