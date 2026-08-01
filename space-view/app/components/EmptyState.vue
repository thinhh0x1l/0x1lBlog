<template>
  <div class="empty-state">
    <div class="empty-icon">
      <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
        <circle cx="12" cy="12" r="10"/>
        <path d="M8 15h8M9 9h.01M15 9h.01"/>
      </svg>
    </div>
    <h3 class="empty-title">{{ title }}</h3>
    <p class="empty-description">{{ description }}</p>
    <slot />
  </div>
</template>

<script setup lang="ts">
defineProps({
  title: { type: String, default: 'Không có dữ liệu' },
  description: { type: String, default: '' },
})
</script>

<style scoped lang="scss">
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 48px 24px;
  text-align: center;
}

.empty-icon {
  color: var(--text-muted);
  opacity: 0.5;
  margin-bottom: 16px;
}

.empty-title {
  font-size: 1.1rem;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 8px 0;
}

.empty-description {
  font-size: 0.9rem;
  color: var(--text-muted);
  margin: 0 0 16px 0;
}
</style>
