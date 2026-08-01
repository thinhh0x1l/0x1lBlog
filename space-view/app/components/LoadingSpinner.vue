<template>
  <div class="loading-spinner" :class="sizeClass">
    <div class="spinner"></div>
    <span v-if="text" class="spinner-text">{{ text }}</span>
  </div>
</template>

<script setup lang="ts">
const props = defineProps({
  size: { type: String, default: 'md', validator: (v: string) => ['sm', 'md', 'lg'].includes(v) },
  text: { type: String, default: '' },
})

const sizeClass = computed(() => `spinner-${props.size}`)
</script>

<style scoped lang="scss">
.loading-spinner {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 12px;
  padding: 24px;
}

.spinner {
  border: 3px solid var(--border-light);
  border-top-color: var(--primary);
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

.spinner-sm .spinner { width: 24px; height: 24px; border-width: 2px; }
.spinner-md .spinner { width: 36px; height: 36px; }
.spinner-lg .spinner { width: 48px; height: 48px; border-width: 4px; }

.spinner-text {
  font-size: 0.85rem;
  color: var(--text-muted);
}

@keyframes spin {
  to { transform: rotate(360deg); }
}
</style>
