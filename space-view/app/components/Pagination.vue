<template>
  <nav class="pagination" v-if="totalPages > 1">
    <button class="page-btn" :disabled="modelValue <= 1" @click="$emit('update:modelValue', modelValue - 1)">
      <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="15 18 9 12 15 6"/></svg>
    </button>
    <template v-for="page in visiblePages" :key="page">
      <span v-if="page === '...'" class="page-dots">...</span>
      <button v-else :class="['page-btn', 'page-num', { active: page === modelValue }]" @click="$emit('update:modelValue', page)">{{ page }}</button>
    </template>
    <button class="page-btn" :disabled="modelValue >= totalPages" @click="$emit('update:modelValue', modelValue + 1)">
      <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="9 18 15 12 9 6"/></svg>
    </button>
  </nav>
</template>

<script setup lang="ts">
const props = defineProps({
  modelValue: { type: Number, default: 1 },
  totalPages: { type: Number, default: 1 },
})
defineEmits(['update:modelValue'])

const visiblePages = computed(() => {
  const total = props.totalPages
  const current = props.modelValue
  const pages: (number | string)[] = []
  
  if (total <= 7) {
    for (let i = 1; i <= total; i++) pages.push(i)
  } else {
    pages.push(1)
    if (current > 3) pages.push('...')
    for (let i = Math.max(2, current - 1); i <= Math.min(total - 1, current + 1); i++) {
      pages.push(i)
    }
    if (current < total - 2) pages.push('...')
    pages.push(total)
  }
  return pages
})
</script>

<style scoped lang="scss">
.pagination {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
}

.page-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  min-width: 36px;
  height: 36px;
  padding: 0 8px;
  border: 1px solid var(--border-light);
  border-radius: var(--radius-md);
  background: var(--surface);
  color: var(--text-secondary);
  cursor: pointer;
  transition: all 0.15s ease;
  font-size: 0.85rem;

  &:hover:not(:disabled) {
    border-color: var(--primary);
    color: var(--primary);
  }

  &:disabled {
    opacity: 0.4;
    cursor: not-allowed;
  }

  &.active {
    background: var(--primary);
    border-color: var(--primary);
    color: white;
    font-weight: 600;
  }
}

.page-dots {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  color: var(--text-muted);
}
</style>
