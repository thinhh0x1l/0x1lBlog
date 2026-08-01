import { onMounted, onUnmounted } from 'vue'

export function useKeyboard(handlers) {
  const onKey = (e) => {
    const fn = handlers[e.key]
    if (fn) { e.preventDefault(); fn() }
  }
  onMounted(() => window.addEventListener('keydown', onKey))
  onUnmounted(() => window.removeEventListener('keydown', onKey))

  return { register: () => window.addEventListener('keydown', onKey), unregister: () => window.removeEventListener('keydown', onKey) }
}
