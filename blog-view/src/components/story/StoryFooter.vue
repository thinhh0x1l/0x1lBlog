<template>
  <div class="sf-row">
    <StoryReply @reply="$emit('reply', $event)" />
    <StoryReaction @react="$emit('react', $event)" />
    <button class="sf-share" @click="$emit('share')">↗</button>
    <StoryMenu @action="$emit('menu', $event)" />
  </div>
</template>

<script setup>
import StoryReply from './StoryReply.vue'
import StoryReaction from './StoryReaction.vue'
import StoryMenu from './StoryMenu.vue'
defineEmits(['reply', 'react', 'share', 'menu'])
</script>

<style scoped lang="scss">
.sf-row { display: flex; align-items: center; gap: 8px; padding: 8px 4px; }
.sf-share { background: none; border: none; color: rgba(255,255,255,0.7); font-size: 1.2rem; cursor: pointer; padding: 4px; }
.sf-share:hover { color: #fff; }
</style>
