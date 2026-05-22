import axios from "axios";
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import {pinia} from "@/store/pinia/pinia.js";
import {useGuestStore} from "@/store/guessStore";

const request = axios.create({
    baseURL: 'http://localhost:8090/admin/',
    timeout: 10000,
})


request.interceptors.request.use(
    config => {
        NProgress.start()
        const token = window.sessionStorage.getItem('token')
        const guestStore =
            useGuestStore(pinia);

        const isYourApi = !(config.url)?.includes("http");
        if ( isYourApi && guestStore.guestToken) {
            config.headers["X-Guest-Token" ] = guestStore.guestToken;
        }

        if (token) {
            config.headers.Authorization = `Bearer ${token}`
        }
        return config
    }
)


request.interceptors.response.use(
    response => {
        NProgress.done()
        const guestStore =
            useGuestStore(pinia);
        const newToken = response.headers["x-guest-token"];

        if (newToken) {
            guestStore.setToken( newToken);
        }
        return response.data
    },
    error => {
        NProgress.done()
        return Promise.reject(error)
    }
)

export default request