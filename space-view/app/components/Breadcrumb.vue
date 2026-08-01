<template>
  <nav class="breadcrumb">
    <template v-for="(item, index) in items" :key="index">
      <NuxtLink v-if="item.to" :to="item.to" class="breadcrumb-link">{{ item.label }}</NuxtLink>
      <span v-else class="breadcrumb-current">{{ item.label }}</span>
      <span v-if="index < items.length - 1" class="breadcrumb-separator">/</span>
    </template>
  </nav>
</template>

<script setup lang="ts">
defineProps({
  items: {
    type: Array,
    default: () => [],
    validator: (v: any[]) => v.every((item: any) => item.label),
  },
})
</script>

<style scoped lang="scss">
.breadcrumb {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.85rem;
}

.breadcrumb-link {
  color: var(--text-muted);
  text-decoration: none;
  transition: color 0.15s ease;

  &:hover {
    color: var(--primary);
  }
}

.breadcrumb-current {
  color: var(--text-primary);
  font-weight: 500;
}

.breadcrumb-separator {
  color: var(--text-muted);
}
</style>
