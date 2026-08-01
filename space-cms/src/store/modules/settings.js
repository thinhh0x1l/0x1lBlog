import { defineStore } from "pinia";
import defaultSettings from '@/settings.js'
import { useDark, useToggle } from '@vueuse/core'

export const useSettingsStore = defineStore('settings', () => {

  const storageSetting = JSON.parse(localStorage.getItem('layout-setting') || '{}')

  const title = ref('Hello')
  const isThemeDark = ref(true)
  const theme = ref(storageSetting.theme ?? '#409EFF')
  const sideTheme = ref(storageSetting.sideTheme ?? defaultSettings.sideTheme)
  const showSettings = ref(storageSetting.showSettings ?? defaultSettings.showSettings)
  const navType = ref(storageSetting.navType ?? 1)
  const tagsView = ref(storageSetting.tagsView ?? defaultSettings.tagsView)
  const tagsViewPersist = ref(storageSetting.tagsViewPersist ?? false)
  const tagsIcon = ref(storageSetting.tagsIcon ?? false)
  const tagsViewStyle = ref(storageSetting.tagsViewStyle ?? 'card')
  const fixedHeader = ref(storageSetting.fixedHeader ?? defaultSettings.fixedHeader)
  const sidebarLogo = ref(storageSetting.sidebarLogo ?? defaultSettings.sidebarLogo)
  const dynamicTitle = ref(storageSetting.dynamicTitle ?? false)
  const footerVisible = ref(storageSetting.footerVisible ?? false)
  const footerContent = ref(defaultSettings.footerContent)

  const isDark = useDark()

  const toggleDark = useToggle(isDark)

  function changeSetting(payload) {
    const { key, value } = payload
    switch (key) {
      case 'sideTheme': sideTheme.value = value; break
      case 'showSettings': showSettings.value = value; break
      case 'tagsView': tagsView.value = value; break
      case 'fixedHeader': fixedHeader.value = value; break
      case 'sidebarLogo': sidebarLogo.value = value; break
      case 'theme': theme.value = value; break
    }
    saveToStorage()
  }

  function saveToStorage() {
    const state = {
      isThemeDark: isThemeDark.value,
      theme: theme.value,
      sideTheme: sideTheme.value,
      showSettings: showSettings.value,
      navType: navType.value,
      tagsView: tagsView.value,
      fixedHeader: fixedHeader.value,
      sidebarLogo: sidebarLogo.value,
    }
    localStorage.setItem('layout-setting', JSON.stringify(state))
  }

  return {
    title,
    isThemeDark,
    theme,
    sideTheme,
    showSettings,
    navType,
    tagsView,
    tagsViewPersist,
    tagsIcon,
    tagsViewStyle,
    fixedHeader,
    sidebarLogo,
    dynamicTitle,
    footerVisible,
    footerContent,
    isDark,
    changeSetting,
    toggleTheme: () => {
      isThemeDark.value = !isThemeDark.value
      toggleDark()
      saveToStorage()
    }
  }
})
