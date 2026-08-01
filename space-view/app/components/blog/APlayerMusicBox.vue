<template>
  <ClientOnly>
    <div class="aplayer-music-box" :class="{ compact }">
      <div ref="containerRef" class="aplayer-wrap"></div>

      <div class="plugin-toolbar" v-if="toolbarPlugins.length">
        <component v-for="p in toolbarPlugins" :is="p.toolbarActions" :key="p.name" />
      </div>

      <div v-if="!compact && playlist?.songs?.length" class="music-song-list">
        <div v-for="(song, idx) in playlist.songs" :key="song.id" :class="['song-item', { active: idx === currentIndex }]" @click="switchTo(idx)">
          <div class="song-idx">{{ idx + 1 }}</div>
          <div class="song-info">
            <span class="song-title">{{ song.title }}</span>
            <span class="song-artist">{{ song.artist }}</span>
          </div>
          <component v-for="p in songActionPlugins" :is="p.songActions" :key="p.name" :song="song" :index="idx" />
          <span class="song-dur">{{ formatDuration(song.durationSec) }}</span>
        </div>
      </div>

      <component v-for="p in addSongPlugins" :is="p.addSongSection" :key="'add-' + p.name" />
    </div>
    <template #fallback>
      <div class="aplayer-music-box aplayer-loading" />
    </template>
  </ClientOnly>
</template>

<script setup lang="ts">
const APlayer = await import('aplayer').then(m => m.default)

const props = defineProps({
  playlist: Object,
  isLoggedIn: { type: Boolean, default: false },
  compact: { type: Boolean, default: false },
  plugins: { type: Array, default: () => [] },
})

const emit = defineEmits(['songChange'])
const containerRef = ref<HTMLElement | null>(null)
const currentIndex = ref(0)
const isPlaying = ref(false)
const apRef = ref<any>(null)

const currentSong = computed(() => props.playlist?.songs?.[currentIndex.value] || null)

provide('playerContext', {
  ap: apRef,
  currentIndex,
  currentSong,
  isPlaying,
  playlist: computed(() => props.playlist),
  isLoggedIn: computed(() => props.isLoggedIn),
})

const resolveSource = (song: any) => {
  for (const p of props.plugins as any[]) {
    if (p.resolveSource) {
      const url = p.resolveSource(song)
      if (url) return url
    }
  }
  return song.audioUrl || song.sourceUrl || ''
}

const buildAudioList = () => (props.playlist?.songs || []).map((song: any) => ({
  name: song.title,
  artist: song.artist,
  url: resolveSource(song),
  cover: song.thumbnailUrl,
}))

const initPlayer = async () => {
  await nextTick()
  if (!containerRef.value) return

  if (apRef.value) {
    apRef.value.destroy()
    apRef.value = null
  }

  const theme = getComputedStyle(document.documentElement).getPropertyValue('--primary').trim() || '#0ea5e9'

  apRef.value = new APlayer({
    container: containerRef.value,
    audio: buildAudioList(),
    mini: false,
    fixed: false,
    theme,
    loop: 'all',
    order: 'list',
    preload: 'metadata',
    volume: 0.7,
    mutex: true,
    listFolded: true,
    listMaxHeight: 0,
    storageName: 'aplayer-music',
  })

  apRef.value.on('play', () => {
    isPlaying.value = true
    ;(props.plugins as any[]).forEach(p => p.onPlay?.(currentSong.value))
  })
  apRef.value.on('pause', () => {
    isPlaying.value = false
    ;(props.plugins as any[]).forEach(p => p.onPause?.(currentSong.value))
  })
  apRef.value.on('listswitch', (index: number) => {
    currentIndex.value = index
    emit('songChange', currentSong.value)
    nextTick(() => (props.plugins as any[]).forEach(p => p.onListSwitch?.(index, currentSong.value)))
  })
  apRef.value.on('error', (e: any) => (props.plugins as any[]).forEach(p => p.onError?.(e)))

  ;(props.plugins as any[]).forEach(p => p.onInit?.(apRef.value))
}

onMounted(initPlayer)

watch(() => props.playlist, () => {
  initPlayer()
}, { deep: false })

onUnmounted(() => {
  if (apRef.value) {
    apRef.value.destroy()
    apRef.value = null
  }
})

const switchTo = (index: number) => apRef.value?.list?.switch(index)

const formatDuration = (s: number) => {
  const m = Math.floor(s / 60)
  return `${m}:${String(s % 60).padStart(2, '0')}`
}

const songActionPlugins = computed(() => (props.plugins as any[]).filter(p => p.songActions))
const toolbarPlugins = computed(() => (props.plugins as any[]).filter(p => p.toolbarActions))
const addSongPlugins = computed(() => (props.plugins as any[]).filter(p => p.addSongSection))
</script>

<style lang="scss" scoped>
.aplayer-music-box {
  width: 100%;

  :deep(.aplayer) {
    box-shadow: none;
    border-radius: var(--radius);
    background: var(--surface);
    border: 1px solid var(--border-light);
  }

  :deep(.aplayer .aplayer-list) {
    display: none;
  }

  :deep(.aplayer .aplayer-info) {
    border-bottom: none;
  }

  :deep(.aplayer .aplayer-lrc) {
    display: none;
  }

  &.compact {
    :deep(.aplayer) {
      max-height: 60px;
      overflow: hidden;
    }
    :deep(.aplayer .aplayer-body) {
      position: static;
    }
    :deep(.aplayer .aplayer-pic) {
      width: 60px;
      height: 60px;
    }
    :deep(.aplayer .aplayer-info) {
      margin-left: 60px;
      height: 60px;
      padding: 6px 10px 0;
    }
    :deep(.aplayer .aplayer-info .aplayer-music) {
      margin-bottom: 2px;
    }
    :deep(.aplayer .aplayer-info .aplayer-music .aplayer-title) {
      font-size: 0.82rem;
    }
    :deep(.aplayer .aplayer-info .aplayer-music .aplayer-author) {
      font-size: 0.7rem;
    }
    :deep(.aplayer .aplayer-info .aplayer-controller) {
      position: static;
      margin-top: 2px;
    }
    :deep(.aplayer .aplayer-info .aplayer-controller .aplayer-bar-wrap) {
      margin: 0;
    }
    :deep(.aplayer .aplayer-info .aplayer-controller .aplayer-bar-wrap .aplayer-bar) {
      height: 3px;
    }
    :deep(.aplayer .aplayer-info .aplayer-controller .aplayer-bar-wrap .aplayer-bar .aplayer-loaded) {
      height: 3px;
    }
    :deep(.aplayer .aplayer-info .aplayer-controller .aplayer-bar-wrap .aplayer-bar .aplayer-played) {
      height: 3px;
    }
    :deep(.aplayer .aplayer-info .aplayer-controller .aplayer-time) {
      position: absolute;
      right: 8px;
      top: 4px;
      font-size: 0.65rem;
    }
  }
}

.aplayer-loading {
  height: 60px;
  background: var(--bg-secondary);
  border-radius: var(--radius);
}

.music-song-list {
  border: 1px solid var(--border-light);
  border-top: none;
  border-radius: 0 0 var(--radius) var(--radius);
  background: var(--surface);
  overflow: hidden;
  max-height: 280px;
  overflow-y: auto;
}

.song-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  cursor: pointer;
  transition: background 0.15s ease;

  &:hover { background: var(--surface-hover); }
  &.active { background: var(--primary-50); }
}

.song-idx {
  width: 20px;
  font-size: 0.7rem;
  color: var(--text-muted);
  text-align: center;
  flex-shrink: 0;
}

.song-info {
  flex: 1;
  min-width: 0;
}

.song-title {
  display: block;
  font-size: 0.82rem;
  font-weight: 600;
  color: var(--text-primary);
}

.song-artist {
  font-size: 0.7rem;
  color: var(--text-muted);
}

.song-dur {
  font-size: 0.68rem;
  color: var(--text-muted);
  flex-shrink: 0;
  margin-left: auto;
}

.plugin-toolbar {
  display: flex;
  gap: 4px;
  margin-top: 4px;
}

:deep(.vote-actions) {
  display: flex;
  align-items: center;
  gap: 2px;
  flex-shrink: 0;
}

:deep(.vote-btn) {
  background: none;
  border: none;
  cursor: pointer;
  padding: 2px 4px;
  font-size: 0.65rem;
  line-height: 1;
  color: var(--text-muted);
  display: flex;
  align-items: center;
  transition: color 0.15s ease;

  &:hover { color: var(--primary); }
}

:deep(.vote-up:hover) { color: var(--success); }
:deep(.vote-down:hover) { color: var(--danger); }

:deep(.vote-count) {
  font-size: 0.68rem;
  font-weight: 600;
  min-width: 18px;
  text-align: center;
  color: var(--text-muted);

  &.hot { color: var(--success); }
  &.cold { color: var(--danger); }
}

:deep(.playlist-manager-section) {
  border-top: 1px solid var(--border-light);
  padding: 8px 0;
  margin-top: 4px;
}

:deep(.add-song-btn) {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  width: 100%;
  padding: 6px 10px;
  background: var(--primary-50);
  color: var(--primary);
  border: none;
  border-radius: var(--radius-sm);
  cursor: pointer;
  font-size: 0.78rem;
  font-weight: 600;
  transition: all 0.15s ease;

  &:hover { background: var(--primary-100); }
}

:deep(.add-song-form) {
  display: flex;
  flex-direction: column;
  gap: 6px;
  padding: 8px 0;
}

:deep(.song-input) {
  padding: 6px 10px;
  border: 1px solid var(--border-light);
  border-radius: var(--radius-sm);
  font-size: 0.78rem;
  background: var(--bg-secondary);
  color: var(--text-primary);

  &:focus { outline: none; border-color: var(--primary); }
}

:deep(.add-song-actions) {
  display: flex;
  gap: 6px;
  justify-content: flex-end;
}
</style>
