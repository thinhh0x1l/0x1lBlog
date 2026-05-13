<template>
  <div>
   <BlogItem
       :blog-list="blogList"
       :loading="loading"
       :skeletonCount="5"
   />

    <Pagination v-if="pageInfo.totalPages>1" :getBlogList="getBlogList" :page-info="pageInfo"/>
  </div>
</template>
<script setup lang="ts">
 import BlogItem from "@/components/blogList/BlogItem.vue";
 import Pagination from "@/components/blogList/Pagination.vue";
 import {useToast} from "@/plugins/primevueConfig/primePluginVue.js";

 const toast = useToast()

 const props = defineProps({
   pageInfo:{
     type: Object,
     required: true
   },
   getBlogList:{
     type: Function,
     required: true
   },
   blogList:{
     type: Array,
     required: true,
   },
   loading: {
     type: Boolean,
     default: false
   },
 })
</script>