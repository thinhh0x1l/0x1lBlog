import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router/index.js'
import ElementPlusPlugin from './plugins/element-plus.js'
import tokenBackupPlugin from './plugins/tokenBackup'
import { pinia } from './store/pinia/pinia.js'
import { initGuest } from './plugins/auth.js'
import './assets/scss/index.scss'

const app = createApp(App)
app.use(router).use(pinia).use(ElementPlusPlugin).use(tokenBackupPlugin)

router.isReady().then(async () => {
  if (await initGuest()) app.mount('#app')
})
