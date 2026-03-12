import axios from "axios";
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

const api = import.meta.env.VITE_API_URL
const request = axios.create({
    baseURL: api,
    timeout: 10000,
    withCredentials: true
})

request.interceptors.request.use((config) => {
    NProgress.start()
    return config
})

request.interceptors.response.use((response) => {
    NProgress.done()
    return response.data
})

export default request