  <template>
  <section class="app-main">
    <router-view v-slot="{ Component, route: viewRoute }">
      <transition name="fade-transform" mode="out-in">
        <keep-alive :include="tagsViewStore.cachedViews">
          <component :is="Component" :key="viewRoute.path" />
        </keep-alive>
      </transition>
    </router-view>
  </section>
</template>

<script setup>
import { useTagsViewStore } from '@/store/modules/tagsView'

const tagsViewStore = useTagsViewStore()
</script>

<style lang="scss" scoped>
.app-main {
  width: 100%;
  position: relative;
  overflow: hidden;
}
</style>

<style lang="scss">
::-webkit-scrollbar { width: 6px; height: 6px; }
::-webkit-scrollbar-track { background-color: #f1f1f1; }
::-webkit-scrollbar-thumb { background-color: #c0c0c0; border-radius: 3px; }
</style>
