
import { createApp } from 'vue'
import { createPinia } from "pinia";
import App from './App.vue'
import router from 'router/index.js'
import VueLazyLoad from "vue3-lazyload"
import '@/util/dateTimeFormatUtils.js'
import '@/assets/css/base.css'
import '@/assets/css/badge.css'
import '@/assets/css/typo.css'
// import '@/assets/css/bat.css'
import {PrismPlugin} from '@/plugins/prism/prism.js'
import PrimeVuePlugin from "@/plugins/primevueConfig/primePluginVue.js";
import FontAwesomeIcon from "@/plugins/fontAwesomeIcon.js";
import tokenBackupPlugin from './plugins/tokenBackup'
import {pinia} from "@/store/pinia/pinia.js";
import {initGuest} from "@/plugins/auth.js";
const app = createApp(App)

console.log("\n %c Thinhh's Blog %c https://0x1l-blog.vercel.app \n",
    "color: #48dbfb; background: #1b1c1d; padding:5px 0;", "background: #fadfa3; padding:5px 0;")

app .use(router)
    .use(pinia)
    .use(PrismPlugin)
    .use(PrimeVuePlugin)
    .component('FontAwesomeIcon', FontAwesomeIcon)
    .use(VueLazyLoad, {
        loading: "/img/loading.gif",
        error: "/img/error.png"
    })

app .use(tokenBackupPlugin)

router.isReady().then(async () => {
    if (await initGuest())
        app.mount('#app')
})
