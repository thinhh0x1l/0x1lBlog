<template>
  <aside class="discovery-sidebar">
    <MusicBox v-if="playlist" :playlist="playlist" />
    <CanvasPreview v-if="canvas" :canvas="canvas" @viewCanvas="(c) => $emit('viewCanvas', c)" />
    <DailyQuestPanel v-if="isLoggedIn && quests.length" :quests="quests" @claim="(id) => $emit('claim', id)" />
    <BlindChallengeCard v-if="challenge" :challenge="challenge" :my-guess="myGuess" @guess="(g) => $emit('guess', g)" />
    <SkillTreeCard v-if="skillTrees.length" :trees="skillTrees" :my-progress="skillProgress" :is-logged-in="isLoggedIn" />
    <TrendingMini v-if="trending.length" :items="trending" />
    <BlogTags v-if="tags.length" :tags="tags" />
  </aside>
</template>

<script setup>
import MusicBox from './MusicBox.vue'
import CanvasPreview from './CanvasPreview.vue'
import DailyQuestPanel from './DailyQuestPanel.vue'
import BlindChallengeCard from './BlindChallengeCard.vue'
import SkillTreeCard from './SkillTreeCard.vue'
import TrendingMini from './TrendingMini.vue'
import BlogTags from './BlogTags.vue'

defineProps({
  playlist: Object,
  canvas: Object,
  quests: { type: Array, default: () => [] },
  challenge: Object,
  myGuess: Object,
  skillTrees: { type: Array, default: () => [] },
  skillProgress: Object,
  trending: { type: Array, default: () => [] },
  tags: { type: Array, default: () => [] },
  isLoggedIn: { type: Boolean, default: false },
})

defineEmits(['viewCanvas', 'claim', 'guess'])
</script>

<style scoped lang="scss">
.discovery-sidebar {
  width: 280px;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  gap: 16px;
  position: sticky;
  top: calc(var(--header-height) + 24px);
  height: fit-content;
}

.sidebar-card {
  background: var(--surface);
  border-radius: var(--radius-xl);
  border: 1px solid var(--border-light);
  overflow: hidden;
}
</style>
