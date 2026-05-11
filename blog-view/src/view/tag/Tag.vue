<template>
  <div>
    <div class="tag-wrapper">
      Bạn đang chọn ở Tag <TagComponent :list-tag="tags"/> quay trở lại
       <router-link class="backhome" to="/home">{{` trang chủ`}}</router-link>
    </div>
    <BlogList :getBlogList="fetchBlogsByTagId" :page-info="pageInfo" :blog-list="blogList"/>
  </div>
</template>

<script setup lang="ts">
import BlogList from "@/components/blogList/BlogList.vue";
import { getBlogListByTagId} from "@/api/tags";
import TagComponent from '@/components/blogList/Tag.vue'
import type { TagSlug} from '@/types/tagType'
import {computed, ref, watch} from "vue";
import { useRoute} from "vue-router";
import type {BlogInfo} from "@/types/blogType";
import type {TagIdGetBlogsResponse} from "@/types/tagType";
import {updatePageInfo} from "@/util/pageInfo";
import type {ApiResponse} from "@/types/commonType";
const route = useRoute()

const tags = ref<TagSlug[]>([])
const blogList = ref<BlogInfo[]>([])
const tagId = computed<string>(() => <string>route.params.id)

const pageInfo = ref({
  pageNum: 0,
  pageSize: 0,
  totalPages: 0,
  totalElements: 0,
})

const fetchBlogsByTagId = async (pageNum: number) => {
  try {
    const response: ApiResponse<TagIdGetBlogsResponse> =
        await getBlogListByTagId(tagId.value,pageNum, 2)
    if (response.code === 200){
      console.log(response)
      updatePageInfo(pageInfo, response.data.blogInfos)
      blogList.value = response.data.blogInfos.items
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