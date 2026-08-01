import { defineStore } from 'pinia'
import { useDark } from '@vueuse/core'

export const useThemeStore = defineStore('theme', () => {
  const isDark = useDark({
    storageKey: 'theme',
    valueDark: 'dark',
    valueLight: 'light',
  })

  function toggle() {
    isDark.value = !isDark.value
  }

  return { isDark, toggle }
})
