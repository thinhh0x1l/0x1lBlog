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
import { onMounted } from 'vue'
import vi from 'element-plus/dist/locale/vi.mjs'
import { useThemeStore } from '@/stores/theme'

const locale = vi
const theme = useThemeStore()

onMounted(() => {
  theme.init()
})
</script>
