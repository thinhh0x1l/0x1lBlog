<template>
  <el-breadcrumb :separator-icon="ArrowRight">
    <el-breadcrumb-item :to="{ path: '/home' }">Trang chủ</el-breadcrumb-item>
    <el-breadcrumb-item>{{ parentTitle }}</el-breadcrumb-item>
    <el-breadcrumb-item>{{ route.meta.title }}</el-breadcrumb-item>
  </el-breadcrumb>
</template>
<script setup >

import {useRoute} from "vue-router";
import {ArrowRight} from "@element-plus/icons-vue";
const route = useRoute()
defineProps({
  parentTitle: {
    required: true,
    type: String,
  }
})

</script>