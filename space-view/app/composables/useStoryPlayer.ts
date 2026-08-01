import { ref, computed, watch, type Ref } from 'vue'
import { useStoryTimer } from './useStoryTimer'

interface Story {
  durationMs?: number
  [key: string]: any
}

interface StoryGroup {
  userId: string
  stories: Story[]
  [key: string]: any
}

export function useStoryPlayer(
  groupsRef: Ref<StoryGroup[]>,
  groupIndexRef: Ref<number>,
  onAdvanceGroup: () => void
) {
  const currentIndex = ref(0)
  const paused = ref(false)

  const currentGroup = computed(() => groupsRef.value?.[groupIndexRef.value] || null)
  const currentStory = computed(() => currentGroup.value?.stories?.[currentIndex.value] || null)
  const isPlaying = computed(() => !paused.value && currentStory.value != null)

  const getMs = () => currentStory.value?.durationMs || 5000

  const goNext = () => {
    if (!currentGroup.value) return
    if (currentIndex.value < currentGroup.value.stories.length - 1) {
      currentIndex.value++
      paused.value = false
      timer.start()
    } else {
      onAdvanceGroup()
    }
  }

  const goPrev = () => {
    if (currentIndex.value > 0) {
      currentIndex.value--
      paused.value = false
      timer.start()
    }
  }

  const goToIndex = (idx: number) => {
    if (idx >= 0 && idx < (currentGroup.value?.stories.length || 0)) {
      currentIndex.value = idx
      paused.value = false
      timer.start()
    }
  }

  const play = () => {
    paused.value = false
    timer.resume()
  }
  const pause = () => {
    paused.value = true
    timer.pause()
  }
  const togglePause = () => (paused.value ? play() : pause())

  const timer = useStoryTimer(goNext, getMs)

  const syncTimer = () => {
    timer.stop()
    if (!paused.value && currentStory.value) timer.start()
  }

  watch(groupIndexRef, () => {
    currentIndex.value = 0
    paused.value = false
    syncTimer()
  })

  const reset = () => {
    currentIndex.value = 0
    paused.value = false
    timer.stop()
  }

  return {
    currentIndex,
    paused,
    isPlaying,
    currentStory,
    currentGroup,
    goNext,
    goPrev,
    goToIndex,
    play,
    pause,
    togglePause,
    reset,
  }
}