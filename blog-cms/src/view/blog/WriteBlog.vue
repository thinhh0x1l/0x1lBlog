<template>
  <div>
    <!--Thanh điều hướng breadcrumb-->
    <Breadcrumb parent-title="Quản lý Blog"></Breadcrumb>

    <el-card>
      <el-form :model="form" :rules="formRules" ref="formRef" label-position="top">
        <el-form-item prop="title">
          <el-input v-model="form.title" placeholder="Vui lòng nhập tiêu đề"
                    style="min-width: 500px">
            <template #prepend>
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
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="content"
                    >
          <!-- TipTap Editor - CSS tối giản -->
          <div style="border: 1px solid #DCDFE6; border-radius: 4px; background: white;">
            <!-- Thanh công cụ đơn giản -->
            <div v-if="editor" style="padding: 8px; border-bottom: 1px solid #DCDFE6; background: #F5F7FA; display: flex; gap: 4px;">
              <el-button
                  size="small"
                  :type="editor.isActive('bold') ? 'primary' : ''"
                  @click="editor.chain().focus().toggleBold().run()"
              >
                B
              </el-button>
              <el-button
                  size="small"
                  :type="editor.isActive('italic') ? 'primary' : ''"
                  @click="editor.chain().focus().toggleItalic().run()"
              >
                I
              </el-button>
              <el-button
                  size="small"
                  :type="editor.isActive('heading', { level: 2 }) ? 'primary' : ''"
                  @click="editor.chain().focus().toggleHeading({ level: 2 }).run()"
              >
                H2
              </el-button>
              <el-button
                  size="small"
                  @click="addImage"
              >
                Hình ảnh
              </el-button>
            </div>

            <!-- Nội dung Editor -->
            <editor-content :editor="editor" style="padding: 16px; min-height: 400px; outline: none; min-width: 500px" />
          </div>
        </el-form-item>

        <!-- Các mục form khác giữ nguyên -->
        <el-form-item label="Danh mục" prop="cate">
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

        <el-form-item label="Thẻ" prop="tagList">
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

        <el-form-item label="Ảnh bìa" prop="firstPicture">
          <el-input
              v-model="form.firstPicture"
              placeholder="Vui lòng nhập URL ảnh bìa"
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

        <el-form-item label="Thời gian đọc (tùy chọn)" prop="readTime">
<!--          v-model = :value(truyền dynamic data từ js) + @input(lắng nghe sự kiện input thay đổi)-->
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

        <el-form-item label="Mô tả bài viết" prop="description">
          <el-input
              v-model="form.description"
              type="textarea"
              placeholder="Vui lòng nhập mô tả Blog..."
              :maxlength="255"
              show-word-limit
          />
        </el-form-item>

        <el-form-item>
          <el-switch v-model="form.shareStatement" active-text="Bản quyền " />
          <el-switch v-model="form.appreciation" active-text="Ủng hộ " />
          <el-switch v-model="form.recommend" active-text="Đề xuất " />
          <el-switch v-model="form.commentEnabled" active-text="Bình luận " />
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
import { useEditor, EditorContent } from '@tiptap/vue-3'
import StarterKit from '@tiptap/starter-kit'
import Image from '@tiptap/extension-image'
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { useRouter , useRoute} from 'vue-router'
import { getCategoryAndTag, saveBlog , getBlogById , updateBlog} from '@/network/blog'
import { ArrowRight } from "@element-plus/icons-vue";
import { getCurrentInstance} from "vue";
import {watch} from "vue";
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
  flag: null,
  content: '',
  cate: null,
  tagList: [],
  readTime: null,
  views: 0,
  firstPicture: '',
  words: null,
  description: '',
  shareStatement: false,
  appreciation: false,
  recommend: false,
  commentEnabled: false,
  published: false
})
watch(() => form.content, (newContent) => {
  if (editor.value && newContent !== editor.value.getHTML()) {
    console.log('🔄 Updating editor with new content:', newContent)
    editor.value.commands.setContent(newContent, false)
  }
})


const formRules = {
  title: [{ required: true, message: 'Vui lòng nhập tiêu đề', trigger: 'change' }],
  cate: [{ required: true, message: 'Vui lòng chọn danh mục', trigger: 'change' }],
  tagList: [{ required: true, message: 'Vui lòng chọn thẻ', trigger: 'change' }],
  firstPicture: [{ required: true, message: 'Vui lòng nhập URL ảnh bìa', trigger: 'change' }],
  words: [{ required: true, message: 'Vui lòng nhập số từ bài viết', trigger: 'change' }],
  description: [{ required: true, message: 'Vui lòng nhập mô tả bài viết', trigger: 'change' }],
}

// TipTap Editor - Thiết lập đơn giản
const editor = useEditor({
  content: form.content,
  extensions: [
    StarterKit, // Cung cấp mọi thứ cần thiết
    Image,       // Thêm hỗ trợ hình ảnh
  ],
  onUpdate: ({ editor }) => {
    form.content = editor.getHTML() // Tự động cập nhật nội dung form
    form.words = editor.getText().trim().length
  },
})


const addImage = () => {
  const url = window.prompt('Vui lòng nhập URL hình ảnh: ')
  if (url) {
    editor.value.chain().focus().setImage({ src: url }).run()
  }
}

const getBlog = async (id) => {
  try{
    const res = await getBlogById(id);
    if (res.code === 200) {
      computeCategoryAndTag(res.data)
      Object.assign(form,res.data)
    }
  }catch (e){

  }
}

const computeCategoryAndTag = (blog) => {
  blog.cate = blog.category.id;
  blog.tagList = []
  blog.tags.forEach( tag => blog.tagList.push(tag.id) )
}

const getData = async () => {
  try {
    const res = await getCategoryAndTag()
    if (res.code === 200) {
      proxy.$msgSuccess(res.msg)
      categoryList.value = res.data.categories
      tagList.value = res.data.tags
    } else {
      proxy.$msgError(res.msg)
    }
  } catch (error) {
    proxy.$msgError('Yêu cầu thất bại')
  }
}

const submit = async (published) => {
  try {
    await formRef.value.validate()

    form.published = published

    console.log('Dữ liệu gửi:', form)
    let res
    if(route.params.id){
      res = await updateBlog(form);
    }else{
      res = await saveBlog(form)
    }
    if (res.code === 200) {
      await router.push('/blogs')
      console.log(res.data)
      proxy.$msgSuccess(res.msg)
    } else
      proxy.$msgError(res.msg)

  } catch (error) {
    if (error?.errors) {
      proxy.$msgError('Vui lòng điền các mục biểu mẫu bắt buộc')
    } else {
      proxy.$msgError('Yêu cầu thất bại')
    }
  }
}


onMounted(() => {
  getData()
  if(route.params.id)
    getBlog(route.params.id)

})

onUnmounted(() => {
  if (editor.value) {
    editor.value.destroy()
  }
})
</script>

<style>

.ProseMirror {
  outline: none;
  min-height: 400px;
  padding: 16px;
  line-height: 1.6;
}

.ProseMirror h1 { font-size: 2em; margin: 0.67em 0; }
.ProseMirror h2 { font-size: 1.5em; margin: 0.83em 0; }
.ProseMirror h3 { font-size: 1.17em; margin: 1em 0; }
.ProseMirror p { margin: 1em 0; }
.ProseMirror img { max-width: 100%; }
</style>