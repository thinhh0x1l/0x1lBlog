import { createApp } from "vue";
import App from "@/App.vue";
import router from "@/router/index.js";
import 'element-plus/dist/index.css'
import 'element-plus/theme-chalk/dark/css-vars.css'
import '@/assets/styles/index.scss'
import { pinia } from "@/store/pinia/pinia.js";

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import editor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
import './util/dateTimeFormatUtils.js'
import tokenBackupPlugin from './plugins/tokenBackup'
import { initGuest } from "@/plugins/auth.js";
import modal from './plugins/modal'
import cache from './plugins/cache'
import tab from './plugins/tab'

const app = createApp(App)

app.use(router)
   .use(pinia)
   .use(ElementPlus)
   .use(editor)

import * as ElementPlusIconsVue from '@element-plus/icons-vue'
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.config.globalProperties.$modal = modal
app.config.globalProperties.$cache = cache
app.config.globalProperties.$tab = tab
app.config.globalProperties.$msgSuccess = (msg) => modal.msgSuccess(msg)
app.config.globalProperties.$msgError = (msg) => modal.msgError(msg)
app.config.globalProperties.$msgInfo = (msg) => modal.msg(msg)

app.use(tokenBackupPlugin)

router.isReady().then(async () => {
  if (await initGuest())
    app.mount('#app')
})
