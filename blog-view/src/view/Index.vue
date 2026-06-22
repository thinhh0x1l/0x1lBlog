<template>
  <div class="app-layout">
    <AppHeader />
    <div class="app-body">
      <router-view />
    </div>
    <AppFooter />
  </div>
</template>
<script setup>
import AppHeader from '@/components/layout/AppHeader.vue'
import AppFooter from '@/components/layout/AppFooter.vue'
</script>
<style lang="scss">
.app-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: var(--bg);
  position: relative;
}
.app-body {
  flex: 1;
  padding-top: 24px;
  padding-bottom: 48px;
  max-width: var(--page-max-width);
  margin: 0 auto;
  padding-left: 32px;
  padding-right: 32px;
  width: 100%;

  @include respond-to(md) {
    padding-left: 16px;
    padding-right: 16px;
    padding-top: 16px;
  }
}
</style>
