<template>
  <div class="three-column-layout">
    <aside class="sidebar-left">
      <slot name="sidebar-left" />
    </aside>
    <main class="main-content">
      <slot />
    </main>
    <aside class="sidebar-right" v-if="!hideRight">
      <slot name="sidebar-right" />
    </aside>
  </div>
</template>
<script setup>
defineProps({
  hideRight: { type: Boolean, default: false }
})
</script>
<style scoped lang="scss">
.three-column-layout {
  display: grid;
  grid-template-columns: 280px 1fr 280px;
  gap: 24px;
  max-width: var(--page-max-width);
  margin: 0 auto;
  width: 100%;
}
.sidebar-left,
.sidebar-right {
  position: sticky;
  top: calc(var(--header-height, 64px) + 24px);
  height: fit-content;
  max-height: calc(100vh - var(--header-height, 64px) - 48px);
  overflow-y: auto;
}
.main-content {
  min-width: 0;
}
@include respond-to(xl) {
  .three-column-layout {
    grid-template-columns: 280px 1fr 280px;
  }
}
@include respond-to(lg) {
  .three-column-layout {
    grid-template-columns: 280px 1fr;
  }
  .sidebar-right {
    display: none;
  }
}
@include respond-to(md) {
  .three-column-layout {
    grid-template-columns: 1fr;
  }
  .sidebar-left {
    display: none;
  }
}
</style>
