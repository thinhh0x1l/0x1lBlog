<template>
  <div>
    <div class="category-wrapper">
      {{ 'Bạn đang chọn ở Phân loại '}}<span class="text">{{category.name}}</span> {{ ` quay trở lại` }}
      <router-link class="backhome" to="/home">{{` trang chủ`}}</router-link>
    </div>
    <BlogList :page-info="pageInfo" :blog-list="blogList" :get-blog-list="getBlogListByCategoryName"/>
  </div>
</template>

<script setup lang="ts">
import BlogList from "@/components/blogList/BlogList.vue";
import {computed, onMounted, ref, watch} from "vue";
import type {BlogInfo} from "@/types/blogType";
import {fGetBlogListByCategoryName} from "@/api/category";
import {useRoute} from "vue-router";
import type {Category, CategoryGetBlogsResponse} from "@/types/categoryType";
import {updatePageInfo} from "@/util/pageInfo";
import type {ApiResponse} from "@/types/commonType";

const route = useRoute()

const category = ref<Category>({
  name: '',
  slug: '',
})

const pageInfo = ref({
  pageNum: 0,
  pageSize: 0,
  totalPages: 0,
  totalElements: 0,
})

const blogList = ref<BlogInfo[]>([])
const categoryName = computed<string>(() => <string>route.params.name)

const getBlogListByCategoryName = async (pageNum: number) => {
  try {
    const response: ApiResponse<CategoryGetBlogsResponse> =
        await fGetBlogListByCategoryName(categoryName.value,pageNum, 5)
    if (response.code === 200){
      updatePageInfo(pageInfo, response.data.blogInfos)
      blogList.value = response.data.blogInfos.items
      category.value = response.data.categorySlug
    }
  }catch (err) {
  }
}
watch(() => route.params.name, () => {
      if(route.name === 'category')
        getBlogListByCategoryName(1)
    },
    {immediate: true}
)


</script>

<style>
.category-wrapper{
  padding: 10px 0;
  background: #c6dcfa;
  position: relative;
  display: flex;
  font-size: 24px;
  justify-content: center;
  margin-bottom: 10px;
}
.category-wrapper .text{
  margin:0 10px;
  background: #d3d3d3;
  font-size: 26px;
}
.backhome{
  margin-left: 10px;
  color: #00a7e0;
  text-decoration: none;
}
.backhome:hover{
  color: #009bd1;
  font-weight: 400;
  background: #b4d1fa;
}
</style>