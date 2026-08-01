<template>
  <TwoColumnLayout>
    <template #sidebar-left>
      <AppSidebar />
    </template>
    <div class="music-page">
      <div class="page-header">
        <h1>Âm nhạc</h1>
        <p class="page-desc">Khám phá playlist và thư giãn cùng âm nhạc</p>
      </div>
      <div class="playlist-grid" v-if="playlists.length">
        <NuxtLink v-for="p in playlists" :key="p.id" :to="`/music/${p.id}`" class="playlist-card">
          <div class="pl-cover" :style="{ background: coverColors[p.id % coverColors.length] }">
            <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><path d="M9 18V5l12-2v13"/><circle cx="6" cy="18" r="3"/><circle cx="18" cy="16" r="3"/></svg>
          </div>
          <div class="pl-info">
            <h3 class="pl-title">{{ p.title }}</h3>
            <span class="pl-meta">{{ p.songCount }} bài hát</span>
          </div>
        </NuxtLink>
      </div>
      <el-empty v-else description="Chưa có playlist nào" />
    </div>
  </TwoColumnLayout>
</template>
<script setup>
definePageMeta({ layout: 'default' })
useHead({ title: 'Âm nhạc - 0x1lBlog' })

const playlists = ref([])
const coverColors = ['#0ea5e9', '#8b5cf6', '#10b981', '#f59e0b', '#ec4899', '#6366f1', '#06b6d4', '#f97316']
</script>
<style lang="scss" scoped>
.music-page { min-width: 0; }
.page-header { margin-bottom: 20px; }
.page-header h1 { font-size: 1.25rem; font-weight: 700; color: var(--text-primary); margin-bottom: 4px; }
.page-desc { color: var(--text-muted); font-size: 0.85rem; }
.playlist-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(200px, 1fr)); gap: 12px; }
.playlist-card {
  display: block; text-decoration: none; border-radius: 8px; overflow: hidden;
  border: 1px solid var(--border-light); background: var(--surface);
  transition: all 0.12s;
  &:hover { border-color: var(--border); }
}
.pl-cover { height: 120px; display: flex; align-items: center; justify-content: center; color: rgba(255, 255, 255, 0.85); }
.pl-info { padding: 12px; }
.pl-title { font-size: 0.9rem; font-weight: 600; color: var(--text-primary); margin-bottom: 2px; }
.pl-meta { font-size: 0.78rem; color: var(--text-muted); }
</style>
