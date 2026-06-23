<template>
  <div class="sidebar-card music-box" v-if="playlist">
    <div class="card-header">
      <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M9 18V5l12-2v13"/><circle cx="6" cy="18" r="3"/><circle cx="18" cy="16" r="3"/></svg>
      <span>Nhạc nền</span>
    </div>
    <div class="music-info">
      <img :src="currentSong?.coverUrl || ''" class="music-cover" />
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
    <div class="music-progress">
      <div class="progress-track"><div class="progress-fill" :style="{ width: '45%' }"></div></div>
      <div class="music-time"><span>1:30</span><span>3:45</span></div>
    </div>
    <div class="music-volume">
      <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polygon points="11 5 6 9 2 9 2 15 6 15 11 19 11 5"/><path d="M19.07 4.93a10 10 0 0 1 0 14.14M15.54 8.46a5 5 0 0 1 0 7.07"/></svg>
      <div class="volume-track"><div class="volume-fill" :style="{ width: '70%' }"></div></div>
    </div>
    <div class="playlist-toggle" @click="showPlaylist = !showPlaylist">
      <span>Danh sách phát ({{ playlist.songs?.length || 0 }})</span>
      <svg :class="{ rotated: showPlaylist }" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="6 9 12 15 18 9"/></svg>
    </div>
    <div class="playlist-songs" v-if="showPlaylist">
      <div v-for="(song, idx) in playlist.songs" :key="song.id" :class="['song-item', { active: idx === currentIndex }]" @click="playSong(idx)">
        <span class="song-idx">{{ idx + 1 }}</span>
        <div class="song-info">
          <span class="song-title">{{ song.title }}</span>
          <span class="song-artist">{{ song.artist }}</span>
        </div>
        <span class="song-dur">{{ formatDuration(song.duration) }}</span>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, computed } from 'vue'
const props = defineProps({ playlist: Object })
const currentIndex = ref(0)
const isPlaying = ref(false)
const showPlaylist = ref(false)
const currentSong = computed(() => props.playlist?.songs?.[currentIndex.value] || null)
const togglePlay = () => { isPlaying.value = !isPlaying.value }
const nextSong = () => { if (props.playlist?.songs) currentIndex.value = (currentIndex.value + 1) % props.playlist.songs.length }
const prevSong = () => { if (props.playlist?.songs) currentIndex.value = (currentIndex.value - 1 + props.playlist.songs.length) % props.playlist.songs.length }
const playSong = (idx) => { currentIndex.value = idx; isPlaying.value = true }
const formatDuration = (s) => { const m = Math.floor(s / 60); const sec = s % 60; return `${m}:${String(sec).padStart(2, '0')}` }
</script>
<style scoped lang="scss">
.music-box { padding: 16px; }
.music-box .card-header { display: flex; align-items: center; gap: 8px; font-size: 0.85rem; font-weight: 600; margin-bottom: 12px; }
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
.music-progress { margin-bottom: 8px; }
.music-progress .progress-track { height: 3px; background: var(--bg-secondary); border-radius: 99px; }
.music-progress .progress-fill { height: 100%; background: var(--primary); border-radius: 99px; }
.music-time { display: flex; justify-content: space-between; font-size: 0.65rem; color: var(--text-muted); margin-top: 2px; }
.music-volume { display: flex; align-items: center; gap: 8px; margin-bottom: 10px; }
.volume-track { flex: 1; height: 3px; background: var(--bg-secondary); border-radius: 99px; }
.volume-fill { height: 100%; background: var(--text-muted); border-radius: 99px; }
.playlist-toggle { display: flex; justify-content: space-between; align-items: center; padding: 8px 0; cursor: pointer; font-size: 0.75rem; color: var(--text-muted); border-top: 1px solid var(--border-light); }
.playlist-toggle svg { transition: transform 0.2s ease; }
.playlist-toggle .rotated { transform: rotate(180deg); }
.playlist-songs { max-height: 160px; overflow-y: auto; }
.song-item { display: flex; align-items: center; gap: 8px; padding: 6px 4px; border-radius: var(--radius-sm); cursor: pointer; transition: background 0.15s ease; }
.song-item:hover { background: var(--surface-hover); }
.song-item.active { background: var(--primary-50); }
.song-idx { width: 20px; font-size: 0.7rem; color: var(--text-muted); text-align: center; }
.song-info { flex: 1; }
.song-title { display: block; font-size: 0.75rem; font-weight: 500; }
.song-artist { font-size: 0.65rem; color: var(--text-muted); }
.song-dur { font-size: 0.65rem; color: var(--text-muted); }
</style>
