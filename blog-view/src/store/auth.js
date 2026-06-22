import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useAuthStore = defineStore('auth', () => {
  const user = ref({
    id: 1,
    username: 'user_1',
    displayName: 'Nguyễn Văn An',
    avatarUrl: 'https://i.pravatar.cc/150?u=user1',
    email: 'user1@example.com',
    bio: 'Lập trình viên yêu thích công nghệ, admin của 0x1lBlog',
    website: 'https://user1.dev',
    location: 'Hà Nội',
    role: 'ADMIN',
    isCreator: true,
    blogCount: 42,
    followerCount: 523,
    followingCount: 89,
    level: 45,
    exp: 42000,
    checkinStreak: 15,
  })
  const token = ref('mock-token-auto')

  const isLoggedIn = computed(() => !!token.value)
  const isAdmin = computed(() => user.value?.role === 'ADMIN')
  const isCreator = computed(() => user.value?.isCreator === true)

  const setUser = (data) => { user.value = data }
  const setToken = (t) => { token.value = t; localStorage.setItem('token', t) }
  const logout = () => { user.value = null; token.value = ''; localStorage.removeItem('token') }

  return { user, token, isLoggedIn, isAdmin, isCreator, setUser, setToken, logout }
})
