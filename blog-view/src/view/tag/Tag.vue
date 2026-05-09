<template>
  <div>
    <div class="tag-wrapper">
      Bạn đang chọn ở phần <TagComponent :list-tag="tags"/> quay trở lại
       <router-link class="backhome" to="/home">{{` trang chủ`}}</router-link>
    </div>
    <BlogList :getBlogList="fetchBlogsByTagId" :blogList="blogList" :totalPage="totalPage"/>
  </div>
</template>

<script setup lang="ts">
import BlogList from "@/components/blogList/BlogList.vue";
import { getBlogListByTagId} from "@/api/tags";
import TagComponent from '@/components/blogList/Tag.vue'
import type { TagSlug} from '@/types/tagType'
import {computed, ref, watch} from "vue";
import { useRoute} from "vue-router";
import type {ApiResponse} from "@/plugins/axios2";
import type {BlogInfo} from "@/types/blogType";
import type {TagIdGetBlogsResponse} from "@/types/tagType";
const route = useRoute()

const tags = ref<TagSlug[]>([])
const blogList = ref<BlogInfo[]>([])
const totalPage = ref<number>(0)
const tagId = computed<string>(() => <string>route.params.id)

const fetchBlogsByTagId = async (pageNum: number) => {
  try {
    const response: ApiResponse<TagIdGetBlogsResponse> =
        await getBlogListByTagId(tagId.value,pageNum, 2)
    if (response.code === 200){
      blogList.value = response.data.blogInfos.list
      totalPage.value = response.data.blogInfos.pages
      tags.value.push(response.data.queryTag)
    }
  }catch (err) {
  }
}

watch(() => route.path, () => {
  if(route.name === 'tag')
      fetchBlogsByTagId(1)
      tags.value = []
    },
    {immediate: true}
)
</script>

<style scoped>
.tag-wrapper{
  padding: 10px 0;
  background: #c6dcfa;
  position: relative;
  display: flex;
  font-size: 24px;
  justify-content: center;
  margin-bottom: 10px;
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