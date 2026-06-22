<template>
  <div :class="classObj" class="app-wrapper"
    :style="{ '--current-color': settingsStore.theme, '--current-color-light': settingsStore.theme + '1a', '--current-color-dark-bg': settingsStore.theme + '33' }"
  >
    <div v-if="device === 'mobile' && sidebar.opened" class="drawer-bg" @click="handleClickOutside" />
    <SideBar class="sidebar-container" />
    <div class="main-container" :class="{ 'hide-sidebar': !sidebar.opened }">
      <div class="fixed-header" :class="{ 'fixed-header--fixed': settingsStore.fixedHeader }">
        <NavBar />
        <TagsView v-if="settingsStore.tagsView" />
      </div>
      <AppMain />
      <Copyright />
    </div>
    <Settings v-model:visible="settingsVisible" />

    <el-tooltip content="Tùy chỉnh giao diện" placement="left">
      <div class="settings-trigger" @click="settingsVisible = true">
        <el-icon :size="20"><Setting /></el-icon>
      </div>
    </el-tooltip>
  </div>
</template>

<script setup>
import { ref, computed, watch, watchEffect } from 'vue'
import { useWindowSize } from '@vueuse/core'
import { useAppStore } from '@/store/index.js'
import { useSettingsStore } from '@/store/modules/settings.js'
import { SideBar, NavBar, TagsView, AppMain, Settings, Copyright } from './components'

const appStore = useAppStore()
const settingsStore = useSettingsStore()

const settingsVisible = ref(false)
const showSettings = () => { settingsVisible.value = true }

const sidebar = computed(() => appStore.sidebar)
const device = computed(() => appStore.device)

const classObj = computed(() => ({
  hideSidebar: !sidebar.value.opened,
  openSidebar: sidebar.value.opened,
  withoutAnimation: sidebar.value.withoutAnimation,
  mobile: device.value === 'mobile',
  [settingsStore.sideTheme]: true
}))

const { width } = useWindowSize()
const WIDTH = 992

watch(() => device.value, () => {
  if (device.value === 'mobile' && sidebar.value.opened) {
    appStore.closeSideBar({ withoutAnimation: false })
  }
})

watchEffect(() => {
  if (width.value - 1 < WIDTH) {
    appStore.setDevice('mobile')
    appStore.closeSideBar({ withoutAnimation: true })
  } else {
    appStore.setDevice('desktop')
  }
})

function handleClickOutside() {
  appStore.closeSideBar({ withoutAnimation: false })
}
</script>

<style lang="scss" scoped>
@use "@/assets/styles/mixin.scss" as mix;
@use "@/assets/styles/variables.module.scss" as vars;

.app-wrapper {
  @include mix.clearfix;
  position: relative;
  height: 100%;
  width: 100%;

  &.mobile.openSidebar {
    position: fixed;
    top: 0;
  }
}

.sidebar-container {
  transition: width 0.28s;
  width: vars.$base-sidebar-width !important;
  height: 100%;
  position: fixed;
  top: 0;
  bottom: 0;
  left: 0;
  z-index: 1001;
  overflow: hidden;
}

.main-container {
  min-height: 100%;
  transition: margin-left 0.28s;
  margin-left: vars.$base-sidebar-width;
  position: relative;

  &.hide-sidebar {
    margin-left: 54px;
  }
}

.fixed-header {
  position: relative;
  z-index: 10;
}

.fixed-header--fixed {
  position: fixed;
  top: 0;
  right: 0;
  left: vars.$base-sidebar-width;
  z-index: 9;
  transition: left 0.28s;
}

.hideSidebar .fixed-header--fixed {
  left: 54px;
}

.mobile .fixed-header--fixed {
  left: 0;
}

.drawer-bg {
  background: #000;
  opacity: 0.3;
  width: 100%;
  top: 0;
  height: 100%;
  position: absolute;
  z-index: 999;
}

.settings-trigger {
  position: fixed;
  bottom: 40px;
  right: 40px;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: var(--el-color-primary, #409eff);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 9999;
  box-shadow: 0 2px 12px rgba(0,0,0,0.3);
  transition: transform 0.3s;
  &:hover { transform: scale(1.1); }
}
</style>

<style lang="scss">
.fixed-header--fixed + .app-main {
  min-height: 100vh;
  padding: 104px 20px 20px;
}

.fixed-header:not(.fixed-header--fixed) + .app-main {
  min-height: calc(100vh - 84px);
  padding: 20px;
}

</style>
