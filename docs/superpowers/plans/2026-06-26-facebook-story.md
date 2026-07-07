# Facebook-Style Story Feature Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Replace the existing StoryViewer with a complete Facebook/Instagram-style story system: per-user story circles, fullscreen viewer, video support, gestures, keyboard shortcuts, composable-based architecture.

**Architecture:** Pinia store holds user-grouped stories + viewer state. 14 components in `src/components/story/`. 5 composables in `src/composables/`. 2 utils. Mock data extracted to `src/assets/mock/stories.js`.

**Tech Stack:** Vue 3 (Composition API), Pinia, SCSS, IntersectionObserver, CSS animations (transform/opacity only for 60 FPS)

## Global Constraints

- Composition API only, no Options API
- No jQuery, no any hack, no `any` type
- Cleanup all event listeners / timers / videos on unmount
- All new files in `src/components/story/` (not `src/components/blog/`)
- Delete old `src/components/blog/StoryViewer.vue`
- Reuse existing `src/data/dummy.js` for mock — extract story subset to `src/assets/mock/stories.js`
- Build verification: `npm run build` must pass after each task
- Vietnamese UI text for user-facing strings

---

### Task 1: Mock Data + Utilities

**Files:**
- Create: `src/assets/mock/stories.js`
- Create: `src/utils/storyHelper.js`
- Create: `src/utils/time.js`

**Interfaces:**
- Consumes: existing `dummy.js` (users, stories arrays)
- Produces: `storyHelper.groupByUser(stories)`, `storyHelper.enrichStory(story, users)`, `storyHelper.findFlatIndex(groups, userId)`, `time.fromNow(date)`, `time.formatDuration(ms)`

- [ ] **Step 1: Create `src/assets/mock/stories.js`**

```js
import { users, stories } from '@/data/dummy'

export const mockUsers = users
export const mockStories = stories
```

- [ ] **Step 2: Create `src/utils/time.js`**

```js
import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'
import 'dayjs/locale/vi'
dayjs.extend(relativeTime)
dayjs.locale('vi')

export const fromNow = (date) => dayjs(date).fromNow()

export const formatDuration = (ms) => {
  const s = Math.floor(ms / 1000)
  if (s >= 60) return `${Math.floor(s / 60)}m`
  return `${s}s`
}
```

- [ ] **Step 3: Create `src/utils/storyHelper.js`**

```js
export const enrichStory = (story, users) => {
  const author = users.find(u => u.id === story.userId)
  return {
    ...story,
    userName: author?.displayName || 'Người dùng',
    userAvatar: author?.avatarUrl || '',
  }
}

export const groupByUser = (stories) => {
  const map = new Map()
  for (const s of stories) {
    if (!map.has(s.userId)) {
      map.set(s.userId, {
        userId: s.userId,
        userName: s.userName || 'Người dùng',
        userAvatar: s.userAvatar || '',
        storyCount: 0,
        stories: [],
      })
    }
    const group = map.get(s.userId)
    group.stories.push(s)
    group.storyCount++
  }
  return Array.from(map.values())
}

export const findFlatIndex = (groups, userId) => {
  let idx = 0
  for (const g of groups) {
    if (g.userId === userId) return idx
    idx += g.stories.length
  }
  return 0
}
```

- [ ] **Step 4: Build verification**

Run: `npm run build` from `blog-view/`

---

### Task 2: Composables — useStoryTimer + useKeyboard + useStoryGesture + usePreload

**Files:**
- Create: `src/composables/useStoryTimer.js`
- Create: `src/composables/useKeyboard.js`
- Create: `src/composables/useStoryGesture.js`
- Create: `src/composables/usePreload.js`

**Interfaces:**
- `useStoryTimer(onElapsed, getMs)` → `{ start, stop, pause, resume }`
- `useKeyboard(handlers)` → `{ register, unregister }`
- `useStoryGesture()` → `{ direction, onTouchStart, onTouchEnd, onTouchMove }`
- `usePreload(getNextUrl)` → `{ preload }`

- [ ] **Step 1: Create `useStoryTimer.js`**

```js
import { ref, onUnmounted } from 'vue'

export function useStoryTimer(onElapsed, getMs) {
  const remaining = ref(0)
  const isRunning = ref(false)
  let timer = null
  let startedAt = 0

  const stop = () => {
    if (timer) { clearTimeout(timer); timer = null }
    isRunning.value = false
  }

  const start = () => {
    stop()
    const ms = getMs()
    if (ms <= 0) return
    remaining.value = ms
    isRunning.value = true
    startedAt = Date.now()
    timer = setTimeout(() => { isRunning.value = false; onElapsed() }, ms)
  }

  const pause = () => {
    if (!isRunning.value) return
    const elapsed = Date.now() - startedAt
    remaining.value = Math.max(0, getMs() - elapsed)
    stop()
  }

  const resume = () => {
    if (remaining.value <= 0) return
    isRunning.value = true
    startedAt = Date.now()
    timer = setTimeout(() => { isRunning.value = false; onElapsed() }, remaining.value)
  }

  onUnmounted(stop)

  return { start, stop, pause, resume, remaining, isRunning }
}
```

- [ ] **Step 2: Create `useKeyboard.js`**

```js
import { onMounted, onUnmounted } from 'vue'

export function useKeyboard(handlers) {
  const onKey = (e) => {
    const fn = handlers[e.key]
    if (fn) { e.preventDefault(); fn() }
  }
  onMounted(() => window.addEventListener('keydown', onKey))
  onUnmounted(() => window.removeEventListener('keydown', onKey))

  return { register: () => window.addEventListener('keydown', onKey), unregister: () => window.removeEventListener('keydown', onKey) }
}
```

- [ ] **Step 3: Create `useStoryGesture.js`**

```js
import { ref } from 'vue'

const SWIPE_THRESHOLD_X = 50
const SWIPE_THRESHOLD_Y = 80

export function useStoryGesture() {
  const direction = ref(null)
  let startX = 0, startY = 0

  const onTouchStart = (e) => {
    const t = e.touches[0]
    startX = t.clientX; startY = t.clientY
    direction.value = null
  }

  const onTouchMove = (e) => {
    const dx = Math.abs(e.touches[0].clientX - startX)
    const dy = Math.abs(e.touches[0].clientY - startY)
    if (dx > dy) e.preventDefault()
  }

  const onTouchEnd = (e) => {
    const dx = e.changedTouches[0].clientX - startX
    const dy = e.changedTouches[0].clientY - startY
    if (Math.abs(dx) > SWIPE_THRESHOLD_X && Math.abs(dx) > Math.abs(dy) * 2) {
      direction.value = dx < 0 ? 'left' : 'right'
    } else if (Math.abs(dy) > SWIPE_THRESHOLD_Y && Math.abs(dy) > Math.abs(dx) * 2) {
      direction.value = dy > 0 ? 'down' : 'up'
    }
  }

  return { direction, onTouchStart, onTouchMove, onTouchEnd }
}
```

- [ ] **Step 4: Create `usePreload.js`**

```js
export function usePreload() {
  let observer = null

  const preload = (url) => {
    if (!url) return
    const img = new Image()
    img.src = url
  }

  const watchElement = (el, loadFn) => {
    if (!window.IntersectionObserver) return
    observer = new IntersectionObserver((entries) => {
      entries.forEach(e => { if (e.isIntersecting) { loadFn(); observer?.unobserve(e.target) } })
    }, { rootMargin: '200px' })
    if (el) observer.observe(el)
  }

  const destroy = () => { observer?.disconnect(); observer = null }

  return { preload, watchElement, destroy }
}
```

- [ ] **Step 5: Build verification**

Run: `npm run build` from `blog-view/`

---

### Task 3: useStoryPlayer Composable

**Files:**
- Create: `src/composables/useStoryPlayer.js`

**Interfaces:**
- Consumes: `useStoryTimer`, store's `userGroups`, `currentGroupIndex`
- Produces: `{ currentIndex, paused, isPlaying, goNext, goPrev, goToIndex, play, pause, togglePause }`

- [ ] **Step 1: Create `useStoryPlayer.js`**

```js
import { ref, computed, watch } from 'vue'
import { useStoryTimer } from './useStoryTimer'

export function useStoryPlayer(groupsRef, groupIndexRef, onAdvanceGroup, onEnd) {
  const currentIndex = ref(0)
  const paused = ref(false)

  const currentGroup = computed(() => groupsRef.value?.[groupIndexRef.value] || null)
  const currentStory = computed(() => currentGroup.value?.stories?.[currentIndex.value] || null)
  const isPlaying = computed(() => !paused.value && currentStory.value != null)

  const getMs = () => currentStory.value?.durationMs || 5000

  const goNext = () => {
    if (!currentGroup.value) return
    if (currentIndex.value < currentGroup.value.stories.length - 1) {
      currentIndex.value++
      paused.value = false
      timer.start()
    } else {
      onAdvanceGroup()
    }
  }

  const goPrev = () => {
    if (currentIndex.value > 0) {
      currentIndex.value--
      paused.value = false
      timer.start()
    }
  }

  const goToIndex = (idx) => {
    if (idx >= 0 && idx < (currentGroup.value?.stories.length || 0)) {
      currentIndex.value = idx
      paused.value = false
      timer.start()
    }
  }

  const play = () => { paused.value = false; timer.resume() }
  const pause = () => { paused.value = true; timer.pause() }
  const togglePause = () => { paused.value ? play() : pause() }

  const onElapsed = () => { goNext() }
  const timer = useStoryTimer(onElapsed, getMs)

  const syncTimer = () => {
    timer.stop()
    if (!paused.value && currentStory.value) timer.start()
  }

  watch(groupIndexRef, () => { currentIndex.value = 0; paused.value = false; syncTimer() })
  watch(currentIndex, () => {})

  const reset = () => { currentIndex.value = 0; paused.value = false; timer.stop() }

  return { currentIndex, paused, isPlaying, currentStory, currentGroup, goNext, goPrev, goToIndex, play, pause, togglePause, reset }
}
```

- [ ] **Step 2: Build verification**

Run: `npm run build` from `blog-view/`

---

### Task 4: Pinia Story Store

**Files:**
- Create: `src/stores/storyStore.js`

**Interfaces:**
- Consumes: `storyApi`, `storyHelper`
- Produces: `{ userGroups, viewedMap, currentGroupIndex, viewerVisible, fetchStories, openViewer, closeViewer, markViewed, isGroupViewed, nextGroup }`

- [ ] **Step 1: Create `storyStore.js`**

```js
import { defineStore } from 'pinia'
import { storyApi } from '@/api/story'
import { enrichStory, groupByUser } from '@/utils/storyHelper'
import { mockUsers, mockStories } from '@/assets/mock/stories'

export const useStoryStore = defineStore('story', {
  state: () => ({
    userGroups: [],
    viewedMap: {},  // { [storyId]: true }
    currentGroupIndex: -1,
    viewerVisible: false,
  }),
  getters: {
    currentGroup: (state) => state.userGroups[state.currentGroupIndex] || null,
    isGroupViewed: (state) => (groupId) => {
      const group = state.userGroups.find(g => g.userId === groupId)
      if (!group) return false
      return group.stories.every(s => state.viewedMap[s.id])
    },
  },
  actions: {
    async fetchStories(userId) {
      const res = await storyApi.getByUser(userId)
      const enriched = (res.data || []).map(s => enrichStory(s, mockUsers))
      this.userGroups = groupByUser(enriched)
      const ownIdx = this.userGroups.findIndex(g => g.userId === Number(userId))
      if (ownIdx > 0) {
        const own = this.userGroups.splice(ownIdx, 1)
        this.userGroups.unshift(own[0])
      }
    },
    openViewer(groupIndex) {
      this.currentGroupIndex = groupIndex
      this.viewerVisible = true
    },
    closeViewer() {
      this.viewerVisible = false
      this.currentGroupIndex = -1
    },
    markViewed(storyId) {
      this.viewedMap[storyId] = true
    },
    nextGroup() {
      if (this.currentGroupIndex < this.userGroups.length - 1) {
        this.currentGroupIndex++
      } else {
        this.closeViewer()
      }
    },
  },
})
```

- [ ] **Step 2: Build verification**

Run: `npm run build` from `blog-view/`

---

### Task 5: Leaf Components — StoryProgress + StoryImage + StoryVideo + StoryHeader + StoryReaction + StoryReply + StoryMenu

**Files:**
- Create: `src/components/story/StoryProgress.vue`
- Create: `src/components/story/StoryImage.vue`
- Create: `src/components/story/StoryVideo.vue`
- Create: `src/components/story/StoryHeader.vue`
- Create: `src/components/story/StoryReaction.vue`
- Create: `src/components/story/StoryReply.vue`
- Create: `src/components/story/StoryMenu.vue`

- [ ] **Step 1: Create `StoryProgress.vue`**

```vue
<template>
  <div class="sp-bar">
    <div v-for="(_, i) in group.stories" :key="i" class="sp-seg">
      <div class="sp-fill" :class="{ done: i < currentIndex, active: i === currentIndex }">
        <div v-if="i === currentIndex" class="sp-anim" :class="{ paused }" :style="{ '--sp-dur': group.stories[i].durationMs + 'ms' }"></div>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({ group: { type: Object, required: true }, currentIndex: { type: Number, default: 0 }, paused: { type: Boolean, default: false } })
</script>

<style scoped lang="scss">
.sp-bar { display: flex; gap: 3px; }
.sp-seg { flex: 1; height: 2px; background: rgba(255,255,255,0.25); border-radius: 99px; overflow: hidden; }
.sp-fill { height: 100%; width: 0; }
.sp-fill.done, .sp-fill.active { width: 100%; background: rgba(255,255,255,0.7); }
.sp-anim { height: 100%; width: 0; background: #fff; animation: spAnim var(--sp-dur, 5s) linear forwards; }
.sp-anim.paused { animation-play-state: paused; }
@keyframes spAnim { from { width: 0; } to { width: 100%; } }
</style>
```

- [ ] **Step 2: Create `StoryImage.vue`**

```vue
<template>
  <div class="si-wrap" :class="{ 'si-active': isActive }">
    <div v-if="loading" class="si-spinner"><div class="spinner-ring"></div></div>
    <img v-show="!loading" :src="story.mediaUrl" class="si-img" @load="loading = false; $emit('loaded')" @error="loading = false; errored = true" :class="{ 'si-ken': isActive }" />
    <div v-if="errored" class="si-error">Không thể tải</div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
const props = defineProps({ story: { type: Object, required: true }, isActive: { type: Boolean, default: false } })
defineEmits(['loaded'])
const loading = ref(true)
const errored = ref(false)
watch(() => props.story?.id, () => { loading.value = true; errored.value = false })
</script>

<style scoped lang="scss">
.si-wrap { width: 100%; aspect-ratio: 9/16; background: #1e293b; border-radius: 8px; overflow: hidden; display: flex; align-items: center; justify-content: center; }
.si-img { width: 100%; height: 100%; object-fit: cover; display: block; }
.si-ken { animation: siKen var(--sp-dur, 10s) ease-in-out forwards; transform-origin: center; }
@keyframes siKen { from { transform: scale(1); } to { transform: scale(1.08); } }
.si-spinner { position: absolute; inset: 0; display: flex; align-items: center; justify-content: center; }
.spinner-ring { width: 40px; height: 40px; border: 3px solid rgba(255,255,255,0.15); border-top-color: #fff; border-radius: 50%; animation: spin 0.7s linear infinite; }
@keyframes spin { to { transform: rotate(360deg); } }
.si-error { color: rgba(255,255,255,0.5); font-size: 0.85rem; }
</style>
```

- [ ] **Step 3: Create `StoryVideo.vue`**

```vue
<template>
  <div class="sv-wrap" ref="wrapRef">
    <div v-if="loading" class="sv-spinner"><div class="spinner-ring"></div></div>
    <video ref="videoRef" :src="story.mediaUrl" muted playsinline class="sv-video" :class="{ 'sv-hidden': loading }" @canplay="onReady" @ended="$emit('ended')" @error="loading = false; errored = true"></video>
    <button v-if="loading" class="sv-mute-toggle" @click="toggleMute">{{ muted ? '🔇' : '🔊' }}</button>
    <div v-if="errored" class="sv-error">Không thể tải</div>
  </div>
</template>

<script setup>
import { ref, watch, onUnmounted, nextTick } from 'vue'
const props = defineProps({ story: { type: Object, required: true }, isActive: { type: Boolean, default: false } })
const emit = defineEmits(['loaded', 'ended'])
const videoRef = ref(null)
const wrapRef = ref(null)
const loading = ref(true)
const errored = ref(false)
const muted = ref(true)

const toggleMute = () => { if (!videoRef.value) return; muted.value = !muted.value; videoRef.value.muted = muted.value }
const onReady = () => { loading.value = false; emit('loaded'); videoRef.value?.play() }

const handleVisibility = () => { if (document.hidden && videoRef.value) videoRef.value.pause(); else if (!document.hidden && videoRef.value && props.isActive) videoRef.value.play() }

watch(() => props.story?.id, () => { loading.value = true; errored.value = false })
watch(() => props.isActive, (v) => { if (v && videoRef.value) videoRef.value.play(); else if (!v && videoRef.value) videoRef.value.pause() })

let visHandler = null
watch(() => props.isActive, (v) => {
  if (v) { document.addEventListener('visibilitychange', handleVisibility) }
  else { document.removeEventListener('visibilitychange', handleVisibility) }
}, { immediate: true })

onUnmounted(() => { document.removeEventListener('visibilitychange', handleVisibility); if (videoRef.value) { videoRef.value.pause(); videoRef.value.src = '' } })
</script>

<style scoped lang="scss">
.sv-wrap { width: 100%; aspect-ratio: 9/16; background: #000; border-radius: 8px; overflow: hidden; position: relative; display: flex; align-items: center; justify-content: center; }
.sv-video { width: 100%; height: 100%; object-fit: cover; }
.sv-hidden { display: none; }
.sv-spinner { position: absolute; inset: 0; display: flex; align-items: center; justify-content: center; }
.sv-mute-toggle { position: absolute; bottom: 16px; right: 16px; background: rgba(0,0,0,0.5); border: none; border-radius: 50%; width: 36px; height: 36px; cursor: pointer; font-size: 1rem; display: flex; align-items: center; justify-content: center; }
.sv-error { color: rgba(255,255,255,0.5); font-size: 0.85rem; }
</style>
```

- [ ] **Step 4: Create `StoryHeader.vue`**

```vue
<template>
  <div class="sh-row">
    <img :src="story.userAvatar || 'https://api.dicebear.com/7.x/avataaars/svg?seed=guest'" class="sh-avatar" />
    <div class="sh-meta">
      <span class="sh-name">{{ story.userName }}</span>
      <span class="sh-time">{{ fromNow(story.createdAt) }}</span>
    </div>
    <button class="sh-close" @click="$emit('close')">✕</button>
  </div>
</template>

<script setup>
import { fromNow } from '@/utils/time'
defineProps({ story: { type: Object, required: true } })
defineEmits(['close'])
</script>

<style scoped lang="scss">
.sh-row { display: flex; align-items: center; gap: 10px; padding: 0 4px; }
.sh-avatar { width: 32px; height: 32px; border-radius: 50%; object-fit: cover; border: 2px solid rgba(255,255,255,0.3); }
.sh-meta { flex: 1; min-width: 0; display: flex; flex-direction: column; }
.sh-name { font-size: 0.82rem; font-weight: 600; color: #fff; }
.sh-time { font-size: 0.65rem; color: rgba(255,255,255,0.55); }
.sh-close { background: none; border: none; color: rgba(255,255,255,0.7); font-size: 1.4rem; cursor: pointer; padding: 4px; line-height: 1; }
.sh-close:hover { color: #fff; }
</style>
```

- [ ] **Step 5: Create `StoryReaction.vue`**

```vue
<template>
  <div class="sr-picker">
    <button v-for="r in reactions" :key="r" class="sr-btn" @click="$emit('react', r)">{{ r }}</button>
  </div>
</template>

<script setup>
defineEmits(['react'])
const reactions = ['❤️', '😂', '😮', '😢', '😡']
</script>

<style scoped lang="scss">
.sr-picker { display: flex; gap: 6px; }
.sr-btn { background: rgba(255,255,255,0.1); border: none; border-radius: 50%; width: 36px; height: 36px; font-size: 1.2rem; cursor: pointer; transition: transform 0.15s; display: flex; align-items: center; justify-content: center; }
.sr-btn:hover { transform: scale(1.25); background: rgba(255,255,255,0.2); }
</style>
```

- [ ] **Step 6: Create `StoryReply.vue`**

```vue
<template>
  <div class="srp-row">
    <input v-model="text" class="srp-input" placeholder="Trả lời..." @keydown.enter="send" maxlength="200" />
    <button class="srp-send" :disabled="!text.trim()" @click="send">Gửi</button>
  </div>
</template>

<script setup>
import { ref } from 'vue'
const emit = defineEmits(['reply'])
const text = ref('')
const send = () => { if (!text.value.trim()) return; emit('reply', text.value.trim()); text.value = '' }
</script>

<style scoped lang="scss">
.srp-row { display: flex; gap: 6px; flex: 1; }
.srp-input { flex: 1; background: rgba(255,255,255,0.1); border: none; border-radius: 20px; padding: 8px 14px; color: #fff; font-size: 0.82rem; outline: none; }
.srp-input::placeholder { color: rgba(255,255,255,0.4); }
.srp-send { background: none; border: none; color: #60a5fa; font-size: 0.85rem; font-weight: 600; cursor: pointer; padding: 0 8px; }
.srp-send:disabled { opacity: 0.4; cursor: default; }
</style>
```

- [ ] **Step 7: Create `StoryMenu.vue`**

```vue
<template>
  <div class="sm-wrap">
    <button class="sm-trigger" @click.stop="open = !open">•••</button>
    <div v-if="open" class="sm-dropdown" @click.stop>
      <button class="sm-item" @click="act('report')">Báo cáo</button>
      <button class="sm-item" @click="act('share')">Chia sẻ</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
const emit = defineEmits(['action'])
const open = ref(false)
const act = (a) => { open.value = false; emit('action', a) }
</script>

<style scoped lang="scss">
.sm-wrap { position: relative; }
.sm-trigger { background: none; border: none; color: rgba(255,255,255,0.7); font-size: 1.2rem; cursor: pointer; padding: 4px; letter-spacing: 2px; }
.sm-dropdown { position: absolute; bottom: 100%; right: 0; background: #1e293b; border-radius: 8px; overflow: hidden; min-width: 120px; margin-bottom: 4px; }
.sm-item { display: block; width: 100%; padding: 10px 16px; background: none; border: none; color: #fff; font-size: 0.82rem; text-align: left; cursor: pointer; }
.sm-item:hover { background: rgba(255,255,255,0.08); }
</style>
```

- [ ] **Step 8: Build verification**

Run: `npm run build` from `blog-view/`

---

### Task 6: Compound Components — StoryFooter + StoryNavigation

**Files:**
- Create: `src/components/story/StoryFooter.vue`
- Create: `src/components/story/StoryNavigation.vue`

- [ ] **Step 1: Create `StoryFooter.vue`**

```vue
<template>
  <div class="sf-row">
    <StoryReply @reply="$emit('reply', $event)" />
    <StoryReaction @react="$emit('react', $event)" />
    <button class="sf-share" @click="$emit('share')">↗</button>
    <StoryMenu @action="$emit('menu', $event)" />
  </div>
</template>

<script setup>
import StoryReply from './StoryReply.vue'
import StoryReaction from './StoryReaction.vue'
import StoryMenu from './StoryMenu.vue'
defineEmits(['reply', 'react', 'share', 'menu'])
</script>

<style scoped lang="scss">
.sf-row { display: flex; align-items: center; gap: 8px; padding: 8px 4px; }
.sf-share { background: none; border: none; color: rgba(255,255,255,0.7); font-size: 1.2rem; cursor: pointer; padding: 4px; }
.sf-share:hover { color: #fff; }
</style>
```

- [ ] **Step 2: Create `StoryNavigation.vue`**

```vue
<template>
  <div class="sn-overlay" @touchstart="onTouchStart" @touchmove="onTouchMove" @touchend="onTouchEnd">
    <div class="sn-zone sn-prev" @click.stop="$emit('prev')"></div>
    <div class="sn-zone sn-pause" @click.stop="$emit('togglePause')"></div>
    <div class="sn-zone sn-next" @click.stop="$emit('next')"></div>
  </div>
</template>

<script setup>
import { useStoryGesture } from '@/composables/useStoryGesture'
const emit = defineEmits(['prev', 'next', 'togglePause', 'close'])
const { direction, onTouchStart, onTouchMove, onTouchEnd } = useStoryGesture()

const handleTouchEnd = (e) => {
  onTouchEnd(e)
  if (direction.value === 'left') emit('next')
  else if (direction.value === 'right') emit('prev')
  else if (direction.value === 'down') emit('close')
}
</script>

<style scoped lang="scss">
.sn-overlay { position: absolute; inset: 0; display: flex; z-index: 5; }
.sn-zone { height: 100%; }
.sn-prev { flex: 0 0 30%; }
.sn-pause { flex: 0 0 40%; }
.sn-next { flex: 0 0 30%; }
</style>
```

- [ ] **Step 3: Build verification**

Run: `npm run build` from `blog-view/`

---

### Task 7: StoryCard + StoryList

**Files:**
- Create: `src/components/story/StoryCard.vue`
- Create: `src/components/story/StoryList.vue`

- [ ] **Step 1: Create `StoryCard.vue`**

```vue
<template>
  <div class="sc-card" @click="$emit('click')">
    <div class="sc-ring" :class="{ viewed: isViewed, own: isOwn }">
      <img v-if="group.userAvatar" :src="group.userAvatar" class="sc-avatar" />
      <div v-else class="sc-avatar-placeholder">{{ group.userName?.charAt(0) || '?' }}</div>
    </div>
    <span class="sc-name">{{ group.userName }}</span>
  </div>
</template>

<script setup>
defineProps({ group: { type: Object, required: true }, isViewed: { type: Boolean, default: false }, isOwn: { type: Boolean, default: false } })
defineEmits(['click'])
</script>

<style scoped lang="scss">
.sc-card { display: flex; flex-direction: column; align-items: center; gap: 4px; cursor: pointer; flex-shrink: 0; width: 64px; }
.sc-ring { width: 56px; height: 56px; border-radius: 50%; padding: 2px; background: linear-gradient(135deg, #0ea5e9, #8b5cf6); display: flex; align-items: center; justify-content: center; }
.sc-ring.viewed { background: #475569; }
.sc-ring.own { background: linear-gradient(135deg, #f59e0b, #ec4899); }
.sc-avatar { width: 100%; height: 100%; border-radius: 50%; object-fit: cover; border: 2px solid var(--surface, #fff); }
.sc-avatar-placeholder { width: 100%; height: 100%; border-radius: 50%; background: #334155; display: flex; align-items: center; justify-content: center; color: #fff; font-weight: 700; font-size: 1.1rem; border: 2px solid var(--surface, #fff); }
.sc-name { font-size: 0.65rem; color: var(--text-muted, #94a3b8); max-width: 64px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; text-align: center; }
</style>
```

- [ ] **Step 2: Create `StoryList.vue`**

```vue
<template>
  <div v-if="store.userGroups.length" class="sl-strip">
    <div class="sl-scroll" ref="scrollRef">
      <StoryCard v-for="(g, i) in store.userGroups" :key="g.userId" :group="g" :is-viewed="store.isGroupViewed(g.userId)" :is-own="g.userId === ownUserId" @click="store.openViewer(i)" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useStoryStore } from '@/stores/storyStore'
import StoryCard from './StoryCard.vue'

const store = useStoryStore()
const scrollRef = ref(null)
const ownUserId = ref(null)

onMounted(async () => {
  const authStore = (await import('@/stores/authStore')).useAuthStore()
  ownUserId.value = authStore.user?.id || null
  await store.fetchStories(ownUserId.value)
})
</script>

<style scoped lang="scss">
.sl-strip { margin-bottom: var(--space-md, 16px); }
.sl-scroll { display: flex; gap: 12px; overflow-x: auto; padding: 8px 0; scrollbar-width: none; }
.sl-scroll::-webkit-scrollbar { display: none; }
</style>
```

- [ ] **Step 3: Build verification**

Run: `npm run build` from `blog-view/`

---

### Task 8: StoryViewer (Orchestrator)

**Files:**
- Create: `src/components/story/StoryViewer.vue`

- [ ] **Step 1: Create `StoryViewer.vue`**

```vue
<template>
  <Teleport to="body">
    <Transition name="sv-overlay">
      <div v-if="store.viewerVisible" class="sv-overlay" @click.self="store.closeViewer" @keydown="onKey" tabindex="-1" ref="overlayRef">
        <Transition name="sv-panel" appear>
          <div v-if="player.currentStory" class="sv-panel" @mousedown="player.pause()" @mouseup="player.play()" @mouseleave="player.play()">
            <StoryProgress :group="player.currentGroup" :current-index="player.currentIndex" :paused="player.paused" />
            <StoryHeader :story="player.currentStory" @close="store.closeViewer" />
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

const player = reactive(useStoryPlayer(
  computed(() => store.userGroups),
  computed(() => store.currentGroupIndex),
  () => store.nextGroup(),
  () => store.closeViewer()
)

const didLoad = () => {
  if (player.currentStory.value) store.markViewed(player.currentStory.value.id)
  const nextStory = player.currentGroup.value?.stories?.[player.currentIndex.value + 1]
  if (nextStory?.mediaUrl) preload(nextStory.mediaUrl)
}

useKeyboard({
  Escape: () => store.closeViewer(),
  ArrowLeft: () => player.goPrev(),
  ArrowRight: () => player.goNext(),
  ' ': () => player.togglePause(),
})

watch(() => store.viewerVisible, (v) => { if (v) nextTick(() => overlayRef.value?.focus()) })

const onReply = (text) => { /* placeholder */ }
const onReact = (type) => { /* placeholder */ }
const onShare = () => { /* placeholder */ }
const onMenu = (action) => { /* placeholder */ }

onUnmounted(destroyPreload)
</script>

<style scoped lang="scss">
.sv-overlay { position: fixed; inset: 0; z-index: 99999; background: rgba(0,0,0,0.92); display: flex; align-items: center; justify-content: center; outline: none; }
.sv-panel { position: relative; max-width: 420px; width: 92%; }
.sv-media { position: relative; border-radius: 8px; overflow: hidden; width: 100%; }

.sv-overlay-enter-active, .sv-overlay-leave-active { transition: opacity 0.3s ease; }
.sv-overlay-enter-from, .sv-overlay-leave-to { opacity: 0; }
.sv-panel-enter-active { transition: transform 0.3s ease, opacity 0.3s ease; }
.sv-panel-leave-active { transition: transform 0.2s ease, opacity 0.2s ease; }
.sv-panel-enter-from { transform: scale(0.95); opacity: 0; }
.sv-panel-leave-to { transform: scale(0.95); opacity: 0; }
</style>
```

- [ ] **Step 2: Build verification**

Run: `npm run build` from `blog-view/`

---

### Task 9: Integration — Replace Old in Home.vue + Blog.vue + Cleanup

**Files:**
- Modify: `src/view/home/Home.vue`
- Modify: `src/view/blog/Blog.vue`
- Delete: `src/components/blog/StoryViewer.vue`
- Verify: `src/api/story.js` compatible (should be — already enriches)

- [ ] **Step 1: Update `Home.vue` — replace old story strip + viewer with new StoryList + StoryViewer**

Remove old story strip template (lines ~92-102) and old StoryViewer import/usage. Add new imports:

Template:
```html
<StoryList />
<StoryViewer />
```

Script:
```js
import StoryList from '@/components/story/StoryList.vue'
import StoryViewer from '@/components/story/StoryViewer.vue'
// remove old import: import StoryViewer from '@/components/blog/StoryViewer.vue'
// remove: const stories = ref([]), viewerVisible, viewerIndex, openStoryViewer, closeStoryViewer
// remove: import { storyApi } ... and loadStories()
```

- [ ] **Step 2: Update `Blog.vue` — replace old StoryViewer with new one**

Remove old:
```html
<StoryViewer :visible="showStoryViewer" :stories="authorStories" :initial-index="storyIndex" @close="showStoryViewer = false" />
```

Add:
```html
<StoryList />
<StoryViewer />
```

Remove old import `import StoryViewer from '@/components/blog/StoryViewer.vue'`, add new imports `import StoryList from '@/components/story/StoryList.vue'; import StoryViewer from '@/components/story/StoryViewer.vue'`

Remove `showStoryViewer`, `storyIndex`, `authorStories`, `viewStory` function etc. if no longer needed.

- [ ] **Step 3: Delete old `src/components/blog/StoryViewer.vue`**

- [ ] **Step 4: Build verification**

Run: `npm run build` from `blog-view/`
Expected: ✓ built in Xs

---
