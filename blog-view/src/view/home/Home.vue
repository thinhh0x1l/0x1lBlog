<template>
  <div  >
    <BlogList :getBlogList="fetchBlogList" :page-info="pageInfo" :blog-list="blogList"/>
  </div>
</template>


<script setup >
import BlogList from "@/components/blogList/BlogList.vue";
import {nextTick, onActivated, onBeforeUpdate, onMounted, ref} from 'vue'
import {getBlogList} from "@/api/home.js";
import {useToast} from "@/plugins/primevueConfig/primePluginVue.js";
import mediumZoom from "medium-zoom";
import {useAppStore} from "@/store/index.ts";
import {updatePageInfo} from "@/util/pageInfo.js";

const store = useAppStore();

const pageInfo = ref({
  pageNum: 0,
  pageSize: 0,
  totalPages: 0,
  totalElements: 0,
})
const toast = useToast()
const blogList = ref([])

const fetchBlogList = async (pageNum) => {
  try {
    console.log(12323)
    const res = await getBlogList(pageNum);
    if(res.code === 200){
      toast.success(res.msg)
      updatePageInfo(pageInfo, res.data)
      blogList.value = res.data.items

    }
  }catch (error){
    console.error(error.response.data)
    toast.error(error.response.data.message)
  }
}
onActivated(() => {

})
let zoom
const initZoom = () => {
  zoom = mediumZoom(".typo img", {
    margin: 24,
    background: "#000"
  })
}
const first = ref(false)
onActivated(async ()=>{
  // nếu prevPath ko phải blog thì fetch lại
  if(first.value && !history.state.back?.includes('/blog')){
    await fetchBlogList()
  }
})
onMounted( async () => {
  await nextTick()
  await fetchBlogList()
  first.value = true
  initZoom()
})


</script>


<style scoped>
</style>

