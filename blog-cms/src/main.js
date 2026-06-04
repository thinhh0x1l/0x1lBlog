import { createApp} from "vue";
import App from "@/App.vue";
import router from "@/router/index.js";
// import '@/assets/css/base.css'
import 'element-plus/dist/index.css'
import 'element-plus/theme-chalk/dark/css-vars.css'
import '@/assets/styles/index.scss'
import {pinia} from "@/store/pinia/pinia.js";

import ElementPlus, {ElMessage} from 'element-plus'
import 'element-plus/dist/index.css'
import editor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
import './util/dateTimeFormatUtils.js'
import PrimePluginVue from "@/plugins/primevueConfig/primePluginVue.js";
import tokenBackupPlugin from './plugins/tokenBackup'
import {initGuest} from "@/plugins/auth.js";
const app = createApp(App)

// sử dụng plugins
app .use(router)
    .use(pinia)
    .use(ElementPlus)
    .use(PrimePluginVue)
    .use(editor)
const showMessage = (type,msg) =>{
    try{
        ElMessage[type](msg)
    }catch (error){
        console.warn('Thông báo không khả dụng', error)
    }
}
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    // key là tên icon như "Edit", component là component <edit />
    app.component(key, component)
}

// Global properties
app.config.globalProperties.$msgSuccess = (msg) => showMessage('success',msg)
app.config.globalProperties.$msgError = (msg) => showMessage('error',msg)
app.config.globalProperties.$msgInfo = (msg) => showMessage('info',msg)


app .use(tokenBackupPlugin)
router.isReady().then(async ()=>{
    if( await initGuest())
        app.mount('#app')
})
