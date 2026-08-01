<template>
  <div class="si-wrap">
    <div v-if="loading" class="si-spinner"><div class="spinner-ring"></div></div>
    <img v-show="!loading" :src="story.mediaUrl" class="si-img" :class="{ 'si-ken': isActive }" @load="loading = false; $emit('loaded')" @error="loading = false; errored = true" />
    <div v-if="errored" class="si-error">Không thể tải</div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
const props = defineProps({ story: { type: Object, required: true }, isActive: { type: Boolean, default: false } })
defineEmits(['loaded'])
const loading = ref(true)
const errored = ref(false)
watch(() => props.story?.id, () => { loading.value = true; errored.value = false })
</script>

<style scoped lang="scss">
.si-wrap { width: 100%; aspect-ratio: 9/16; background: #1e293b; border-radius: 8px; overflow: hidden; position: relative; display: flex; align-items: center; justify-content: center; }
.si-img { width: 100%; height: 100%; object-fit: cover; display: block; }
.si-ken { animation: siKen var(--sp-dur, 10s) ease-in-out forwards; transform-origin: center; }
@keyframes siKen { from { transform: scale(1); } to { transform: scale(1.08); } }
.si-spinner { position: absolute; inset: 0; display: flex; align-items: center; justify-content: center; z-index: 2; }
.spinner-ring { width: 40px; height: 40px; border: 3px solid rgba(255,255,255,0.15); border-top-color: #fff; border-radius: 50%; animation: spin 0.7s linear infinite; }
@keyframes spin { to { transform: rotate(360deg); } }
.si-error { color: rgba(255,255,255,0.5); font-size: 0.85rem; }
</style>
