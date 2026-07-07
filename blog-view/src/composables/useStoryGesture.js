import { ref } from 'vue'

const SWIPE_THRESHOLD_X = 50
const SWIPE_THRESHOLD_Y = 80

export function useStoryGesture() {
  const direction = ref(null)
  let startX = 0
  let startY = 0

  const onTouchStart = (e) => {
    const t = e.touches[0]
    startX = t.clientX
    startY = t.clientY
    direction.value = null
  }

  const onTouchMove = (e) => {
    const dx = Math.abs(e.touches[0].clientX - startX)
    const dy = Math.abs(e.touches[0].clientY - startY)
    if (dx > dy) e.preventDefault()
  }

  const onTouchEnd = (e) => {
    const dx = e.changedTouches[0].clientX - startX
    const dy = e.changedTouches[0].clientY - startY
    if (Math.abs(dx) > SWIPE_THRESHOLD_X && Math.abs(dx) > Math.abs(dy) * 2) {
      direction.value = dx < 0 ? 'left' : 'right'
    } else if (Math.abs(dy) > SWIPE_THRESHOLD_Y && Math.abs(dy) > Math.abs(dx) * 2) {
      direction.value = dy > 0 ? 'down' : 'up'
    }
  }

  return { direction, onTouchStart, onTouchMove, onTouchEnd }
}
