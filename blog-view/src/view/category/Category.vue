<template>
  <div>
    <div class="category-wrapper">
      {{ 'Bạn đang chọn ở phần '}}<span class="text">{{category.name}}</span> {{ ` quay trở lại` }}
      <router-link class="backhome" to="/home">{{` trang chủ`}}</router-link>
    </div>
    <BlogList :total-page="totalPage" :blog-list="blogList" :get-blog-list="getBlogListByCategoryName"/>
  </div>
</template>

<script setup lang="ts">
import BlogList from "@/components/blogList/BlogList.vue";
import {computed, onMounted, ref, watch} from "vue";
import type {BlogInfo} from "@/types/blogType";
import type {ApiResponse} from "@/plugins/axios2";
import {fGetBlogListByCategoryName} from "@/api/category";
import {useRoute} from "vue-router";
import type {Category, CategoryGetBlogsResponse} from "@/types/categoryType";
import Ribbon from "@/components/blogList/Ribbon.vue";

const route = useRoute()

const category = ref<Category>({
  name: '',
  slug: '',
})
const blogList = ref<BlogInfo[]>([])
const totalPage = ref<number>(0)
const categoryName = computed<string>(() => <string>route.params.name)

const getBlogListByCategoryName = async (pageNum: number) => {
  try {
    const response: ApiResponse<CategoryGetBlogsResponse> =
        await fGetBlogListByCategoryName(categoryName.value,pageNum, 5)
    if (response.code === 200){
      blogList.value = response.data.blogInfos.list
      totalPage.value = response.data.blogInfos.pages
      category.value = response.data.categorySlug
    }
  }catch (err) {
  }
}
watch(() => route.path, () => {
      if(route.name === 'category')
        getBlogListByCategoryName(1)
    },
    {immediate: true}
)
onMounted(() => {
  getBlogListByCategoryName(1);
})

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