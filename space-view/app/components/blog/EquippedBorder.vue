<template>
  <div v-if="border" class="equipped-border" :style="borderStyle">
    <slot />
  </div>
</template>
<script setup lang="ts">
const props = defineProps({ border: Object })
const borderStyle = computed(() => {
  if (!props.border) return {}
  if ((props.border as any).type === 'GRADIENT' && (props.border as any).colors) {
    return {
      background: `linear-gradient(135deg, ${(props.border as any).colors[0]}, ${(props.border as any).colors[1]})`,
      padding: '3px',
      borderRadius: '50%',
      display: 'inline-block',
    }
  }
  return { border: '3px solid var(--primary)', borderRadius: '50%', padding: '2px', display: 'inline-block' }
})
</script>
