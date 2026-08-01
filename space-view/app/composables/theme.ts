import { defineStore } from 'pinia'
import { useDark, useToggle } from '@vueuse/core'

export const useThemeStore = defineStore('theme', () => {
  const isDark = useDark({ storageKey: 'theme' })
  const toggleDark = useToggle(isDark)
  return { isDark, toggleDark }
})
