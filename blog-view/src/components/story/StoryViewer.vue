<template>
  <Teleport to="body">
    <Transition name="sv-overlay">
      <div v-if="store.viewerVisible" class="sv-overlay" tabindex="-1" ref="overlayRef" @click.self="store.closeViewer()">
        <button v-if="hasNextGroup" class="sv-next-user" @click.stop="skipToNextUser" title="Chuyển sang người khác">→</button>
        <Transition name="sv-panel" appear>
          <div v-if="player.currentStory" class="sv-panel" @mousedown="player.pause()" @mouseup="player.play()" @mouseleave="player.play()">
            <StoryProgress :group="player.currentGroup" :current-index="player.currentIndex" :paused="player.paused" />
            <StoryHeader :story="player.currentStory" @close="store.closeViewer()" />
            <div class="sv-media" v-if="player.currentStory.mediaType === 'VIDEO'">
              <StoryVideo :story="player.currentStory" :is-active="true" @loaded="didLoad" @ended="player.goNext()" />
            </div>
            <div class="sv-media" v-else>
              <StoryImage :story="player.currentStory" :is-active="true" @loaded="didLoad" />
            </div>
            <StoryFooter @reply="onReply" @react="onReact" @share="onShare" @menu="onMenu" />
            <StoryNavigation @prev="player.goPrev()" @next="player.goNext()" @toggle-pause="player.togglePause()" @close="store.closeViewer()" />
          </div>
        </Transition>
      </div>
    </Transition>
  </Teleport>
</template>

<script setup>
import { ref, reactive, computed, watch, nextTick, onUnmounted } from 'vue'
import { useStoryStore } from '@/stores/storyStore'
import { useStoryPlayer } from '@/composables/useStoryPlayer'
import { useKeyboard } from '@/composables/useKeyboard'
import { usePreload } from '@/composables/usePreload'
import StoryProgress from './StoryProgress.vue'
import StoryHeader from './StoryHeader.vue'
import StoryImage from './StoryImage.vue'
import StoryVideo from './StoryVideo.vue'
import StoryFooter from './StoryFooter.vue'
import StoryNavigation from './StoryNavigation.vue'

const store = useStoryStore()
const overlayRef = ref(null)
const { preload, destroy: destroyPreload } = usePreload()

const hasNextGroup = computed(() => store.currentGroupIndex < store.userGroups.length - 1)
const skipToNextUser = () => { store.nextGroup() }

const player = reactive(useStoryPlayer(
  computed(() => store.userGroups),
  computed(() => store.currentGroupIndex),
  () => store.nextGroup()
))

const didLoad = () => {
  if (player.currentStory) store.markViewed(player.currentStory.id)
  const nextStory = player.currentGroup?.stories?.[player.currentIndex + 1]
  if (nextStory?.mediaUrl) preload(nextStory.mediaUrl)
}

useKeyboard({
  Escape: () => store.closeViewer(),
  ArrowLeft: () => player.goPrev(),
  ArrowRight: () => player.goNext(),
  ' ': () => player.togglePause(),
})

watch(() => store.viewerVisible, (v) => { if (v) nextTick(() => overlayRef.value?.focus()) })

const onReply = (text) => { console.log('reply', text) }
const onReact = (type) => { console.log('react', type) }
const onShare = () => { console.log('share') }
const onMenu = (action) => { console.log('menu', action) }

onUnmounted(destroyPreload)
</script>

<style scoped lang="scss">
.sv-overlay { position: fixed; top: 0; left: 0; width: 100vw; height: 100vh; z-index: 99999; background: rgba(0,0,0,0.92); display: flex; align-items: center; justify-content: center; outline: none; }
.sv-panel { position: relative; max-width: 420px; width: 92%; }
.sv-media { position: relative; border-radius: 8px; overflow: hidden; width: 100%; }

.sv-overlay-enter-active, .sv-overlay-leave-active { transition: opacity 0.3s ease; }
.sv-overlay-enter-from, .sv-overlay-leave-to { opacity: 0; }
.sv-panel-enter-active { transition: transform 0.3s ease, opacity 0.3s ease; }
.sv-panel-leave-active { transition: transform 0.2s ease, opacity 0.2s ease; }
.sv-panel-enter-from { transform: scale(0.95); opacity: 0; }
.sv-panel-leave-to { transform: scale(0.95); opacity: 0; }

.sv-next-user { position: fixed; top: 50%; right: 16px; z-index: 100000; transform: translateY(-50%); width: 44px; height: 44px; border-radius: 50%; background: rgba(255,255,255,0.12); border: none; color: #fff; font-size: 1.2rem; cursor: pointer; display: flex; align-items: center; justify-content: center; backdrop-filter: blur(6px); transition: background 0.15s, transform 0.15s; }
.sv-next-user:hover { background: rgba(255,255,255,0.25); transform: translateY(-50%) scale(1.1); }
</style>
