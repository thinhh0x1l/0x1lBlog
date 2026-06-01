import { createApp} from "vue";
import App from "@/App.vue";
import router from "@/router/index.js";
import '@/assets/css/base.css'
import {initGuestToken} from "@/services/bridge/guestBootstrap.js";
import {pinia} from "@/store/pinia/pinia.js";

// Element Plus (thay cho Element UI)
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
await initGuestToken();
const showMessage = (type,msg) =>{
    try{
        ElMessage[type](msg)
    }catch (error){
        console.warn('Thông báo không khả dụng', error)
    }
}

// Global properties
app.config.globalProperties.$msgSuccess = (msg) => showMessage('success',msg)
app.config.globalProperties.$msgError = (msg) => showMessage('error',msg)
app.config.globalProperties.$msgInfo = (msg) => showMessage('info',msg)


app .use(tokenBackupPlugin)

if(initGuest())
    app.mount('#app')