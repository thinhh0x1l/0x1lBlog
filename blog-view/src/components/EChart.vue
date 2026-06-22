<template>
  <div ref="chartRef" class="echart-container" />
</template>

<script setup>
import * as echarts from 'echarts'
import { onBeforeUnmount, onMounted, ref, watch } from 'vue'

const props = defineProps({
  option: { type: Object, required: true },
  theme: { type: String, default: null },
})

const chartRef = ref(null)
let chart
let observer

function render() {
  if (!chartRef.value) return
  if (!chart) chart = echarts.init(chartRef.value, props.theme)
  chart.setOption(props.option, true)
}

onMounted(() => {
  render()
  observer = new ResizeObserver(() => chart?.resize())
  observer.observe(chartRef.value)
})

watch(() => props.option, render, { deep: true })

onBeforeUnmount(() => {
  observer?.disconnect()
  chart?.dispose()
})
</script>

<style scoped lang="scss">
.echart-container {
  width: 100%;
  height: 100%;
  min-height: 300px;
}
</style>
