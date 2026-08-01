type KeyHandlers = Record<string, () => void>

export function useKeyboard(handlers: KeyHandlers) {
  const onKey = (e: KeyboardEvent) => {
    const fn = handlers[e.key]
    if (fn) {
      e.preventDefault()
      fn()
    }
  }

  onMounted(() => window.addEventListener('keydown', onKey))
  onUnmounted(() => window.removeEventListener('keydown', onKey))

  return {
    register: () => window.addEventListener('keydown', onKey),
    unregister: () => window.removeEventListener('keydown', onKey),
  }
}