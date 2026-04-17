<template>
  <div>
    <!--Thanh điều hướng breadcrumb-->
    <Breadcrumb parent-title="Quản lý Blog"></Breadcrumb>

    <el-card>
      <el-form :model="form" :rules="formRules" ref="formRef" label-position="top">
        <el-form-item prop="title">
          <el-input v-model="form.title" placeholder="Vui lòng nhập tiêu đề"
                    style="min-width: 500px">
<!--            <template #prepend>
              <el-select
                  v-model="form.flag"
                  placeholder="Vui lòng chọn thể loại"
                  :allow-create="true"
                  :filterable="true"
                  style="width: 160px"
              >
                <el-option
                    :label="item"
                    :value="item"
                    v-for="(item,index) in flagList"
                    :key="index"
                />
              </el-select>
            </template>-->
          </el-input>
        </el-form-item>

        <!-- Editor cho mô tả -->
        <el-form-item label="Mô tả Blog" prop="description">
          <mavon-editor
              class="mavon-editor"
              v-model="form.description"
              language="en"
              placeholder="Mô tả Blog...."
              :ishljs="false"
          />
        </el-form-item>

        <!-- Editor cho nội dung chính -->
        <el-form-item label="Nội dung Blog"  prop="content" style="width: 100%">
          <mavon-editor
              class="mavon-editor"
              v-model="form.content"
              language="en"
              placeholder="Nội dụng Blog.... "
              :ishljs="false"
          />
        </el-form-item>

        <!-- Các mục form khác giữ nguyên -->
        <el-form-item label="Thể loại (có thể thêm thể loại mới)" prop="cate" >
          <el-select
              v-model="form.cate"
              placeholder="Vui lòng chọn thể loại"
              :allow-create="true"
              :filterable="true"
              style="width: 50%;"
          >
            <el-option
                :label="item.name"
                :value="item.id"
                v-for="item in categoryList"
                :key="item.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="Tag (có thể thêm tag mới)" prop="tagList">
          <el-select
              v-model="form.tagList"
              placeholder="Vui lòng chọn Tag"
              :allow-create="true"
              :filterable="true"
              :multiple="true"
              style="width: 50%;"
          >
            <el-option
                :label="item.name"
                :value="item.id"
                v-for="item in tagList"
                :key="item.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="Id Mp3" prop="musicId">
          <el-input
              v-model="form.musicId"
              placeholder="Thêm nhạc"
              type="text"
              style="width: 50%;"
          />
        </el-form-item>
        <el-form-item label="Số từ" prop="words">
          <el-input
              v-model="form.words"
              placeholder="Vui lòng nhập số từ của Blog"
              type="number"
              style="width: 50%;"
          />
        </el-form-item>

        <el-form-item label="Thời gian đọc (tùy chọn)" >
          <el-input
              v-model="form.readTime"
              placeholder="Vui lòng nhập thời gian đọc. Mặc định Math.round(words/200)"
              type="number"
              style="width: 50%"
          />
        </el-form-item>

        <el-form-item label="Lượt xem (tùy chọn)" prop="views">
          <el-input
              v-model="form.views"
              placeholder="Vui lòng nhập số lượt xem. Mặc đinh là 0"
              type="number"
              style="width: 50%"
          />
        </el-form-item>

        <el-form-item>
<!--          <el-switch v-model="form.shareStatement" active-text="Bản quyền " />-->
          <el-switch v-model="form.appreciation" active-text="Ủng hộ " />
          <el-switch v-model="form.recommend" active-text="Đề xuất " />
          <el-switch v-model="form.commentEnabled" active-text="Bình luận " />
        </el-form-item>
        <el-form-item>
          <el-switch v-model="form.top" active-text="Đã ghim"/>
        </el-form-item>
        <el-form-item style="text-align: right;">
          <el-button type="info" @click="submit(false)">Lưu bản nháp</el-button>
          <el-button type="primary" @click="submit(true)">Đăng bài</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>

import {ref, reactive, onMounted} from 'vue'
import { useRouter , useRoute} from 'vue-router'
import { getCategoryAndTag, saveBlog , getBlogById , updateBlog} from '@/api/blog'
import { getCurrentInstance } from "vue";
import Breadcrumb from "@/components/Breadcrumb.vue";

const { proxy } = getCurrentInstance()
const route = useRoute()
const router = useRouter()
const formRef = ref()

// Dữ liệu
const flagList = ['Nguyên tác', 'Chuyển tải', 'Dịch thuật']
const categoryList = ref([])
const tagList = ref([])

const form = reactive({
  title: '',
  // flag: null,
  content: '',
  description: '',
  cate: null,
  tagList: [],
  readTime: null,
  views: 0,
  words: null,
  // shareStatement: false,
  appreciation: false,
  top: false,
  recommend: false,
  commentEnabled: false,
  published: false,
  musicId: ''
})

const formRules = {
  title: [{ required: true, message: 'Vui lòng nhập tiêu đề', trigger: 'change' }],
  cate: [{ required: true, message: 'Vui lòng chọn danh mục', trigger: 'change' }],
  tagList: [{ required: true, message: 'Vui lòng chọn thẻ', trigger: 'change' }],
  words: [{ required: true, message: 'Vui lòng nhập số từ bài viết', trigger: 'change' }],
  description: [{ required: true, message: 'Vui lòng nhập mô tả bài viết', trigger: 'change' }],
}

const getBlog = async (id) => {
  try {
    const res = await getBlogById(id);
    if (res.code === 200) {
      computeCategoryAndTag(res.data)
      Object.assign(form, res.data)
      console.log(res.data)
    }
  } catch (e) {
    console.error('Lỗi khi lấy blog:', e)
  }
}

const computeCategoryAndTag = (blog) => {
  blog.cate = blog.category.id;
  blog.tagList = []
  blog.tags.forEach(tag => blog.tagList.push(tag.id))
}

const getData = async () => {
  try {
    const res = await getCategoryAndTag()
    if (res.code === 200) {
      console.log(res.msg)
      categoryList.value = res.data.categories
      tagList.value = res.data.tags
    } else {
      proxy.$msgError(res.msg)
    }
  } catch (error) {
    console.error(error)
    proxy.$msgError(error.response.data.message)
  }
}

const submit = async (published) => {
  try {
    await formRef.value.validate()

    form.published = published

    console.log('Dữ liệu gửi:', form)
    let res
    if (route.params.id) {
      res = await updateBlog(form);
    } else {
      res = await saveBlog(form)
    }
    if (res.code === 200) {
      await router.push('/blogs')
      console.log(res.data)
      proxy.$msgSuccess(res.msg)
    } else
      proxy.$msgError(res.msg)

  } catch (error) {
    console.error(error)
    proxy.$msgError(error.response.data.msg)
  }
}

onMounted(() => {
  getData()
  if (route.params.id)
    getBlog(route.params.id)
})

</script>

<style>
.mavon-editor {
  width: 100%;
  height: 100%;
}
</style>