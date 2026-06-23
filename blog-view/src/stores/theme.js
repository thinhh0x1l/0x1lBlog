import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useThemeStore = defineStore('theme', () => {
  const isDark = ref(false)

  function apply(val) {
    isDark.value = val
    document.documentElement.setAttribute('data-theme', val ? 'dark' : 'light')
    localStorage.setItem('theme', val ? 'dark' : 'light')
  }

  function init() {
    const saved = localStorage.getItem('theme')
    if (saved) {
      apply(saved === 'dark')
    } else {
      const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches
      apply(prefersDark)
    }
    window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', (e) => {
      if (!localStorage.getItem('theme')) {
        apply(e.matches)
      }
    })
  }

  function toggle() {
    apply(!isDark.value)
  }

  return { isDark, init, toggle }
})
