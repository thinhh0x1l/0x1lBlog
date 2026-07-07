import { ref, onUnmounted } from 'vue'

export function useStoryTimer(onElapsed, getMs) {
  const remaining = ref(0)
  const isRunning = ref(false)
  let timer = null
  let startedAt = 0

  const stop = () => {
    if (timer) { clearTimeout(timer); timer = null }
    isRunning.value = false
  }

  const start = () => {
    stop()
    const ms = getMs()
    if (ms <= 0) return
    remaining.value = ms
    isRunning.value = true
    startedAt = Date.now()
    timer = setTimeout(() => { isRunning.value = false; onElapsed() }, ms)
  }

  const pause = () => {
    if (!isRunning.value) return
    const elapsed = Date.now() - startedAt
    remaining.value = Math.max(0, getMs() - elapsed)
    stop()
  }

  const resume = () => {
    if (remaining.value <= 0) return
    isRunning.value = true
    startedAt = Date.now()
    timer = setTimeout(() => { isRunning.value = false; onElapsed() }, remaining.value)
  }

  onUnmounted(stop)

  return { start, stop, pause, resume, remaining, isRunning }
}
