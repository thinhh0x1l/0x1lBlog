<template>
  <el-config-provider :locale="locale">
    <router-view />
  </el-config-provider>
</template>
<script setup>
import vi from 'element-plus/dist/locale/vi.mjs'
const locale = vi
</script>
