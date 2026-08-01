<template>
  <div class="suggested-playlists" v-if="playlists.length">
    <h4 class="section-title">Gợi ý</h4>
    <div class="playlist-grid">
      <NuxtLink v-for="p in playlists" :key="p.id" :to="`/music/${p.id}`" class="playlist-card">
        <div class="playlist-cover" :style="{ background: coverColors[p.id % coverColors.length] }">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M9 18V5l12-2v13"/><circle cx="6" cy="18" r="3"/><circle cx="18" cy="16" r="3"/></svg>
        </div>
        <div class="playlist-info">
          <span class="playlist-title">{{ p.title }}</span>
          <span class="playlist-songs">{{ p.songCount }} bài</span>
        </div>
      </NuxtLink>
    </div>
  </div>
</template>

<script setup lang="ts">
const playlists = ref<any[]>([])
const coverColors = ['#0ea5e9', '#8b5cf6', '#10b981', '#f59e0b', '#ec4899', '#6366f1']
const api = useApi()

onMounted(async () => {
  try {
    const res: any = await api.get('/playlists')
    playlists.value = (res.data || res || []).slice(0, 5)
  } catch (e) { console.error(e) }
})
</script>

<style lang="scss" scoped>
.suggested-playlists {
  .section-title {
    font-size: 0.88rem;
    font-weight: 700;
    color: var(--text-primary);
    margin-bottom: var(--space-sm);
  }
}

.playlist-grid { display: flex; flex-direction: column; gap: 8px; }

.playlist-card {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 8px;
  border-radius: var(--radius-md);
  text-decoration: none;
  color: var(--text-primary);
  transition: background 0.15s ease;

  &:hover { background: var(--surface-hover); }
}

.playlist-cover {
  width: 40px;
  height: 40px;
  border-radius: var(--radius-sm);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  flex-shrink: 0;
}

.playlist-info { flex: 1; min-width: 0; }
.playlist-title { display: block; font-size: 0.82rem; font-weight: 600; color: var(--text-primary); margin-bottom: 1px; }
.playlist-songs { font-size: 0.7rem; color: var(--text-muted); }
</style>
