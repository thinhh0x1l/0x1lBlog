<script setup lang="ts">
defineProps({
  loading: {
    type: Boolean,
    default: true
  },
  skeletonClass: {
    type: String,
    default: ''
  }
})
</script>

<template>
  <Transition mode="out-in" name="skeleton">
    <div
      v-if="loading"
      :key="'skeleton'"
      :class="['skeleton-placeholder', skeletonClass]"
    />
    <div v-else :key="'content'" class="skeleton-content">
      <slot />
    </div>
  </Transition>
</template>

<style scoped lang="scss">
.skeleton-placeholder {
  @include skeleton;
  display: block;
  height: 1em;
  margin-bottom: var(--space-sm);
}

.skeleton-content {
  animation: fadeIn var(--duration-slow) var(--ease-out) both;
}

.skeleton-enter-active,
.skeleton-leave-active {
  transition: opacity var(--duration-fast) ease;
}

.skeleton-enter-from,
.skeleton-leave-to {
  opacity: 0;
}

.skeleton-enter-to,
.skeleton-leave-from {
  opacity: 1;
}
</style>
