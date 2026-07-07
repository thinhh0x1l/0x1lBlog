<template>
  <OneColumnLayout>
    <div class="canvas-page">
      <h1 class="page-title">Canvas</h1>
      <section class="canvas-section" v-if="communityCanvases.length">
        <h2 class="section-title">Community Canvases</h2>
        <div class="canvas-grid">
          <div v-for="c in communityCanvases" :key="c.id" class="canvas-card" @click="openViewer(c)">
            <img v-if="c.thumbnailUrl" :src="c.thumbnailUrl" :alt="c.title" class="canvas-thumb" />
            <div v-else class="canvas-thumb-placeholder">
              <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><rect x="3" y="3" width="18" height="18" rx="2"/><circle cx="8.5" cy="8.5" r="1.5"/><polyline points="21 15 16 10 5 21"/></svg>
            </div>
            <div class="canvas-info">
              <span class="canvas-title">{{ c.title }}</span>
              <span class="canvas-size">{{ c.width }}x{{ c.height }}</span>
            </div>
          </div>
        </div>
      </section>
      <section class="canvas-section" v-if="eventCanvases.length">
        <h2 class="section-title">Event Canvases</h2>
        <div class="canvas-grid">
          <div v-for="c in eventCanvases" :key="c.id" class="canvas-card event-card" @click="openViewer(c)">
            <div class="event-badge">{{ timeRemaining(c) }}</div>
            <img v-if="c.thumbnailUrl" :src="c.thumbnailUrl" :alt="c.title" class="canvas-thumb" />
            <div v-else class="canvas-thumb-placeholder">
              <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><rect x="3" y="3" width="18" height="18" rx="2"/><circle cx="8.5" cy="8.5" r="1.5"/><polyline points="21 15 16 10 5 21"/></svg>
            </div>
            <div class="canvas-info">
              <span class="canvas-title">{{ c.title }}</span>
              <span class="canvas-size">{{ c.width }}x{{ c.height }}</span>
            </div>
          </div>
        </div>
      </section>
      <el-empty v-if="!communityCanvases.length && !eventCanvases.length" description="Không có canvas nào" />
      <CanvasViewer v-if="viewerVisible" :canvas="viewingCanvas" :initial-strokes="[]" @close="viewerVisible = false" @stroke="handleStroke" />
    </div>
  </OneColumnLayout>
</template>
<script setup>
import { ref } from 'vue'
import OneColumnLayout from '@/components/layouts/OneColumnLayout.vue'
import CanvasViewer from '@/components/canvas/CanvasViewer.vue'
const communityCanvases = ref([{ id: 1, title: 'Global Canvas', width: 500, height: 500, thumbnailUrl: 'https://picsum.photos/seed/canvas1/200/200' }])
const eventCanvases = ref([])
const viewerVisible = ref(false)
const viewingCanvas = ref(null)
const openViewer = (c) => { viewingCanvas.value = c; viewerVisible.value = true }
const timeRemaining = (c) => '2 ngày'
const handleStroke = () => {}
</script>
<style scoped lang="scss">
.page-title { font-size: 1.5rem; font-weight: 800; margin-bottom: var(--space-xl); }
.canvas-section { margin-bottom: var(--space-2xl); }
.section-title { font-size: 1.1rem; font-weight: 700; margin-bottom: var(--space-lg); }
.canvas-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(200px, 1fr)); gap: var(--space-md); }
.canvas-card { background: var(--surface); border: 1px solid var(--border-light); border-radius: var(--radius-xl); overflow: hidden; cursor: pointer; transition: all var(--duration-fast) ease; }
.canvas-card:hover { border-color: var(--primary); box-shadow: var(--shadow-md); transform: translateY(-2px); }
.canvas-thumb { width: 100%; aspect-ratio: 1; object-fit: cover; }
.canvas-thumb-placeholder { width: 100%; aspect-ratio: 1; display: flex; align-items: center; justify-content: center; background: var(--bg-secondary); }
.canvas-info { padding: var(--space-sm) var(--space-md); display: flex; justify-content: space-between; align-items: center; }
.canvas-title { font-size: 0.85rem; font-weight: 600; }
.canvas-size { font-size: 0.75rem; color: var(--text-muted); }
.event-card { position: relative; }
.event-badge { position: absolute; top: 8px; right: 8px; background: var(--danger); color: white; font-size: 0.7rem; font-weight: 600; padding: 2px 8px; border-radius: var(--radius-full); z-index: 1; }
</style>
