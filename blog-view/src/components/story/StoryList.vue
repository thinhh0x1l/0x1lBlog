<template>
  <div v-if="store.userGroups.length" class="sl-strip">
    <div class="sl-scroll" ref="scrollRef">
      <StoryCard v-for="(g, i) in store.userGroups" :key="g.userId" :group="g" :is-viewed="store.isGroupViewed(g.userId)" :is-own="g.userId === ownUserId" @click="store.openViewer(i)" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useStoryStore } from '@/stores/storyStore'
import StoryCard from './StoryCard.vue'

const store = useStoryStore()
const scrollRef = ref(null)
const ownUserId = ref(null)

onMounted(async () => {
  const { useAuthStore } = await import('@/store/auth')
  const authStore = useAuthStore()
  ownUserId.value = authStore.user?.id || null
  await store.fetchStories(ownUserId.value)
})
</script>

<style scoped lang="scss">
.sl-strip { margin-bottom: var(--space-md, 16px); }
.sl-scroll { display: flex; gap: 12px; overflow-x: auto; padding: 8px 0; scrollbar-width: none; }
.sl-scroll::-webkit-scrollbar { display: none; }
</style>
