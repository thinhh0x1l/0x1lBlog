<template>
  <div class="sn-overlay" @touchstart="onTouchStart" @touchmove="onTouchMove" @touchend="handleTouchEnd">
    <div class="sn-zone sn-prev" @click.stop="$emit('prev')"></div>
    <div class="sn-zone sn-pause" @click.stop="$emit('togglePause')"></div>
    <div class="sn-zone sn-next" @click.stop="$emit('next')"></div>
  </div>
</template>

<script setup>
import { useStoryGesture } from '@/composables/useStoryGesture'
const emit = defineEmits(['prev', 'next', 'togglePause', 'close'])
const { direction, onTouchStart, onTouchMove, onTouchEnd } = useStoryGesture()

const handleTouchEnd = (e) => {
  onTouchEnd(e)
  if (direction.value === 'left') emit('next')
  else if (direction.value === 'right') emit('prev')
  else if (direction.value === 'down') emit('close')
}
</script>

<style scoped lang="scss">
.sn-overlay { position: absolute; inset: 0; display: flex; z-index: 5; }
.sn-zone { height: 100%; }
.sn-prev { flex: 0 0 30%; }
.sn-pause { flex: 0 0 40%; }
.sn-next { flex: 0 0 30%; }
</style>
