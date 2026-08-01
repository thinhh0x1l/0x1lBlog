import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useAuthStore = defineStore('auth', () => {
  const user = ref(JSON.parse(localStorage.getItem('user') || 'null'))
  const token = ref(localStorage.getItem('token') || '')
  const refreshToken = ref(localStorage.getItem('refreshToken') || '')

  const isLoggedIn = computed(() => !!token.value)
  const isAdmin = computed(() => user.value?.role === 'ADMIN')
  const isCreator = computed(() => user.value?.isCreator === true)

  const setUser = (data) => { user.value = data; localStorage.setItem('user', JSON.stringify(data)) }
  const setToken = (t) => { token.value = t; localStorage.setItem('token', t) }
  const setRefreshToken = (rt) => { refreshToken.value = rt; localStorage.setItem('refreshToken', rt) }
  const logout = () => {
    user.value = null; token.value = ''; refreshToken.value = ''
    localStorage.removeItem('token'); localStorage.removeItem('refreshToken'); localStorage.removeItem('user')
  }

  return { user, token, refreshToken, isLoggedIn, isAdmin, isCreator, setUser, setToken, setRefreshToken, logout }
})
