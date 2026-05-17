     import axios from "axios";
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

const request = axios.create({
    baseURL: 'https://danuta-epirogenic-reportedly.ngrok-free.dev/admin/',
    timeout: 10000,
    withCredentials: true,
})


request.interceptors.request.use(
    config => {
        NProgress.start()
        const token = window.sessionStorage.getItem('token')
        if (token) {
            config.headers.Authorization = `Bearer ${token}`
        }
        return config
    }
)


request.interceptors.response.use(
    response => {
        NProgress.done()
        return response.data
    },
    error => {
        NProgress.done()
        return Promise.reject(error)
    }
)

export default request