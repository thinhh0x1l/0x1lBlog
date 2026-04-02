<template>
  <div  >
    <BlogList :getBlogList="fetchBlogList" :blogList="blogList" :totalPage="totalPage"/>
  </div>
</template>


<script setup>
import BlogList from "@/components/blogList/BlogList.vue";
import {onMounted, ref} from 'vue'
import {getBlogList} from "@/api/home.js";
import {useToast} from "@/plugins/primevueConfig/primePluginVue.js";

const toast = useToast()

const blogList = ref([])
const totalPage = ref(0)

const fetchBlogList = async (pageNum) => {
  try {
    const res = await getBlogList(pageNum);
    if(res.code === 200){
      toast.success(res.msg)
      blogList.value = res.data.list
      totalPage.value = res.data.totalPage
    }
  }catch (error){
    console.error(error.response.data)
    toast.error(error.response.data.message)
  }
}
onMounted(() => {
  fetchBlogList()
})
</script>


<style scoped>
</style>

