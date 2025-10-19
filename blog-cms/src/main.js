import { createApp} from "vue";
import App from "@/App.vue";
import router from "@/router/index.js";
import './assets/css/base.css'

// Element Plus (thay cho Element UI)
import ElementPlus, {ElMessage} from 'element-plus'
import 'element-plus/dist/index.css'

import './util/dateTimeFormatUtils.js'

const app = createApp(App)

// sử dụng plugins
app.use(router)
app.use(ElementPlus)

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

app.mount('#app')