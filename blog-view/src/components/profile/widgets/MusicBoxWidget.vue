<template>
  <div class="widget-card">
    <div class="widget-header"><span class="widget-title">Âm nhạc</span></div>
    <div class="widget-body">
      <div class="music-info">
        <img :src="currentSong?.thumbnailUrl || defaultCover" class="music-cover" />
        <div class="music-meta">
          <span class="music-title">{{ currentSong?.title || 'Chưa có bài hát' }}</span>
          <span class="music-artist">{{ currentSong?.artist || '' }}</span>
        </div>
      </div>
      <div class="music-controls">
        <button class="ctrl-btn" @click="prevSong"><svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor"><path d="M19 20L9 12l10-8v16zM5 19V5h2v14H5z"/></svg></button>
        <button class="ctrl-btn play-btn" @click="togglePlay">
          <svg v-if="!isPlaying" width="20" height="20" viewBox="0 0 24 24" fill="currentColor"><path d="M8 5v14l11-7z"/></svg>
          <svg v-else width="20" height="20" viewBox="0 0 24 24" fill="currentColor"><path d="M6 4h4v16H6V4zm8 0h4v16h-4V4z"/></svg>
        </button>
        <button class="ctrl-btn" @click="nextSong"><svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor"><path d="M5 4l10 8-10 8V4zM17 5v14h2V5h-2z"/></svg></button>
      </div>
      <div class="youtube-embed" v-if="isPlaying && currentSong?.source === 'youtube'">
        <iframe
          :key="currentSong?.id"
          :src="`https://www.youtube.com/embed/${currentSong?.sourceId}?autoplay=1&enablejsapi=1`"
          width="100%" height="160"
          frameborder="0"
          allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
          allowfullscreen
        ></iframe>
      </div>
      <div class="playlist-songs" v-if="songs.length">
        <div v-for="(song, idx) in songs" :key="song.id" :class="['song-item', { active: idx === currentIndex }]" @click="playSong(idx)">
          <span class="song-idx">{{ idx + 1 }}</span>
          <div class="song-info">
            <span class="song-title">{{ song.title }}</span>
            <span class="song-artist">{{ song.artist }}</span>
          </div>
        </div>
      </div>
      <div v-if="!songs.length" class="empty-state">Chưa có danh sách phát</div>
    </div>
  </div>
</template>
<script setup>
import { ref, computed } from 'vue'
const props = defineProps({
  playlist: { type: Object, default: null },
  equippedItems: { type: Object, default: () => ({}) },
})
const defaultCover = 'https://picsum.photos/seed/default/100/100'
const songs = computed(() => props.playlist?.songs || [])
const currentIndex = ref(0)
const isPlaying = ref(false)
const currentSong = computed(() => songs.value[currentIndex.value] || null)
const togglePlay = () => { isPlaying.value = !isPlaying.value }
const nextSong = () => { currentIndex.value = (currentIndex.value + 1) % (songs.value.length || 1) }
const prevSong = () => { currentIndex.value = (currentIndex.value - 1 + (songs.value.length || 1)) % (songs.value.length || 1) }
const playSong = (idx) => { currentIndex.value = idx; isPlaying.value = true }
</script>
<style scoped lang="scss">
.widget-card { background: var(--surface); border-radius: var(--radius-xl); border: 1px solid var(--border-light); overflow: hidden; }
.widget-header { padding: 14px 16px; border-bottom: 1px solid var(--border-light); }
.widget-title { font-size: 0.85rem; font-weight: 700; }
.widget-body { padding: 12px 16px; }
.music-info { display: flex; align-items: center; gap: 10px; margin-bottom: 12px; }
.music-cover { width: 48px; height: 48px; border-radius: var(--radius); object-fit: cover; }
.music-meta { flex: 1; }
.music-title { display: block; font-size: 0.82rem; font-weight: 600; }
.music-artist { font-size: 0.72rem; color: var(--text-muted); }
.music-controls { display: flex; justify-content: center; align-items: center; gap: 16px; margin-bottom: 10px; }
.ctrl-btn { background: none; border: none; cursor: pointer; color: var(--text-secondary); padding: 4px; display: flex; align-items: center; justify-content: center; }
.ctrl-btn:hover { color: var(--primary); }
.play-btn { width: 36px; height: 36px; border-radius: 50%; background: var(--primary); color: white; }
.play-btn:hover { background: var(--primary-dark); color: white; }
.playlist-songs { max-height: 160px; overflow-y: auto; }
.song-item { display: flex; align-items: center; gap: 8px; padding: 6px 4px; border-radius: var(--radius-sm); cursor: pointer; transition: background 0.15s ease; }
.song-item:hover { background: var(--surface-hover); }
.song-item.active { background: var(--primary-50); }
.song-idx { width: 20px; font-size: 0.7rem; color: var(--text-muted); text-align: center; }
.song-info { flex: 1; }
.song-title { display: block; font-size: 0.75rem; font-weight: 500; }
.song-artist { font-size: 0.65rem; color: var(--text-muted); }
.empty-state { font-size: 0.8rem; color: var(--text-muted); text-align: center; padding: 12px 0; }
.youtube-embed { margin-bottom: 8px; border-radius: var(--radius); overflow: hidden; }
.youtube-embed iframe { display: block; border-radius: var(--radius); }
</style>
