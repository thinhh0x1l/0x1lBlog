<template>
  <div class="app">
    <AppHeader />
    <div class="app-body">
      <slot />
    </div>
    <AppFooter />
  </div>
</template>

<style lang="scss">
.app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: var(--bg);
}
.app-body {
  flex: 1;
  padding: 20px 24px 48px;
  max-width: var(--page-max-width);
  margin: 0 auto;
  width: 100%;
}
</style>
