import ElementPlus from 'element-plus'
import vi from 'element-plus/es/locale/lang/vi'

export default defineNuxtPlugin((nuxtApp) => {
  nuxtApp.vueApp.use(ElementPlus, { locale: vi })
})
