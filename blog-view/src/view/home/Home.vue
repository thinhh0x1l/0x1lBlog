<template>
  <div  >
    <BlogList :getBlogList="fetchBlogList" :blogList="blogList" :totalPage="totalPage"/>
  </div>
</template>


<script setup>
import BlogList from "@/components/blogList/BlogList.vue";
import {nextTick, onMounted, ref} from 'vue'
import {getBlogList} from "@/api/home.js";
import {useToast} from "@/plugins/primevueConfig/primePluginVue.js";
import mediumZoom from "medium-zoom";

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

let zoom
const initZoom = () => {
  zoom = mediumZoom(".typo img", {
    margin: 24,
    background: "#000"
  })
}
onMounted( async () => {
  await nextTick()
  await fetchBlogList()
  initZoom()
})
</script>


<style scoped>
</style>

