<template>
  <div class="sv-wrap" ref="wrapRef">
    <div v-if="loading" class="sv-spinner"><div class="spinner-ring"></div></div>
    <video ref="videoRef" :src="story.mediaUrl" muted playsinline class="sv-video" :class="{ 'sv-hidden': loading }" @canplay="onReady" @ended="$emit('ended')" @error="loading = false; errored = true"></video>
    <button v-if="!loading" class="sv-mute-toggle" @click.stop="toggleMute">{{ muted ? '🔇' : '🔊' }}</button>
    <div v-if="errored" class="sv-error">Không thể tải</div>
  </div>
</template>

<script setup lang="ts">
const props = defineProps({ story: { type: Object, required: true }, isActive: { type: Boolean, default: false } })
const emit = defineEmits(['loaded', 'ended'])
const videoRef = ref<HTMLVideoElement | null>(null)
const wrapRef = ref<HTMLElement | null>(null)
const loading = ref(true)
const errored = ref(false)
const muted = ref(true)

const toggleMute = () => { if (!videoRef.value) return; muted.value = !muted.value; videoRef.value.muted = muted.value }
const onReady = () => { loading.value = false; emit('loaded'); videoRef.value?.play() }

const handleVisibility = () => {
  if (document.hidden && videoRef.value) videoRef.value.pause()
  else if (!document.hidden && videoRef.value && props.isActive) videoRef.value.play()
}

watch(() => props.story?.id, () => { loading.value = true; errored.value = false })
watch(() => props.isActive, (v) => {
  if (v) videoRef.value?.play()
  else videoRef.value?.pause()
  if (v) document.addEventListener('visibilitychange', handleVisibility)
  else document.removeEventListener('visibilitychange', handleVisibility)
}, { immediate: true })

onUnmounted(() => {
  document.removeEventListener('visibilitychange', handleVisibility)
  if (videoRef.value) { videoRef.value.pause(); videoRef.value.src = '' }
})
</script>

<style scoped lang="scss">
.sv-wrap { width: 100%; aspect-ratio: 9/16; background: #000; border-radius: 8px; overflow: hidden; position: relative; display: flex; align-items: center; justify-content: center; }
.sv-video { width: 100%; height: 100%; object-fit: cover; }
.sv-hidden { display: none; }
.sv-spinner { position: absolute; inset: 0; display: flex; align-items: center; justify-content: center; z-index: 2; }
.sv-mute-toggle { position: absolute; bottom: 16px; right: 16px; background: rgba(0,0,0,0.5); border: none; border-radius: 50%; width: 36px; height: 36px; cursor: pointer; font-size: 1rem; display: flex; align-items: center; justify-content: center; z-index: 3; }
.sv-error { color: rgba(255,255,255,0.5); font-size: 0.85rem; }
</style>
