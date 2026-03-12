
import { createApp } from 'vue'
import { createPinia } from "pinia";
import App from './App.vue'
import router from 'router/index.js'
import VueLazyLoad from "vue3-lazyload"
import '@/util/dateTimeFormatUtils.js'
import '@/assets/css/base.css'
import '@/assets/css/badge.css'
import '@/assets/css/typo.css'
import {PrismPlugin} from '@/plugins/prism/prism.js'
import PrimeVuePlugin from "@/plugins/primevueConfig/primePluginVue.js";
import FontAwesomeIcon from "@/plugins/fontAwesomeIcon.js";

const app = createApp(App)
const pinia = createPinia()
app .use(router)
    .use(pinia)
    .use(PrismPlugin)
    .use(PrimeVuePlugin)
    .component('font-awesome-icon', FontAwesomeIcon)
    .use(VueLazyLoad, {
        loading: "/img/loading.gif",
        error: "/img/error.png"
    })
    .mount('#app')
