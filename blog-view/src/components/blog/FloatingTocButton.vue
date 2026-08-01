<template>
  <button class="floating-toc-btn" @click="$emit('toggle')" v-if="visible">
    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M4 6h16M4 12h12M4 18h8"/></svg>
  </button>
</template>
<script setup>
defineProps({ visible: Boolean })
defineEmits(['toggle'])
</script>
<style scoped lang="scss">
.floating-toc-btn { position: fixed; bottom: 24px; left: 24px; z-index: 100; width: 48px; height: 48px; border-radius: 50%; background: var(--primary); color: white; border: none; box-shadow: 0 4px 12px rgba(14,165,233,0.4); cursor: pointer; display: flex; align-items: center; justify-content: center; transition: transform 0.2s; }
.floating-toc-btn:hover { transform: scale(1.1); }
</style>
