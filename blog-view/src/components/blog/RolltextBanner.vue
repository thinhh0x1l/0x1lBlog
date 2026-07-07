<template>
  <div v-if="rolltext" class="rolltext-banner">
    <div class="rolltext-track" :style="{ animationDuration: speedMs + 'ms' }">
      <span class="rolltext-text">{{ rolltext.text }}</span>
      <span class="rolltext-text" v-if="isDuplicate">{{ rolltext.text }}</span>
    </div>
  </div>
</template>
<script setup>
import { computed, ref, onMounted } from 'vue'
const props = defineProps({ rolltext: Object })
const speeds = { slow: 15000, normal: 10000, fast: 6000 }
const speedMs = computed(() => speeds[props.rolltext?.speed] || 10000)
const isDuplicate = ref(false)
onMounted(() => { isDuplicate.value = true })
</script>
<style scoped lang="scss">
.rolltext-banner { overflow: hidden; white-space: nowrap; margin: 4px 0; width: 100%; }
.rolltext-track { display: inline-flex; gap: 48px; animation: scroll linear infinite; }
.rolltext-text { font-size: 0.75rem; color: var(--primary); font-weight: 500; }
@keyframes scroll {
  0% { transform: translateX(0); }
  100% { transform: translateX(-50%); }
}
</style>
