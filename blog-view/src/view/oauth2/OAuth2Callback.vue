<template>
  <div class="oauth2-callback">
    <div class="loading-card">
      <div class="spinner"></div>
      <p>Đang đăng nhập...</p>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/store/auth'
import api from '@/plugins/axios'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

onMounted(async () => {
  const token = route.query.token
  const userId = route.query.userId

  if (!token) {
    ElMessage.error('Đăng nhập thất bại: không có token')
    router.push('/login')
    return
  }

  localStorage.setItem('token', token)
  authStore.setToken(token)
  authStore.setUser({ id: Number(userId || 0) })

  try {
    const res = await api.get('profile/me')
    if (res?.data) {
      authStore.setUser(res.data)
      localStorage.setItem('user', JSON.stringify(res.data))
    }
  } catch {
    // proceed with minimal user data
  }

  ElMessage.success('Đăng nhập thành công!')

  const redirect = route.query.redirect || '/home'
  router.push(redirect)
})
</script>

<style scoped>
.oauth2-callback {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--bg);
}
.loading-card {
  text-align: center;
  padding: 40px;
  background: var(--surface);
  border-radius: var(--radius-lg);
  border: 1px solid var(--border-light);
  box-shadow: var(--shadow-md);
}
.loading-card p {
  margin-top: 16px;
  color: var(--text-muted);
  font-size: 0.95rem;
}
.spinner {
  display: inline-block;
  width: 32px;
  height: 32px;
  border: 3px solid var(--border);
  border-top-color: var(--primary);
  border-radius: 50%;
  animation: spin 0.6s linear infinite;
}
@keyframes spin { to { transform: rotate(360deg); } }
</style>
