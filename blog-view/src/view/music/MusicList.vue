<template>
  <TwoColumnLayout>
    <template #sidebar-left>
      <AppSidebar />
    </template>

    <div class="music-list-page">
      <div class="page-header">
        <h1>Âm nhạc</h1>
        <p class="page-desc">Khám phá playlist và thư giãn cùng âm nhạc</p>
      </div>

      <div class="playlist-grid" v-if="playlists.length">
        <router-link v-for="p in playlists" :key="p.id" :to="`/music/${p.id}`" class="playlist-card">
          <div class="pl-cover" :style="{ background: coverColors[p.id % coverColors.length] }">
            <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><path d="M9 18V5l12-2v13"/><circle cx="6" cy="18" r="3"/><circle cx="18" cy="16" r="3"/></svg>
          </div>
          <div class="pl-info">
            <h3 class="pl-title">{{ p.title }}</h3>
            <span class="pl-meta">{{ p.songCount }} bài hát</span>
          </div>
        </router-link>
      </div>

      <el-empty v-else description="Chưa có playlist nào" />
    </div>
  </TwoColumnLayout>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { playlistApi } from '@/api'
import TwoColumnLayout from '@/components/layouts/TwoColumnLayout.vue'
import AppSidebar from '@/components/layout/AppSidebar.vue'

const playlists = ref([])
const coverColors = ['#0ea5e9', '#8b5cf6', '#10b981', '#f59e0b', '#ec4899', '#6366f1', '#06b6d4', '#f97316']

onMounted(async () => {
  try {
    const res = await playlistApi.getAll()
    playlists.value = res.data || []
  } catch (e) { console.error(e) }
})
</script>

<style lang="scss" scoped>
.music-list-page {
  min-width: 0;
}

.page-header {
  margin-bottom: var(--space-xl);

  h1 {
    font-size: 1.5rem;
    font-weight: 700;
    margin-bottom: 4px;
  }

  .page-desc {
    color: var(--text-muted);
    font-size: 0.88rem;
  }
}

.playlist-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: var(--space-md);
}

.playlist-card {
  display: block;
  text-decoration: none;
  border-radius: var(--radius-xl);
  overflow: hidden;
  border: 1px solid var(--border-light);
  background: var(--surface);
  transition: all 0.2s ease;

  &:hover {
    border-color: var(--border);
    box-shadow: var(--shadow-md);
    transform: translateY(-2px);
  }
}

.pl-cover {
  height: 140px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: rgba(255, 255, 255, 0.85);
}

.pl-info {
  padding: var(--space-md);
}

.pl-title {
  font-size: 0.95rem;
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: 4px;
}

.pl-meta {
  font-size: 0.78rem;
  color: var(--text-muted);
}
</style>
