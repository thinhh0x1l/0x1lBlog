<template>
  <button :class="['like-button', { liked }]" @click="$emit('toggle')">
    <span class="like-icon">{{ liked ? '❤️' : '🤍' }}</span>
    <span class="like-count" v-if="count > 0">{{ count }}</span>
  </button>
</template>

<script setup lang="ts">
defineProps({
  liked: { type: Boolean, default: false },
  count: { type: Number, default: 0 },
})

defineEmits(['toggle'])
</script>

<style scoped lang="scss">
.like-button {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 6px 12px;
  border: 1px solid var(--border-light);
  border-radius: var(--radius-full);
  background: var(--surface);
  cursor: pointer;
  transition: all 0.15s ease;

  &:hover {
    border-color: var(--primary);
    background: var(--primary-50);
  }

  &.liked {
    border-color: #ef4444;
    background: #fef2f2;
  }
}

.like-icon {
  font-size: 1rem;
}

.like-count {
  font-size: 0.82rem;
  font-weight: 600;
  color: var(--text-secondary);
}
</style>
