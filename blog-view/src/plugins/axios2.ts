
import axios from 'axios'
import type { AxiosInstance, InternalAxiosRequestConfig, AxiosResponse, AxiosError } from 'axios'
// @ts-ignore
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

export const request: AxiosInstance = axios.create({
    baseURL: 'https://danuta-epirogenic-reportedly.ngrok-free.dev/',
    timeout: 10000,
    withCredentials: true
})

request.interceptors.request.use(
    (config: InternalAxiosRequestConfig): InternalAxiosRequestConfig => {
        NProgress.start()
        return config
    }
)

request.interceptors.response.use(
    (response: AxiosResponse) => {
        NProgress.done()
        return response.data
    }
)
