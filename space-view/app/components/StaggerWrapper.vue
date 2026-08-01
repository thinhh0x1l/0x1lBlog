<script setup lang="ts">
defineProps({
  staggerDelay: {
    type: Number,
    default: 50
  }
})
</script>

<template>
  <div class="stagger-wrapper">
    <slot />
  </div>
</template>

<style scoped lang="scss">
.stagger-wrapper > :deep(*) {
  opacity: 0;
  animation: fadeInUp var(--duration-slow) var(--ease-out) forwards;
}

.stagger-wrapper > :deep(*):nth-child(1) { animation-delay: 0ms; }
.stagger-wrapper > :deep(*):nth-child(2) { animation-delay: 50ms; }
.stagger-wrapper > :deep(*):nth-child(3) { animation-delay: 100ms; }
.stagger-wrapper > :deep(*):nth-child(4) { animation-delay: 150ms; }
.stagger-wrapper > :deep(*):nth-child(5) { animation-delay: 200ms; }
.stagger-wrapper > :deep(*):nth-child(6) { animation-delay: 250ms; }
.stagger-wrapper > :deep(*):nth-child(7) { animation-delay: 300ms; }
.stagger-wrapper > :deep(*):nth-child(8) { animation-delay: 350ms; }
.stagger-wrapper > :deep(*):nth-child(9) { animation-delay: 400ms; }
.stagger-wrapper > :deep(*):nth-child(10) { animation-delay: 450ms; }
</style>
