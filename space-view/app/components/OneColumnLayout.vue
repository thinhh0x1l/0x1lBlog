<template>
  <div class="one-column-layout">
    <main class="one-column-content">
      <slot />
    </main>
  </div>
</template>
<style scoped lang="scss">
.one-column-layout {
  max-width: var(--content-max-width, 720px);
  margin: 0 auto;
  width: 100%;
}
</style>