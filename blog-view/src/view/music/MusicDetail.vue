<template>
  <OneColumnLayout>
    <div class="music-detail" v-if="playlist">
      <APlayerMusicBox
        :playlist="playlist"
        :is-logged-in="!!user"
        :plugins="plugins"
        @song-change="onSongChange"
      />

      <div class="music-content-section">
        <div class="music-main">
          <div class="current-song-info" v-if="currentSong">
            <h2 class="song-title">{{ currentSong.title }}</h2>
            <span class="song-artist">{{ currentSong.artist }}</span>
          </div>

          <ReactionBar :song-id="currentSong?.id" v-if="currentSong" />

          <CommentSection :song-id="currentSong?.id" :is-logged-in="!!user" v-if="currentSong" />
        </div>

        <aside class="music-sidebar">
          <SuggestedPlaylists />
        </aside>
      </div>
    </div>

    <el-empty v-else description="Không tìm thấy playlist" />
  </OneColumnLayout>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useAuthStore } from '@/store/auth'
import { playlistApi } from '@/api'
import { UrlSourcePlugin, VotePlugin, PlaylistManagerPlugin } from '@/components/blog/music-plugins'
import OneColumnLayout from '@/components/layouts/OneColumnLayout.vue'
import APlayerMusicBox from '@/components/blog/APlayerMusicBox.vue'
import ReactionBar from '@/components/music/ReactionBar.vue'
import CommentSection from '@/components/music/CommentSection.vue'
import SuggestedPlaylists from '@/components/music/SuggestedPlaylists.vue'

const route = useRoute()
const authStore = useAuthStore()
const user = ref(authStore.user)

const plugins = [UrlSourcePlugin, VotePlugin, PlaylistManagerPlugin]
const playlist = ref(null)
const currentSong = ref(null)

const onSongChange = (song) => {
  currentSong.value = song
}

onMounted(async () => {
  try {
    const res = await playlistApi.getById(route.params.id)
    playlist.value = res.data
    if (res.data?.songs?.length) {
      currentSong.value = res.data.songs[0]
    }
  } catch (e) { console.error(e) }
})
</script>

<style lang="scss" scoped>
.music-detail {
  min-width: 0;
}

.music-content-section {
  display: grid;
  grid-template-columns: 1fr 280px;
  gap: 24px;
  margin-top: var(--space-lg);
}

.music-main {
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: var(--space-lg);
}

.music-sidebar {
  position: sticky;
  top: calc(var(--header-height, 64px) + 24px);
  height: fit-content;
  max-height: calc(100vh - var(--header-height, 64px) - 48px);
  overflow-y: auto;
}

.current-song-info {
  .song-title {
    font-size: 1.1rem;
    font-weight: 700;
    color: var(--text-primary);
    margin-bottom: 2px;
  }
  .song-artist {
    font-size: 0.85rem;
    color: var(--text-muted);
  }
}

@media (max-width: 768px) {
  .music-content-section {
    grid-template-columns: 1fr;
  }
  .music-sidebar {
    display: none;
  }
}
</style>
