<script setup>
import { useThemeStore } from '@/stores/theme'

const theme = useThemeStore()
</script>

<template>
  <el-button
    :icon="theme.isDark ? 'Sunny' : 'Moon'"
    :title="theme.isDark ? 'Chế độ sáng' : 'Chế độ tối'"
    circle
    size="default"
    @click="theme.toggle()"
  />
</template>

<style scoped lang="scss">
.el-button {
  transition: transform var(--duration-fast) var(--ease-out);

  &:active {
    transform: scale(0.9);
  }
}
</style>
