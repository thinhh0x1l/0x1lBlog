<template>
  <el-scrollbar ref="scrollContainer" :vertical="false" class="tags-view-scroll" @wheel.prevent="handleScroll">
    <slot />
  </el-scrollbar>
</template>

<script setup>
import { ref } from 'vue'

const scrollContainer = ref(null)

const handleScroll = (e) => {
  const container = scrollContainer.value?.$el?.querySelector('.el-scrollbar__wrap')
  if (container) {
    container.scrollLeft += e.deltaY > 0 ? 30 : -30
  }
}

defineExpose({ scrollContainer })
</script>

<style lang="scss" scoped>
.tags-view-scroll {
  overflow: hidden;
  :deep(.el-scrollbar__bar.is-horizontal) { height: 0; }
}
</style>
