<template>
  <el-config-provider :locale="locale">
    <router-view v-slot="{ Component }">
      <transition name="page" mode="out-in">
        <component :is="Component" />
      </transition>
    </router-view>
  </el-config-provider>
</template>
<script setup>
import vi from 'element-plus/dist/locale/vi.mjs'

const locale = vi
</script>
