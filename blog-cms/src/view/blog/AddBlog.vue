<template>
  <!-- Thanh điều hướng breadcrumb-->
  <el-breadcrumb :separator-icon="ArrowRight">
    <el-breadcrumb-item :to="{ path : '/home'}">Trang chủ</el-breadcrumb-item>
    <el-breadcrumb-item>Quản lý Blog</el-breadcrumb-item>
    <el-breadcrumb-item>Viết Blog</el-breadcrumb-item>
  </el-breadcrumb>

  <el-card>
    <el-form :model="form" :rules="formRules" ref="formRef">
      <el-form-item prop="title">

      </el-form-item>
    </el-form>
  </el-card>

</template>

<script setup >
import {ArrowRight} from "@element-plus/icons-vue";
import { useEditor, EditorContent } from "@tiptap/vue-3";
import StarterKit from "@tiptap/starter-kit";
import Image from "@tiptap/extension-image";
import { ref, reactive, onMounted, onUnmounted} from "vue";
import { useRouter} from "vue-router";
import { getCategoryAndTag, saveBlog} from "@/network/blog.js"
import { getCurrentInstance } from "vue";

const { proxy } = getCurrentInstance()

const router = useRouter()
const formRef = ref()


// Dữ liệu
const flagList = ['Nguyên tác', 'Chuyển tải', 'Dịch Thuật']
const categoryList = ref([])
const tagList = ref([])

const form = reactive({
  title: '',
  flag: null,
  content: '',
  cate: null,
  tagList: [],
  firstPicture: '',
  words: null,
  description: '',
  shareStatement: false,
  appreciation: false,
  recommend: false,
  commentEnable: false,
  published: false
})

const formRules = {
  title: [{required: true, message: 'Vui lòng nhập tiêu đề', trigger: 'change'}],
  cate: [{required: true, message: 'Vui lòng chọn thể loại', trigger: 'change'}],
  tagList: [{required: true, message: 'Vui lòng chọn tag', trigger: 'change'}],
  firstPicture: [{required: true, message: 'Vui lòng nhập URL ảnh bìa', trigger: 'change'}],
  words: [{required: true, message: 'Vui lòng nhập số từ của Blog', trigger: 'change'}],
  description: [{required: true, message: 'Vui lòng nhập mô tả bài viết', trigger: 'change'}]
}

// TipTap Editor
const editor = useEditor({
  content: form.content,
  extensions: [
      StarterKit, // Cung cấp mọi thứ cần thiết
      Image, // Thêm hỗ trợ hình ảnh
  ],
  // { editor } là kiểu Biến destructuring <Object.editor>
  onUpdate: ({ editor }) => {
    form.content = editor.getHTML() // tự động cập nhật nội dung form
  }
})


</script>