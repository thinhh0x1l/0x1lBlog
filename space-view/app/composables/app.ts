import { defineStore } from 'pinia'

export const useAppStore = defineStore('app', () => {
  const siteInfo = ref<any>({})
  const webTitleSuffix = computed(() => siteInfo.value?.webTitleSuffix || '')
  return { siteInfo, webTitleSuffix }
})
