<template>
  <div class="two-column-layout">
    <aside class="sidebar-left">
      <slot name="sidebar-left" />
    </aside>
    <main class="main-content">
      <slot />
    </main>
  </div>
</template>
<style scoped lang="scss">
.two-column-layout {
  display: grid;
  grid-template-columns: 280px 1fr;
  gap: 24px;
  max-width: var(--page-max-width);
  margin: 0 auto;
  width: 100%;
}
.sidebar-left {
  position: sticky;
  top: calc(var(--header-height, 64px) + 24px);
  height: fit-content;
  max-height: calc(100vh - var(--header-height, 64px) - 48px);
  overflow-y: auto;
}
.main-content {
  min-width: 0;
}
@include respond-to(lg) {
  .two-column-layout {
    grid-template-columns: 240px 1fr;
  }
}
@include respond-to(md) {
  .two-column-layout {
    grid-template-columns: 1fr;
  }
  .sidebar-left {
    display: none;
  }
}
</style>