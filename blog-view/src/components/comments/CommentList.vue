<template>
  <div >
    <Comment/>
    <Pagination/>
  </div>
</template>

<script setup lang="ts">
import Comment from "@/components/comments/Comment.vue";
import Pagination from "@/components/comments/Pagination.vue";
import {watch} from "vue";
import {useCommentStore} from "@/store/commentStore";
import {useRoute} from "vue-router";

const route = useRoute()
const commentStore = useCommentStore()

const props = defineProps<{
  page: number
  blogId: number|null
}>()
const init =  () => {
  commentStore.setCommentQueryPage(props.page)
  commentStore.setCommentQueryBlogId(props.blogId)
  commentStore.setCommentQueryPageNum(1)
  commentStore.getCommentList()
}
watch(() => route.path,
    (newId) => {
  commentStore.clearCommentData()
  init()
},{ immediate: true})


</script>
<style scoped>
</style>