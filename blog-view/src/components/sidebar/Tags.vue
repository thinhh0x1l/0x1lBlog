<template>
  <div class="container m-box">
    <div class="header-container">
      <font-awesome-icon icon="tags" class="mr-2" />Đám mây Tag</div>
    <div class="body-container">
      <TagComponent
          :list-tag="tagList"
          :loading="loading"
          :skeleton-count="10"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import TagComponent from '@/components/blogList/Tag.vue'
import type {Tag, TagSlug} from "@/types/tagType";

const props = defineProps<{
  tagList: TagSlug[],
  loading: boolean
}>()

</script>
<style scoped>
.container{
  position: relative;
  margin: 1rem 0;
  border: 1px solid rgba(34, 36, 38, .15);
  box-shadow: 0 1px 2px 0 rgba(34, 36, 38, .15);
  border-radius: .28571429rem;
}
.header-container{
  border-top: none;
  margin-top: 0;
  padding: 10px;
  bottom: 0;
  margin-bottom: 0;
  top: 0;
  border-radius: .28571429rem .28571429rem 0 0;
  background: #f3f4f5;
  color: rgba(0, 0, 0, .6);
}
:deep(.tag){
  margin: 0 !important;
  padding: 0.47rem 0.8rem 0.47rem 0.8rem !important;
  clip-path: none !important;
  border-radius: .28571429rem !important;
}
:deep(.tag-content){
  padding-left: 0 !important;
}
:deep(.tag::after){
  content: none !important;
}
.body-container{
  border-top: 2px solid #fbbd08 !important;
  position: relative;
  background: #fff;
  padding: 7px;
}
</style>