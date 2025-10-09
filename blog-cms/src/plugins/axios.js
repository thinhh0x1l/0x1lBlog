import axios from "axios";
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

const request = axios.create({
    baseURL: 'http://localhost:8090/admin/',
    timeout: 10000,
})

// Request Interceptor
request.interceptors.request.use(
    config => {
        NProgress.start()
        const token = window.sessionStorage.getItem('token')
        if (token) {
            config.headers.Authorization = token
        }
        return config
    }
)

// Response Interceptor - SỬA LỖI: thêm error handling
request.interceptors.response.use(
    response => {  // SỬA: config → response
        NProgress.done()
        return response.data  // SỬA: config.data → response.data
    },
    error => {  // THÊM: error handling
        NProgress.done()
        return Promise.reject(error)
    }
)

export default request