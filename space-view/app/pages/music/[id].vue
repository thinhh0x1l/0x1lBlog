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
definePageMeta({ layout: 'default' })

const route = useRoute()
const authStore = useAuthStore()
const user = ref(authStore.user)

const { UrlSourcePlugin, VotePlugin, PlaylistManagerPlugin } = await import('~/components/blog/music-plugins')
const plugins = [UrlSourcePlugin, VotePlugin, PlaylistManagerPlugin]
const playlist = ref(null)
const currentSong = ref(null)

useHead({ title: `${playlist.value?.title || 'Music'} - 0x1lBlog` })

const onSongChange = (song) => {
  currentSong.value = song
}
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
