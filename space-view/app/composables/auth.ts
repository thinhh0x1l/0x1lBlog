import { defineStore } from 'pinia'

export const useAuthStore = defineStore('auth', () => {
  const user = ref<any>(null)
  const token = useCookie('token', { maxAge: 60 * 60 * 24 * 7 })
  const refreshToken = useCookie('refreshToken', { maxAge: 60 * 60 * 24 * 30 })

  if (import.meta.client) {
    const stored = localStorage.getItem('user')
    if (stored) user.value = JSON.parse(stored)
  }

  const isLoggedIn = computed(() => !!token.value)
  const isAdmin = computed(() => user.value?.role === 'ADMIN')
  const isCreator = computed(() => user.value?.isCreator === true)

  const setUser = (data: any) => {
    user.value = data
    if (import.meta.client) localStorage.setItem('user', JSON.stringify(data))
  }
  const setToken = (t: string) => {
    token.value = t
    if (import.meta.client) localStorage.setItem('token', t)
  }
  const setRefreshToken = (rt: string) => {
    refreshToken.value = rt
    if (import.meta.client) localStorage.setItem('refreshToken', rt)
  }
  const logout = () => {
    user.value = null
    token.value = undefined
    refreshToken.value = undefined
    if (import.meta.client) {
      localStorage.removeItem('token')
      localStorage.removeItem('refreshToken')
      localStorage.removeItem('user')
    }
    navigateTo('/login')
  }

  return { user, token, refreshToken, isLoggedIn, isAdmin, isCreator, setUser, setToken, setRefreshToken, logout }
})
