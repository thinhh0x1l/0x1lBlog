<template>
  <el-tooltip content="Toàn màn hình" effect="dark" placement="bottom">
    <div class="screenfull right-menu-item hover-effect" @click="toggle">
      <el-icon :size="18">
        <FullScreen v-if="!isFullscreen" />
        <Aim v-else />
      </el-icon>
    </div>
  </el-tooltip>
</template>

<script setup>
import { useFullscreen } from '@vueuse/core'

const { isFullscreen, toggle } = useFullscreen()
</script>

<style lang="scss" scoped>
.screenfull {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 0 8px;
  height: 100%;
  cursor: pointer;
  transition: background 0.3s;
  &:hover { background: rgba(0, 0, 0, 0.025); }
}
</style>
