<template>
  <div class="login-page">
    <div class="login-container">
      <!-- Left side - Branding -->
      <div class="login-branding">
        <div class="branding-content">
          <div class="brand-logo">
            <svg width="48" height="48" viewBox="0 0 36 36" fill="none">
              <rect width="36" height="36" rx="10" fill="url(#login-grad)"/>
              <path d="M10 13h16M10 17.5h12M10 22h8" stroke="white" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round"/>
              <defs><linearGradient id="login-grad" x1="0" y1="0" x2="36" y2="36"><stop stop-color="#0ea5e9"/><stop offset="1" stop-color="#8b5cf6"/></linearGradient></defs>
            </svg>
          </div>
          <h1 class="brand-name">0x1lBlog</h1>
          <p class="brand-tagline">Nơi chia sẻ kiến thức lập trình</p>
          <div class="brand-features">
            <div class="feature"><span class="feature-icon">✍️</span><span>Viết blog Markdown chuyên nghiệp</span></div>
            <div class="feature"><span class="feature-icon">💬</span><span>Thảo luận & bình luận</span></div>
            <div class="feature"><span class="feature-icon">🏆</span><span>Hệ thống badge & xếp hạng</span></div>
          </div>
        </div>
      </div>

      <!-- Right side - Form -->
      <div class="login-form-wrapper">
        <div class="form-container">
          <div class="form-header">
            <h2>{{ isRegister ? 'Tạo tài khoản' : 'Chào mừng trở lại' }}</h2>
            <p>{{ isRegister ? 'Đăng ký để bắt đầu' : 'Đăng nhập để tiếp tục' }}</p>
          </div>

          <form @submit.prevent="handleSubmit" class="login-form">
            <div class="form-group" v-if="isRegister">
              <label>Tên hiển thị</label>
              <input v-model="form.displayName" type="text" placeholder="Nguyễn Văn A" class="form-input" />
            </div>
            <div class="form-group">
              <label>Tên đăng nhập</label>
              <input v-model="form.username" type="text" placeholder="username" class="form-input" required />
            </div>
            <div class="form-group" v-if="isRegister">
              <label>Email</label>
              <input v-model="form.email" type="email" placeholder="email@example.com" class="form-input" />
            </div>
            <div class="form-group">
              <label>Mật khẩu</label>
              <div class="password-wrapper">
                <input v-model="form.password" :type="showPwd ? 'text' : 'password'" placeholder="••••••••" class="form-input" required />
                <button type="button" class="pwd-toggle" @click="showPwd = !showPwd">
                  <svg v-if="!showPwd" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/></svg>
                  <svg v-else width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"/><line x1="1" y1="1" x2="23" y2="23"/></svg>
                </button>
              </div>
            </div>
            <button type="submit" class="submit-btn" :disabled="loading">
              <span v-if="loading" class="spinner"></span>
              <span v-else>{{ isRegister ? 'Đăng ký' : 'Đăng nhập' }}</span>
            </button>
          </form>

          <div class="form-divider">
            <span>hoặc</span>
          </div>

          <button class="social-btn google-btn" @click="handleGoogleLogin">
            <svg width="18" height="18" viewBox="0 0 24 24"><path d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92a5.06 5.06 0 0 1-2.2 3.32v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.1z" fill="#4285F4"/><path d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z" fill="#34A853"/><path d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z" fill="#FBBC05"/><path d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z" fill="#EA4335"/></svg>
            Đăng nhập với Google
          </button>

          <p class="form-footer">
            {{ isRegister ? 'Đã có tài khoản?' : 'Chưa có tài khoản?' }}
            <a href="#" @click.prevent="isRegister = !isRegister">{{ isRegister ? 'Đăng nhập' : 'Đăng ký ngay' }}</a>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/store/auth'

const router = useRouter()
const authStore = useAuthStore()
const isRegister = ref(false)
const showPwd = ref(false)
const loading = ref(false)
const form = reactive({ username: '', password: '', email: '', displayName: '' })

const handleSubmit = async () => {
  if (!form.username || !form.password) { ElMessage.warning('Vui lòng nhập đầy đủ thông tin'); return }
  loading.value = true
  setTimeout(() => {
    authStore.setToken('mock-token')
    authStore.setUser({ id: 1, username: form.username, displayName: form.displayName || form.username, role: 'USER', avatarUrl: '', blogCount: 0, followerCount: 0, followingCount: 0, level: 1 })
    ElMessage.success(isRegister.value ? 'Đăng ký thành công!' : 'Đăng nhập thành công!')
    router.push('/home')
    loading.value = false
  }, 800)
}

const handleGoogleLogin = () => { ElMessage.info('Google OAuth sẽ được tích hợp sau') }
</script>

<style scoped lang="scss">
.login-page { min-height: 100vh; display: flex; background: var(--bg); }
.login-container { display: flex; width: 100%; }

.login-branding {
  flex: 1; display: flex; align-items: center; justify-content: center;
  background: linear-gradient(135deg, #0ea5e9 0%, #8b5cf6 50%, #ec4899 100%);
  padding: var(--space-3xl); position: relative; overflow: hidden;
}
.login-branding::before { content: ''; position: absolute; inset: 0; background: url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%23ffffff' fill-opacity='0.05'%3E%3Cpath d='M36 34v-4h-2v4h-4v2h4v4h2v-4h4v-2h-4zm0-30V0h-2v4h-4v2h4v4h2V6h4V4h-4zM6 34v-4H4v4H0v2h4v4h2v-4h4v-2H6zM6 4V0H4v4H0v2h4v4h2V6h4V4H6z'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E"); }

.branding-content { position: relative; z-index: 1; text-align: center; color: white; }
.brand-logo { margin-bottom: var(--space-lg); filter: drop-shadow(0 4px 12px rgba(0,0,0,0.2)); }
.brand-name { font-size: 2.5rem; font-weight: 800; letter-spacing: -0.03em; margin-bottom: 8px; }
.brand-tagline { font-size: 1.1rem; opacity: 0.9; margin-bottom: var(--space-2xl); }
.brand-features { display: flex; flex-direction: column; gap: var(--space-md); text-align: left; max-width: 320px; margin: 0 auto; }
.feature { display: flex; align-items: center; gap: 10px; font-size: 0.95rem; opacity: 0.95; }
.feature-icon { font-size: 1.2rem; }

.login-form-wrapper { width: 480px; display: flex; align-items: center; justify-content: center; padding: var(--space-3xl); }
.form-container { width: 100%; max-width: 380px; }
.form-header { margin-bottom: var(--space-xl); }
.form-header h2 { font-size: 1.6rem; font-weight: 800; color: var(--text-primary); margin-bottom: 6px; letter-spacing: -0.02em; }
.form-header p { color: var(--text-muted); font-size: 0.9rem; }

.login-form { display: flex; flex-direction: column; gap: var(--space-md); }
.form-group { display: flex; flex-direction: column; gap: 6px; }
.form-group label { font-size: 0.82rem; font-weight: 600; color: var(--text-secondary); }
.form-input {
  width: 100%; padding: 12px 14px; border: 1px solid var(--border);
  border-radius: var(--radius-md); font-size: 0.9rem; color: var(--text-primary);
  background: var(--surface); transition: all var(--duration-fast) ease; font-family: inherit;
}
.form-input:focus { outline: none; border-color: var(--primary); box-shadow: 0 0 0 3px var(--primary-50); }
.form-input::placeholder { color: var(--text-muted); }

.password-wrapper { position: relative; }
.password-wrapper .form-input { padding-right: 42px; }
.pwd-toggle {
  position: absolute; right: 10px; top: 50%; transform: translateY(-50%);
  background: none; border: none; color: var(--text-muted); cursor: pointer; padding: 4px;
}

.submit-btn {
  width: 100%; padding: 12px; border: none; border-radius: var(--radius-md);
  background: linear-gradient(135deg, var(--primary), var(--primary-dark));
  color: white; font-size: 0.95rem; font-weight: 600; cursor: pointer;
  transition: all var(--duration-normal) var(--ease-out);
  box-shadow: 0 2px 8px rgba(14,165,233,0.3); margin-top: var(--space-sm);
}
.submit-btn:hover { transform: translateY(-1px); box-shadow: 0 4px 12px rgba(14,165,233,0.4); }
.submit-btn:disabled { opacity: 0.6; cursor: not-allowed; transform: none; }

.spinner { display: inline-block; width: 18px; height: 18px; border: 2px solid rgba(255,255,255,0.3); border-top-color: white; border-radius: 50%; animation: spin 0.6s linear infinite; }
@keyframes spin { to { transform: rotate(360deg); } }

.form-divider { display: flex; align-items: center; gap: var(--space-md); margin: var(--space-lg) 0; color: var(--text-muted); font-size: 0.82rem; }
.form-divider::before, .form-divider::after { content: ''; flex: 1; height: 1px; background: var(--border); }

.social-btn {
  width: 100%; padding: 12px; border: 1px solid var(--border); border-radius: var(--radius-md);
  background: var(--surface); font-size: 0.9rem; font-weight: 500; cursor: pointer;
  display: flex; align-items: center; justify-content: center; gap: 8px;
  transition: all var(--duration-fast) ease; color: var(--text-primary); font-family: inherit;
}
.social-btn:hover { background: var(--surface-hover); border-color: var(--text-muted); }

.form-footer { text-align: center; margin-top: var(--space-xl); font-size: 0.85rem; color: var(--text-muted); }
.form-footer a { color: var(--primary); font-weight: 600; margin-left: 4px; }

@media (max-width: 768px) {
  .login-container { flex-direction: column; }
  .login-branding { padding: var(--space-xl); min-height: 200px; }
  .login-form-wrapper { width: 100%; padding: var(--space-xl); }
  .brand-name { font-size: 1.8rem; }
  .brand-features { display: none; }
}
</style>
