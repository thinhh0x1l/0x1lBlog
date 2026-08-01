<template>
  <footer v-if="settingsStore.footerVisible" class="copyright">
    <span>{{ settingsStore.footerContent }}</span>
  </footer>
</template>

<script setup>
import { useSettingsStore } from '@/store/modules/settings'

const settingsStore = useSettingsStore()
</script>

<style scoped>
.copyright {
  position: relative;
  margin-top: 20px;
  padding: 10px 0;
  text-align: center;
  color: #999;
  font-size: 12px;
  border-top: 1px solid #e8eaf0;
}
</style>
