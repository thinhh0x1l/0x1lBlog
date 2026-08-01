<template>
  <ClientOnly>
    <div class="canvas-viewer-overlay" @click.self="$emit('close')">
      <div class="canvas-viewer">
        <div class="canvas-toolbar">
          <div class="toolbar-left">
            <span class="canvas-title">{{ canvas?.title || 'Canvas' }}</span>
            <span class="canvas-size">{{ canvas?.width }}×{{ canvas?.height }}</span>
          </div>
          <div class="toolbar-center">
            <div class="color-palette">
              <button v-for="c in colors" :key="c" :class="['color-swatch', { active: selectedColor === c }]"
                :style="{ background: c }" @click="selectedColor = c" />
            </div>
            <div class="brush-sizes">
              <button v-for="s in brushSizes" :key="s" :class="['brush-btn', { active: brushSize === s }]"
                @click="brushSize = s">{{ s }}px</button>
            </div>
          </div>
          <div class="toolbar-right">
            <button class="tool-btn replay-btn" @click="startReplay">Replay</button>
            <button class="tool-btn close-btn" @click="$emit('close')">X</button>
          </div>
        </div>
        <div class="canvas-stage-wrapper">
          <canvas ref="canvasEl" :width="canvas?.width || 200" :height="canvas?.height || 200"
            class="canvas-stage" @mousedown="startDraw" @mousemove="draw" @mouseup="stopDraw" @mouseleave="stopDraw"
            @touchstart.prevent="touchStart" @touchmove.prevent="touchMove" @touchend="stopDraw" />
        </div>
        <div class="canvas-footer">
          <span>{{ strokesCount }} strokes {{ strokeCountToday }} hôm nay</span>
          <span class="rate-warning" v-if="isRateLimited" style="color: var(--danger)">Chậm lại! 1 stroke/giây</span>
        </div>
      </div>
    </div>
    <template #fallback>
      <div class="canvas-viewer-overlay canvas-loading" @click.self="$emit('close')">
        <div class="canvas-viewer">
          <div class="canvas-toolbar">
            <div class="toolbar-left">
              <span class="canvas-title">Loading...</span>
            </div>
          </div>
        </div>
      </div>
    </template>
  </ClientOnly>
</template>
<script setup lang="ts">
const props = defineProps({ canvas: Object, initialStrokes: { type: Array, default: () => [] } })
const emit = defineEmits(['close', 'stroke'])
const colors = ['#000000','#ffffff','#ef4444','#f97316','#f59e0b','#10b981','#06b6d4','#0ea5e9','#6366f1','#8b5cf6','#ec4899','#be123c','#78350f','#4ade80','#22d3ee','#cbd5e1']
const brushSizes = [1, 3, 5]
const selectedColor = ref('#0ea5e9')
const brushSize = ref(3)
const canvasEl = ref<HTMLCanvasElement | null>(null)
const isDrawing = ref(false)
const lastStrokeTime = ref(0)
const isRateLimited = ref(false)
const strokes = ref([...props.initialStrokes] as any[])
const strokesCount = ref(props.initialStrokes.length)
const strokeCountToday = ref(Math.floor(Math.random() * 30))

const startDraw = (e: MouseEvent) => { isDrawing.value = true; draw(e) }
const draw = (e: MouseEvent) => {
  if (!isDrawing.value || !canvasEl.value) return
  const rect = canvasEl.value.getBoundingClientRect()
  const x = Math.floor((e.clientX - rect.left) * (props.canvas?.width || 200) / rect.width)
  const y = Math.floor((e.clientY - rect.top) * (props.canvas?.height || 200) / rect.height)
  addStroke(x, y)
}
const touchStart = (e: TouchEvent) => { isDrawing.value = true; touchMove(e) }
const touchMove = (e: TouchEvent) => {
  if (!isDrawing.value || !canvasEl.value) return
  const touch = e.touches[0]
  const rect = canvasEl.value.getBoundingClientRect()
  const x = Math.floor((touch.clientX - rect.left) * (props.canvas?.width || 200) / rect.width)
  const y = Math.floor((touch.clientY - rect.top) * (props.canvas?.height || 200) / rect.height)
  addStroke(x, y)
}
const stopDraw = () => { isDrawing.value = false }

const addStroke = (x: number, y: number) => {
  const now = Date.now()
  if (now - lastStrokeTime.value < 1000) { isRateLimited.value = true; setTimeout(() => isRateLimited.value = false, 1000); return }
  lastStrokeTime.value = now
  const ctx = canvasEl.value?.getContext('2d')
  if (!ctx) return
  ctx.fillStyle = selectedColor.value
  ctx.beginPath()
  ctx.arc(x, y, brushSize.value, 0, Math.PI * 2)
  ctx.fill()
  strokesCount.value++
  emit('stroke', { canvasId: props.canvas?.id, x, y, color: selectedColor.value, brushSize: brushSize.value })
}

const renderStrokes = () => {
  const ctx = canvasEl.value?.getContext('2d')
  if (!ctx) return
  ctx.clearRect(0, 0, props.canvas?.width || 200, props.canvas?.height || 200)
  ctx.fillStyle = '#1e293b'
  ctx.fillRect(0, 0, props.canvas?.width || 200, props.canvas?.height || 200)
  for (const s of strokes.value) {
    ctx.fillStyle = s.color
    ctx.beginPath()
    ctx.arc(s.x, s.y, s.brushSize || 3, 0, Math.PI * 2)
    ctx.fill()
  }
}

const startReplay = async () => {
  if (!strokes.value.length) return
  const ctx = canvasEl.value?.getContext('2d')
  if (!ctx) return
  ctx.fillStyle = '#1e293b'
  ctx.fillRect(0, 0, props.canvas?.width || 200, props.canvas?.height || 200)
  for (const s of strokes.value) {
    ctx.fillStyle = s.color
    ctx.beginPath()
    ctx.arc(s.x, s.y, s.brushSize || 3, 0, Math.PI * 2)
    ctx.fill()
    await new Promise(r => setTimeout(r, 30))
  }
}

onMounted(async () => {
  await nextTick()
  renderStrokes()
})
</script>
<style scoped lang="scss">
.canvas-viewer-overlay { position: fixed; inset: 0; background: rgba(0,0,0,0.8); z-index: 10000; display: flex; align-items: center; justify-content: center; }
.canvas-loading { opacity: 0.5; }
.canvas-viewer { background: #0f172a; border-radius: 16px; max-width: 90vw; max-height: 90vh; display: flex; flex-direction: column; overflow: hidden; }
.canvas-toolbar { display: flex; align-items: center; justify-content: space-between; padding: 12px 16px; background: #1e293b; gap: 16px; flex-wrap: wrap; }
.toolbar-left { display: flex; align-items: center; gap: 8px; }
.canvas-title { font-weight: 700; font-size: 0.9rem; color: #e2e8f0; }
.canvas-size { font-size: 0.75rem; color: #64748b; padding: 2px 8px; background: #334155; border-radius: 4px; }
.toolbar-center { display: flex; align-items: center; gap: 12px; }
.color-palette { display: flex; gap: 3px; }
.color-swatch { width: 22px; height: 22px; border-radius: 4px; border: 2px solid transparent; cursor: pointer; transition: all 0.15s ease; }
.color-swatch:hover { transform: scale(1.2); }
.color-swatch.active { border-color: #facc15; box-shadow: 0 0 6px rgba(250,204,21,0.5); }
.brush-sizes { display: flex; gap: 4px; }
.brush-btn { padding: 4px 10px; background: #334155; border: 1px solid #475569; border-radius: 6px; color: #94a3b8; font-size: 0.72rem; cursor: pointer; }
.brush-btn.active { background: #0ea5e9; color: white; border-color: #0ea5e9; }
.toolbar-right { display: flex; gap: 8px; }
.tool-btn { padding: 6px 14px; border-radius: 8px; border: none; font-size: 0.78rem; font-weight: 600; cursor: pointer; }
.replay-btn { background: #10b981; color: white; }
.replay-btn:hover { background: #059669; }
.close-btn { background: #ef4444; color: white; }
.close-btn:hover { background: #dc2626; }
.canvas-stage-wrapper { display: flex; align-items: center; justify-content: center; padding: 16px; }
.canvas-stage { border: 2px solid #334155; border-radius: 8px; cursor: crosshair; image-rendering: pixelated; max-width: 100%; max-height: 70vh; }
.canvas-footer { display: flex; justify-content: space-between; padding: 8px 16px; font-size: 0.72rem; color: #64748b; }
</style>
