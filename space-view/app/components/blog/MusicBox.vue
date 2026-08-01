<template>
  <div class="sidebar-card music-box" v-if="playlist">
    <div class="card-header">
      <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M9 18V5l12-2v13"/><circle cx="6" cy="18" r="3"/><circle cx="18" cy="16" r="3"/></svg>
      <span>Nhạc nền</span>
    </div>
    <div class="music-info">
      <img :src="currentSong?.thumbnailUrl || ''" class="music-cover" />
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
    <div class="secondary-controls" v-if="playlist.songs?.length">
      <button class="ctrl-btn small" @click="shufflePlaylist" :class="{ active: isShuffled }" title="Shuffle">
        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="16 3 21 3 21 8"/><line x1="4" y1="20" x2="21" y2="3"/><polyline points="21 16 21 21 16 21"/><line x1="15" y1="15" x2="21" y2="21"/><line x1="4" y1="4" x2="9" y2="9"/></svg>
      </button>
      <button class="ctrl-btn small" @click="loopPlaylist" :class="{ active: isLooping }" title="Loop">
        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="23 4 23 10 17 10"/><polyline points="1 20 1 14 7 14"/><path d="M3.51 9a9 9 0 0 1 14.85-3.36L23 10M1 14l4.64 4.36A9 9 0 0 0 20.49 15"/></svg>
      </button>
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
        <div class="song-votes">
          <button class="vote-up" :class="{ active: userVotes[song.id] === 1 }" @click.stop="voteSong(song.id, 1)">▲</button>
          <span class="vote-count" :class="{ hot: song.voteCount >= 5, cold: song.voteCount <= -3 }">{{ song.voteCount }}</span>
          <button class="vote-down" :class="{ active: userVotes[song.id] === -1 }" @click.stop="voteSong(song.id, -1)">▼</button>
        </div>
        <span v-if="song.voteCount <= -3" class="remove-warning" title="Sắp bị xóa">⚠️</span>
        <button v-if="isOwner" class="song-remove" @click.stop="removeSong(song.id)" title="Xóa">✕</button>
        <span class="song-dur">{{ formatDuration(song.durationSec) }}</span>
      </div>
    </div>
    <div class="add-song-section" v-if="isLoggedIn">
      <button class="add-song-btn" @click="showAddSong = !showAddSong">
        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/></svg>
        Thêm nhạc
      </button>
      <div class="add-song-form" v-if="showAddSong">
        <input v-model="newSong.title" placeholder="Tên bài hát" class="song-input" />
        <input v-model="newSong.artist" placeholder="Nghệ sĩ" class="song-input" />
        <select v-model="newSong.source" class="song-input">
          <option value="youtube">YouTube</option>
          <option value="soundcloud">SoundCloud</option>
        </select>
        <input v-model="newSong.sourceUrl" placeholder="URL" class="song-input" />
        <div class="add-song-actions">
          <el-button size="small" @click="submitSong" type="primary">Thêm</el-button>
          <el-button size="small" @click="showAddSong = false">Hủy</el-button>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
const props = defineProps({
  playlist: Object,
  isLoggedIn: { type: Boolean, default: false },
})
const currentIndex = ref(0)
const isPlaying = ref(false)
const showPlaylist = ref(false)
const isShuffled = ref(false)
const isLooping = ref(false)
const userVotes = ref<Record<number, number>>({})
const showAddSong = ref(false)
const newSong = ref({ title: '', artist: '', source: 'youtube', sourceUrl: '' })

const isOwner = computed(() => props.playlist?.ownerId === 1)

const currentSong = computed(() => props.playlist?.songs?.[currentIndex.value] || null)
const togglePlay = () => { isPlaying.value = !isPlaying.value }
const nextSong = () => {
  if (!props.playlist?.songs) return
  if (isShuffled.value) {
    currentIndex.value = Math.floor(Math.random() * props.playlist.songs.length)
  } else {
    currentIndex.value = (currentIndex.value + 1) % props.playlist.songs.length
  }
}
const prevSong = () => {
  if (!props.playlist?.songs) return
  if (isShuffled.value) {
    currentIndex.value = Math.floor(Math.random() * props.playlist.songs.length)
  } else {
    currentIndex.value = (currentIndex.value - 1 + props.playlist.songs.length) % props.playlist.songs.length
  }
}
const playSong = (idx: number) => { currentIndex.value = idx; isPlaying.value = true }
const formatDuration = (s: number) => { const m = Math.floor(s / 60); const sec = s % 60; return `${m}:${String(sec).padStart(2, '0')}` }
const shufflePlaylist = () => { isShuffled.value = !isShuffled.value }
const loopPlaylist = () => { isLooping.value = !isLooping.value }

const voteSong = (songId: number, vote: number) => {
  if (userVotes.value[songId] === vote) return
  userVotes.value[songId] = vote
  const song = props.playlist?.songs?.find((s: any) => s.id === songId)
  if (song) song.voteCount += vote * 2
}

const removeSong = (songId: number) => {
  if (!props.playlist?.songs) return
  const idx = props.playlist.songs.findIndex((s: any) => s.id === songId)
  if (idx >= 0) {
    props.playlist.songs.splice(idx, 1)
    if (currentIndex.value >= props.playlist.songs.length) {
      currentIndex.value = 0
    }
    ElMessage.success('Đã xóa bài hát')
  }
}

const submitSong = () => {
  if (!newSong.value.title || !newSong.value.artist) {
    ElMessage.warning('Vui lòng nhập tên bài hát và nghệ sĩ')
    return
  }
  const song = {
    id: Date.now(),
    playlistId: props.playlist?.id,
    addedBy: 1,
    title: newSong.value.title,
    artist: newSong.value.artist,
    source: 'youtube',
    sourceId: `song_${Date.now()}`,
    sourceUrl: '',
    thumbnailUrl: `https://picsum.photos/seed/music${Date.now()}/100/100`,
    durationSec: 180,
    sortOrder: props.playlist?.songs?.length || 0,
    voteCount: 0,
    isApproved: true,
    createdAt: new Date().toISOString(),
  }
  if (props.playlist?.songs) {
    props.playlist.songs.push(song)
  }
  showAddSong.value = false
  newSong.value = { title: '', artist: '', source: 'youtube', sourceUrl: '' }
  ElMessage.success('Đã thêm bài hát')
}
</script>
<style scoped lang="scss">
.music-box { padding: 16px; }
.music-box .card-header { display: flex; align-items: center; gap: 8px; font-size: 0.85rem; font-weight: 600; margin-bottom: 12px; }
.music-info { display: flex; align-items: center; gap: 10px; margin-bottom: 12px; }
.music-cover { width: 48px; height: 48px; border-radius: var(--radius); object-fit: cover; }
.music-meta { flex: 1; }
.music-title { display: block; font-size: 0.82rem; font-weight: 600; }
.music-artist { font-size: 0.72rem; color: var(--text-muted); }
.music-controls { display: flex; justify-content: center; align-items: center; gap: 16px; margin-bottom: 6px; }
.ctrl-btn { background: none; border: none; cursor: pointer; color: var(--text-secondary); padding: 4px; display: flex; align-items: center; justify-content: center; }
.ctrl-btn:hover { color: var(--primary); }
.ctrl-btn.small { padding: 2px; }
.ctrl-btn.active { color: var(--primary); }
.play-btn { width: 36px; height: 36px; border-radius: 50%; background: var(--primary); color: white; }
.play-btn:hover { background: var(--primary-dark); color: white; }
.secondary-controls { display: flex; justify-content: center; gap: 12px; margin-bottom: 6px; }
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
.song-item { display: flex; align-items: center; gap: 6px; padding: 6px 4px; border-radius: var(--radius-sm); cursor: pointer; transition: background 0.15s ease; }
.song-item:hover { background: var(--surface-hover); }
.song-item.active { background: var(--primary-50); }
.song-idx { width: 20px; font-size: 0.7rem; color: var(--text-muted); text-align: center; flex-shrink: 0; }
.song-info { flex: 1; min-width: 0; }
.song-title { display: block; font-size: 0.75rem; font-weight: 500; }
.song-artist { font-size: 0.65rem; color: var(--text-muted); }
.song-votes { display: flex; align-items: center; gap: 2px; flex-shrink: 0; }
.song-votes button { background: none; border: none; cursor: pointer; padding: 2px; font-size: 0.6rem; line-height: 1; color: var(--text-muted); display: flex; align-items: center; }
.song-votes button:hover { color: var(--primary); }
.song-votes button.active { color: var(--primary); }
.vote-up.active { color: var(--success); }
.vote-down.active { color: var(--danger); }
.vote-count { font-size: 0.65rem; font-weight: 600; min-width: 18px; text-align: center; color: var(--text-muted); }
.vote-count.hot { color: var(--success); }
.vote-count.cold { color: var(--danger); }
.remove-warning { font-size: 0.7rem; flex-shrink: 0; cursor: default; }
.song-remove { background: none; border: none; cursor: pointer; color: var(--text-muted); padding: 2px 4px; font-size: 0.65rem; line-height: 1; border-radius: 3px; flex-shrink: 0; }
.song-remove:hover { color: var(--danger); background: var(--danger-50); }
.song-dur { font-size: 0.65rem; color: var(--text-muted); flex-shrink: 0; }
.add-song-section { border-top: 1px solid var(--border-light); padding-top: 8px; margin-top: 4px; }
.add-song-btn { display: flex; align-items: center; gap: 6px; padding: 6px 10px; background: var(--primary-50); color: var(--primary); border: none; border-radius: var(--radius-sm); cursor: pointer; font-size: 0.75rem; font-weight: 600; width: 100%; justify-content: center; transition: all 0.15s ease; }
.add-song-btn:hover { background: var(--primary-100); }
.add-song-form { display: flex; flex-direction: column; gap: 6px; padding: 8px 0; }
.song-input { padding: 6px 10px; border: 1px solid var(--border-light); border-radius: var(--radius-sm); font-size: 0.75rem; background: var(--bg-secondary); color: var(--text-primary); }
.song-input:focus { outline: none; border-color: var(--primary); }
.add-song-actions { display: flex; gap: 6px; justify-content: flex-end; }
</style>
