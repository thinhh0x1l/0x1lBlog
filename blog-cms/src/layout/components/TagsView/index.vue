<template>
  <div class="tags-view-container" v-if="settingsStore.tagsView">
    <ScrollPane ref="scrollPane" class="tags-view-wrapper">
      <router-link
        v-for="tag in tagsViewStore.visitedViews"
        :key="tag.path"
        :to="{ path: tag.path, query: tag.query }"
        class="tags-view-item"
        :class="isActive(tag.path) ? 'active' : ''"
        @click.middle="closeSelectedTag(tag)"
        @contextmenu.prevent="openMenu(tag, $event)"
      >
        <span>{{ tag.title }}</span>
        <el-icon v-if="!tag.meta?.affix" class="close-icon" @click.prevent.stop="closeSelectedTag(tag)">
          <Close />
        </el-icon>
      </router-link>
    </ScrollPane>

    <ul v-show="visible" :style="{ left: left + 'px', top: top + 'px' }" class="contextmenu">
      <li @click="refreshSelectedTag(selectedTag)">Làm mới</li>
      <li v-if="!selectedTag?.meta?.affix" @click="closeSelectedTag(selectedTag)">Đóng</li>
      <li @click="closeOthersTags">Đóng các tab khác</li>
      <li @click="closeAllTags">Đóng tất cả</li>
    </ul>
  </div>
</template>

<script setup>
import { ref, watch, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useTagsViewStore } from '@/store/modules/tagsView'
import { useSettingsStore } from '@/store/modules/settings'
import ScrollPane from './ScrollPane.vue'

const route = useRoute()
const router = useRouter()
const tagsViewStore = useTagsViewStore()
const settingsStore = useSettingsStore()

const visible = ref(false)
const left = ref(0)
const top = ref(0)
const selectedTag = ref({})
const scrollPane = ref(null)

const isActive = (path) => path === route.path

const closeSelectedTag = (view) => {
  tagsViewStore.delView(view).then(() => {
    if (isActive(view.path)) {
      const latestView = tagsViewStore.visitedViews.slice(-1)[0]
      if (latestView) {
        router.push(latestView.path)
      }
    }
  })
}

const closeOthersTags = () => {
  router.push(selectedTag.value.path)
  nextTick(() => {
    tagsViewStore.delOthersViews(selectedTag.value)
  })
}

const closeAllTags = () => {
  tagsViewStore.delAllViews().then(() => {
    const affix = tagsViewStore.visitedViews[0]
    if (affix) router.push(affix.path)
  })
}

const refreshSelectedTag = (view) => {
  const { path, query, hash } = view
  router.replace({
    path: '/redirect' + path,
    query: query,
    hash: hash
  })
}

const openMenu = (tag, e) => {
  selectedTag.value = tag
  left.value = e.clientX
  top.value = e.clientY
  visible.value = true
}

const closeMenu = () => { visible.value = false }

watch(() => route.path, () => {
  tagsViewStore.addView(route)
}, { immediate: true })

watch(visible, (val) => {
  if (val) {
    document.body.addEventListener('click', closeMenu, { once: true })
  }
})
</script>

<style lang="scss">
.tags-view-container {
  height: 34px;
  width: 100%;
  background: #fff;
  border-bottom: 1px solid #d8dce5;
  box-shadow: 0 1px 3px 0 rgba(0,0,0,.12), 0 0 3px 0 rgba(0,0,0,.04);
  .tags-view-wrapper {
    .tags-view-item {
      display: inline-block;
      position: relative;
      cursor: pointer;
      height: 26px;
      line-height: 26px;
      border: 1px solid #d8dce5;
      color: #495060;
      background: #fff;
      padding: 0 8px;
      font-size: 12px;
      margin-left: 5px;
      margin-top: 4px;
      text-decoration: none;
      &:first-of-type { margin-left: 15px; }
      &:last-of-type { margin-right: 15px; }
      &.active {
        background-color: #409EFF;
        color: #fff;
        border-color: #409EFF;
        &::before {
          content: '';
          background: #fff;
          display: inline-block;
          width: 8px;
          height: 8px;
          border-radius: 50%;
          position: relative;
          margin-right: 4px;
        }
      }
      .close-icon {
        width: 14px;
        height: 14px;
        vertical-align: -2px;
        margin-left: 4px;
        &:hover { background: rgba(0,0,0,.1); border-radius: 50%; }
      }
    }
  }
}

.contextmenu {
  margin: 0;
  background: #fff;
  z-index: 3000;
  position: fixed;
  list-style-type: none;
  padding: 5px 0;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 400;
  color: #333;
  box-shadow: 2px 2px 3px 0 rgba(0,0,0,.3);
  li {
    margin: 0;
    padding: 7px 16px;
    cursor: pointer;
    &:hover { background: #eee; }
  }
}
</style>
