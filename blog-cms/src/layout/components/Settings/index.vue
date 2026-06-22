<template>
  <el-drawer v-model="drawerVisible" title="Tùy chỉnh giao diện" size="300px" direction="rtl">
    <div class="settings-container">
      <div class="setting-item">
        <span class="setting-label">Màu sidebar</span>
        <el-switch
          :model-value="settingsStore.sideTheme === 'theme-dark'"
          @change="toggleSideTheme"
          active-text="Tối"
          inactive-text="Sáng"
        />
      </div>
      <div class="setting-item">
        <span class="setting-label">Thẻ tab</span>
        <el-switch v-model="tagsView" active-text="Bật" inactive-text="Tắt" />
      </div>
      <div class="setting-item">
        <span class="setting-label">Cố định header</span>
        <el-switch v-model="fixedHeader" active-text="Bật" inactive-text="Tắt" />
      </div>
      <div class="setting-item">
        <span class="setting-label">Logo sidebar</span>
        <el-switch v-model="sidebarLogo" active-text="Bật" inactive-text="Tắt" />
      </div>
    </div>
  </el-drawer>
</template>

<script setup>
import { computed } from 'vue'
import { useSettingsStore } from '@/store/modules/settings'

const settingsStore = useSettingsStore()

const drawerVisible = defineModel('visible', { type: Boolean, default: false })

const tagsView = computed({
  get: () => settingsStore.tagsView,
  set: (val) => settingsStore.changeSetting({ key: 'tagsView', value: val })
})
const fixedHeader = computed({
  get: () => settingsStore.fixedHeader,
  set: (val) => settingsStore.changeSetting({ key: 'fixedHeader', value: val })
})
const sidebarLogo = computed({
  get: () => settingsStore.sidebarLogo,
  set: (val) => settingsStore.changeSetting({ key: 'sidebarLogo', value: val })
})

const toggleSideTheme = (val) => {
  settingsStore.changeSetting({
    key: 'sideTheme',
    value: val ? 'theme-dark' : 'theme-light'
  })
}
</script>

<style lang="scss" scoped>
.settings-container {
  padding: 0 8px;
  .setting-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px 0;
    border-bottom: 1px solid var(--el-border-color-light);
    .setting-label {
      font-size: 14px;
      color: var(--el-text-color-primary);
    }
  }
}
</style>
