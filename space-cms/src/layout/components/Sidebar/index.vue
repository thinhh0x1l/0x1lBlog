<template>
  <div :class="['sidebar-container', { 'has-logo': settingsStore.sidebarLogo }, sideTheme]">
    <Logo v-if="settingsStore.sidebarLogo" :collapse="isCollapse" />
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :background-color="menuBg"
        :text-color="menuText"
        :active-text-color="menuActiveText"
        :unique-opened="true"
        :collapse-transition="false"
        mode="vertical"
        router
      >
        <SidebarItem
          v-for="(route, index) in sidebarRouters"
          :key="route.path + index"
          :item="route"
          :base-path="route.path"
        />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useAppStore } from '@/store/index.js'
import { useSettingsStore } from '@/store/modules/settings.js'
import router from "@/router/index.js"
import Logo from './Logo.vue'
import SidebarItem from './SidebarItem.vue'
import variables from '@/assets/styles/variables.module.scss'

const route = useRoute()
const appStore = useAppStore()
const settingsStore = useSettingsStore()

const sidebarRouters = computed(() => {
  return router.options.routes.filter(r => !r.hidden)
})

const isCollapse = computed(() => !appStore.sidebar.opened)
const sideTheme = computed(() => settingsStore.sideTheme)

const menuBg = computed(() => {
  if (settingsStore.isDark) return 'var(--sidebar-bg)'
  return sideTheme.value === 'theme-dark' ? variables.menuBg : variables.menuLightBg
})

const menuText = computed(() => {
  if (settingsStore.isDark) return 'var(--sidebar-text)'
  return sideTheme.value === 'theme-dark' ? variables.menuText : variables.menuLightText
})

const menuActiveText = computed(() => variables.menuActiveText)

const activeMenu = computed(() => {
  const { meta, path } = route
  if (meta.activeMenu) return meta.activeMenu
  return path
})
</script>

<style lang="scss" scoped>
.sidebar-container {
  background-color: v-bind(menuBg);

  .scrollbar-wrapper {
    overflow-x: hidden !important;
    background-color: v-bind(menuBg);
  }

  .el-scrollbar__bar.is-vertical { right: 0px; }
  .el-scrollbar { height: 100%; }

  &.has-logo .el-scrollbar { height: calc(100% - 50px); }

  .is-horizontal { display: none; }

  a { display: inline-block; width: 100%; overflow: hidden; }

  .el-menu {
    border: none;
    height: 100%;
    width: 100% !important;
  }

  .el-menu-item, .el-sub-menu__title { height: 44px !important; line-height: 44px !important; }

  .el-menu-item .el-menu-tooltip__trigger { display: inline-block !important; }

  .sub-menu-title-noDropdown,
  .el-sub-menu__title {
    &:hover { background-color: rgba(0, 0, 0, 0.06); }
  }

  &.theme-dark {
    box-shadow: 2px 0 8px rgba(0, 0, 0, 0.4);
    border-right: none;

    .el-menu-item.is-active {
      position: relative;
      &::before {
        content: '';
        position: absolute;
        inset: 0;
        background-color: var(--current-color-dark-bg, rgba(64, 158, 255, 0.2));
        pointer-events: none;
        z-index: 1;
      }
    }
    .el-sub-menu.is-active > .el-sub-menu__title {
      color: var(--current-color, #409eff) !important;
    }
  }

  &.theme-light {
    border-right: 1px solid #e8eaf0;
    box-shadow: none;

    .el-menu-item, .el-sub-menu__title { color: rgba(0, 0, 0, 0.65); }
    .el-menu-item.is-active {
      color: var(--current-color, #409eff) !important;
      position: relative;
      &::before {
        content: '';
        position: absolute;
        inset: 0;
        background-color: var(--current-color-light, #ecf5ff);
        pointer-events: none;
        z-index: 1;
      }
    }
  }
}
</style>
