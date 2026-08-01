<template>
  <div class="sp-bar">
    <div v-for="(_, i) in group.stories" :key="i" class="sp-seg">
      <div class="sp-fill" :class="{ done: i < currentIndex, active: i === currentIndex }">
        <div v-if="i === currentIndex" class="sp-anim" :class="{ paused }" :style="{ '--sp-dur': group.stories[i].durationMs + 'ms' }"></div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps({ group: { type: Object, required: true }, currentIndex: { type: Number, default: 0 }, paused: { type: Boolean, default: false } })
</script>

<style scoped lang="scss">
.sp-bar { display: flex; gap: 3px; }
.sp-seg { flex: 1; height: 2px; background: rgba(255,255,255,0.25); border-radius: 99px; overflow: hidden; }
.sp-fill { height: 100%; width: 0; }
.sp-fill.done, .sp-fill.active { width: 100%; background: rgba(255,255,255,0.7); }
.sp-anim { height: 100%; width: 0; background: #fff; animation: spAnim var(--sp-dur, 5s) linear forwards; }
.sp-anim.paused { animation-play-state: paused; }
@keyframes spAnim { from { width: 0; } to { width: 100%; } }
</style>
