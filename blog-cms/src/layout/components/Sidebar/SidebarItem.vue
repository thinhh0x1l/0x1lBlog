<template>
  <div v-if="!item.hidden">
    <!-- Item có children array -->
    <template v-if="item.children && item.children.length > 0">
      <template v-if="hasOneShowingChild(item.children, item) && (!onlyOneChild.children || onlyOneChild.noShowingChildren) && !item.alwaysShow">
        <el-menu-item v-if="onlyOneChild.meta" :index="resolvePath(onlyOneChild.path)" :class="{ 'submenu-title-noDropdown': !isNest }">
          <el-icon v-if="onlyOneChild.meta?.icon">
            <component :is="iconComponent(onlyOneChild.meta.icon)" />
          </el-icon>
          <template #title>
            <span class="menu-title">{{ onlyOneChild.meta.title }}</span>
          </template>
        </el-menu-item>
      </template>
        <el-sub-menu v-else ref="subMenu" :index="resolvePath(item.path)" teleported  :="''">
        <template #title>
          <el-icon v-if="item.meta?.icon">
            <component :is="iconComponent(item.meta.icon)" />
          </el-icon>
          <span class="menu-title">{{ item.meta?.title }}</span>
        </template>
        <sidebar-item
          v-for="child in item.children"
          :key="child.path"
          :is-nest="true"
          :item="child"
          :base-path="props.basePath"
          class="nest-menu"
        />
      </el-sub-menu>
    </template>
    <!-- Leaf item (không có children) render trực tiếp -->
    <template v-else>
      <el-menu-item v-if="item.meta" :index="resolvePath(item.path)" :class="{ 'submenu-title-noDropdown': !isNest }">
        <el-icon v-if="item.meta?.icon">
          <component :is="iconComponent(item.meta.icon)" />
        </el-icon>
        <template #title>
          <span class="menu-title">{{ item.meta.title }}</span>
        </template>
      </el-menu-item>
    </template>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { isExternal } from '@/utils/validate'
import * as ElementPlusIcons from '@element-plus/icons-vue'

const props = defineProps({
  item: { type: Object, required: true },
  isNest: { type: Boolean, default: false },
  basePath: { type: String, default: '' }
})

const onlyOneChild = ref({})

function iconComponent(iconName) {
  if (!iconName) return null
  return ElementPlusIcons[iconName] || null
}

function hasOneShowingChild(children = [], parent) {
  if (!children) children = []
  const showingChildren = children.filter(item => {
    if (item.hidden) return false
    onlyOneChild.value = item
    return true
  })
  if (showingChildren.length === 1) return true
  if (showingChildren.length === 0) {
    onlyOneChild.value = { ...parent, path: '', noShowingChildren: true }
    return true
  }
  return false
}

function resolvePath(routePath) {
  if (isExternal(routePath)) return routePath
  if (isExternal(props.basePath)) return props.basePath
  if (routePath.startsWith('/')) return routePath
  const fullPath = props.basePath + '/' + routePath
  return fullPath.replace(/\/+/g, '/')
}
</script>

<style scoped lang="scss">
.sub-el-icon, .nav-icon {
  display: inline-block;
  font-size: 15px;
  margin-right: 12px;
  position: relative;
}
</style>
