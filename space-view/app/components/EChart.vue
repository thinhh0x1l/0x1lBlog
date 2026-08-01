<template>
  <ClientOnly>
    <div ref="chartRef" class="echart-container" />
    <template #fallback>
      <div class="echart-container echart-loading" />
    </template>
  </ClientOnly>
</template>

<script setup lang="ts">
const props = defineProps({
  option: { type: Object, required: true },
  theme: { type: String, default: null },
})

const chartRef = ref<HTMLElement | null>(null)
let chart: any = null
let observer: ResizeObserver | null = null

function render() {
  if (!chartRef.value) return
  if (!chart) {
    import('echarts').then((echarts) => {
      chart = echarts.init(chartRef.value!, props.theme)
      chart.setOption(props.option, true)
    })
  } else {
    chart.setOption(props.option, true)
  }
}

onMounted(() => {
  render()
  if (chartRef.value) {
    observer = new ResizeObserver(() => chart?.resize())
    observer.observe(chartRef.value)
  }
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

.echart-loading {
  background: var(--bg-secondary);
  border-radius: var(--radius);
}
</style>
