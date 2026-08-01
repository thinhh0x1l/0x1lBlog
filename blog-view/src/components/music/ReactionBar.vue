<template>
  <div class="reaction-bar" v-if="reactions">
    <button v-for="r in reactionTypes" :key="r.type" :class="['reaction-btn', { active: userReaction === r.type }]" @click="toggleReaction(r.type)">
      <span class="reaction-icon">{{ r.icon }}</span>
      <span class="reaction-count">{{ reactions[r.type] || 0 }}</span>
    </button>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { musicReactionApi } from '@/api'

const props = defineProps({
  songId: { type: [Number, String], required: true },
  userId: { type: [Number, String], default: null },
})

const reactionTypes = [
  { type: 'LIKE', icon: '❤️' },
  { type: 'LOVE', icon: '👍' },
  { type: 'HAHA', icon: '😂' },
  { type: 'FIRE', icon: '🔥' },
  { type: 'SAD', icon: '😮' },
]

const reactions = ref(null)
const userReaction = ref(null)

const loadReactions = async () => {
  try {
    const res = await musicReactionApi.getBySong(props.songId)
    reactions.value = res.data
  } catch (e) {
    console.error('Failed to load reactions', e)
  }
}

const toggleReaction = async (type) => {
  try {
    await musicReactionApi.toggle(props.songId, type)
    if (userReaction.value === type) {
      userReaction.value = null
      if (reactions.value) reactions.value[type] = Math.max(0, (reactions.value[type] || 0) - 1)
    } else {
      if (userReaction.value && reactions.value) {
        reactions.value[userReaction.value] = Math.max(0, (reactions.value[userReaction.value] || 0) - 1)
      }
      userReaction.value = type
      if (reactions.value) reactions.value[type] = (reactions.value[type] || 0) + 1
    }
  } catch (e) {
    console.error('Failed to toggle reaction', e)
  }
}

watch(() => props.songId, loadReactions)
onMounted(loadReactions)
</script>

<style lang="scss" scoped>
.reaction-bar {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.reaction-btn {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 6px 12px;
  border: 1px solid var(--border-light);
  border-radius: var(--radius-full);
  background: var(--surface);
  cursor: pointer;
  transition: all 0.15s ease;
  font-size: 0.85rem;
  color: var(--text-secondary);

  &:hover {
    border-color: var(--primary);
    background: var(--primary-50);
  }

  &.active {
    border-color: var(--primary);
    background: var(--primary-50);
    color: var(--primary);
    font-weight: 600;
  }
}

.reaction-icon {
  font-size: 1rem;
  line-height: 1;
}

.reaction-count {
  font-size: 0.78rem;
  font-weight: 600;
  min-width: 12px;
  text-align: center;
}
</style>
