import axios from 'axios'

export const useApi = () => {
  const config = useRuntimeConfig()
  const api = axios.create({
    baseURL: config.public.apiBase,
    timeout: 10000,
  })

  api.interceptors.request.use((cfg) => {
    if (import.meta.client) {
      const token = localStorage.getItem('token')
      if (token) cfg.headers.Authorization = `Bearer ${token}`
    }
    return cfg
  })

  api.interceptors.response.use(
    (res) => res.data,
    async (err) => {
      if (err.response?.status === 401 && import.meta.client) {
        localStorage.clear()
        navigateTo('/login')
      }
      return Promise.reject(err)
    }
  )

  return api
}
