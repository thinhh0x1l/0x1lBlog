<template>
  <div class="layout">
    <aside class="layout-left">
      <slot name="sidebar-left" />
    </aside>
    <main class="layout-main">
      <slot />
    </main>
    <aside class="layout-right" v-if="!hideRight">
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
.layout {
  display: grid;
  grid-template-columns: 260px 1fr 300px;
  gap: 20px;
  max-width: var(--page-max-width);
  margin: 0 auto;
  width: 100%;
}

.layout-left,
.layout-right {
  position: sticky;
  top: calc(52px + 20px);
  height: fit-content;
  max-height: calc(100vh - 52px - 40px);
  overflow-y: auto;
  &::-webkit-scrollbar { width: 4px; }
  &::-webkit-scrollbar-track { background: transparent; }
  &::-webkit-scrollbar-thumb { background: var(--border); border-radius: 4px; }
}

.layout-main {
  min-width: 0;
}

@media (max-width: 1280px) {
  .layout {
    grid-template-columns: 260px 1fr 280px;
  }
}

@media (max-width: 1024px) {
  .layout {
    grid-template-columns: 260px 1fr;
  }
  .layout-right { display: none; }
}

@media (max-width: 768px) {
  .layout {
    grid-template-columns: 1fr;
  }
  .layout-left { display: none; }
}
</style>
