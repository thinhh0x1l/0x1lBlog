<template>
  <header class="header">
    <div class="header-inner">
      <router-link to="/" class="logo">
        <div class="logo-mark">
          <svg width="36" height="36" viewBox="0 0 36 36" fill="none">
            <rect width="36" height="36" rx="10" fill="url(#logo-grad)"/>
            <path d="M10 13h16M10 17.5h12M10 22h8" stroke="white" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round"/>
            <defs>
              <linearGradient id="logo-grad" x1="0" y1="0" x2="36" y2="36">
                <stop stop-color="#0ea5e9"/>
                <stop offset="1" stop-color="#8b5cf6"/>
              </linearGradient>
            </defs>
          </svg>
        </div>
        <div class="logo-text">
          <span class="logo-brand">0x1l</span><span class="logo-accent">Blog</span>
        </div>
      </router-link>

      <div class="search-wrapper">
        <div class="search-box">
          <el-icon class="search-icon"><Search /></el-icon>
          <input
            v-model="searchQuery"
            type="text"
            class="search-input"
            placeholder="Tìm kiếm bài viết, tag, tác giả..."
            @keyup.enter="handleSearch"
          />
          <kbd class="search-kbd" v-if="!searchQuery">⌘K</kbd>
        </div>
      </div>

      <nav class="nav">
        <router-link to="/home" class="nav-link" active-class="active">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="m3 9 9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/><polyline points="9 22 9 12 15 12 15 22"/></svg>
          <span>Trang chủ</span>
        </router-link>
        <router-link to="/archives" class="nav-link" active-class="active">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M4 19.5v-15A2.5 2.5 0 0 1 6.5 2H20v20H6.5a2.5 2.5 0 0 1 0-5H20"/></svg>
          <span>Lưu trữ</span>
        </router-link>
        <router-link to="/about" class="nav-link" active-class="active">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><path d="M12 16v-4"/><path d="M12 8h.01"/></svg>
          <span>Về tôi</span>
        </router-link>
      </nav>

      <div class="header-right">
        <template v-if="isLoggedIn">
          <button class="icon-btn" @click="$router.push('/notifications')" title="Thông báo">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M6 8a6 6 0 0 1 12 0c0 7 3 9 3 9H3s3-2 3-9"/><path d="M10.3 21a1.94 1.94 0 0 0 3.4 0"/></svg>
            <span class="notif-dot" v-if="unreadCount > 0"></span>
          </button>

          <el-dropdown trigger="click" @command="handleCommand" placement="bottom-end">
            <div class="user-menu">
              <div class="user-avatar-wrapper">
                <img v-if="user?.avatarUrl" :src="user.avatarUrl" class="user-avatar" />
                <div v-else class="user-avatar user-avatar-placeholder">{{ user?.displayName?.charAt(0) || 'U' }}</div>
                <span class="user-status-dot"></span>
              </div>
              <span class="user-name">{{ user?.displayName }}</span>
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="6 9 12 15 18 9"/></svg>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M19 21v-2a4 4 0 0 0-4-4H9a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
                  Trang cá nhân
                </el-dropdown-item>
                <el-dropdown-item command="bookmarks">
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M19 21l-7-5-7 5V5a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2z"/></svg>
                  Bài viết đã lưu
                </el-dropdown-item>
                <el-dropdown-item command="settings">
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="3"/><path d="M12 1v2M12 21v2M4.22 4.22l1.42 1.42M18.36 18.36l1.42 1.42M1 12h2M21 12h2M4.22 19.78l1.42-1.42M18.36 5.64l1.42-1.42"/></svg>
                  Cài đặt
                </el-dropdown-item>
                <el-dropdown-item divided command="logout">
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/><polyline points="16 17 21 12 16 7"/><line x1="21" y1="12" x2="9" y2="12"/></svg>
                  Đăng xuất
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
        <template v-else>
          <router-link to="/login" class="login-btn">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M15 3h4a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2h-4"/><polyline points="10 17 15 12 10 7"/><line x1="15" y1="12" x2="3" y2="12"/></svg>
            Đăng nhập
          </router-link>
        </template>
      </div>
    </div>
  </header>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { Search } from '@element-plus/icons-vue'
import { useAuthStore } from '@/store/auth'

const router = useRouter()
const authStore = useAuthStore()
const searchQuery = ref('')
const unreadCount = ref(3)
const isLoggedIn = computed(() => authStore.isLoggedIn)
const user = computed(() => authStore.user)

const handleSearch = () => {
  if (searchQuery.value.trim()) router.push({ path: '/search', query: { q: searchQuery.value } })
}

const handleCommand = (cmd) => {
  if (cmd === 'profile') router.push(`/profile/${user.value?.id}`)
  else if (cmd === 'bookmarks') router.push('/bookmarks')
  else if (cmd === 'settings') router.push('/settings')
  else if (cmd === 'logout') { authStore.logout(); router.push('/home') }
}
</script>

<style scoped lang="scss">
.header {
  position: sticky;
  top: 0;
  z-index: var(--z-sticky);
  background: rgba(255, 255, 255, 0.78);
  backdrop-filter: blur(20px) saturate(180%);
  -webkit-backdrop-filter: blur(20px) saturate(180%);
  border-bottom: 1px solid rgba(226, 232, 240, 0.6);
  box-shadow: 0 1px 3px rgba(0,0,0,0.04), 0 1px 0 rgba(255,255,255,0.8) inset;
  transition: box-shadow var(--duration-normal) var(--ease-out);

  &:hover {
    box-shadow: 0 1px 4px rgba(0,0,0,0.06), 0 1px 0 rgba(255,255,255,0.8) inset;
  }
}

.header-inner {
  max-width: var(--page-max-width);
  margin: 0 auto;
  padding: 0 var(--space-xl);
  height: var(--header-height);
  display: flex;
  align-items: center;
  gap: var(--space-lg);
}

/* ===== Logo ===== */
.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  text-decoration: none;
  flex-shrink: 0;
  padding: 6px 12px 6px 6px;
  border-radius: var(--radius-lg);
  transition: background var(--duration-fast) ease;
}
.logo:hover { background: var(--surface-hover); }

.logo-mark {
  display: flex;
  align-items: center;
  filter: drop-shadow(0 2px 4px rgba(14,165,233,0.3));
}

.logo-text {
  font-size: 1.3rem;
  font-weight: 800;
  letter-spacing: -0.03em;
  line-height: 1;
}

.logo-brand { color: var(--text-primary); }

.logo-accent {
  background: linear-gradient(135deg, var(--primary) 0%, #8b5cf6 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

/* ===== Search ===== */
.search-wrapper { flex: 1; max-width: 520px; }

.search-box {
  position: relative;
  display: flex;
  align-items: center;
  background: var(--bg-secondary);
  border: 1px solid transparent;
  border-radius: var(--radius-xl);
  padding: 0 14px;
  height: 42px;
  transition: all var(--duration-normal) var(--ease-out);
}

.search-box:hover {
  background: var(--surface);
  border-color: var(--border);
  box-shadow: var(--shadow-sm);
}

.search-box:focus-within {
  background: var(--surface);
  border-color: var(--primary);
  box-shadow: 0 0 0 3px var(--primary-50), var(--shadow-sm);
}

.search-icon { color: var(--text-muted); font-size: 16px; flex-shrink: 0; }

.search-input {
  flex: 1;
  border: none;
  background: transparent;
  outline: none;
  font-size: 0.9rem;
  color: var(--text-primary);
  padding: 0 8px;
  font-family: inherit;
}

.search-input::placeholder { color: var(--text-muted); }

.search-kbd {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 2px 6px;
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: var(--radius-xs);
  font-size: 0.7rem;
  font-family: var(--font-mono);
  color: var(--text-muted);
  line-height: 1;
}

/* ===== Navigation ===== */
.nav { display: flex; gap: 2px; }

.nav-link {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 14px;
  border-radius: var(--radius-md);
  color: var(--text-secondary);
  font-weight: 500;
  font-size: 0.88rem;
  text-decoration: none;
  transition: all var(--duration-fast) ease;
  position: relative;
}

.nav-link:hover { background: var(--surface-hover); color: var(--text-primary); }

.nav-link.active {
  background: var(--primary-50);
  color: var(--primary);
  font-weight: 600;
}

.nav-link.active::after {
  content: '';
  position: absolute;
  bottom: -1px;
  left: 50%;
  transform: translateX(-50%);
  width: 16px;
  height: 2px;
  background: var(--primary);
  border-radius: var(--radius-full);
}

/* ===== Header Right ===== */
.header-right {
  display: flex;
  align-items: center;
  gap: var(--space-sm);
  margin-left: auto;
}

.icon-btn {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  border: none;
  background: transparent;
  border-radius: var(--radius-md);
  color: var(--text-secondary);
  cursor: pointer;
  transition: all var(--duration-fast) ease;
}

.icon-btn:hover { background: var(--surface-hover); color: var(--text-primary); }

.notif-dot {
  position: absolute;
  top: 8px;
  right: 8px;
  width: 8px;
  height: 8px;
  background: var(--danger);
  border-radius: 50%;
  border: 2px solid var(--surface);
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.1); }
}

/* ===== User Menu ===== */
.user-menu {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 4px 10px 4px 4px;
  border-radius: var(--radius-xl);
  cursor: pointer;
  transition: background var(--duration-fast) ease;
}

.user-menu:hover { background: var(--surface-hover); }

.user-avatar-wrapper { position: relative; }

.user-avatar {
  width: 34px;
  height: 34px;
  border-radius: 50%;
  object-fit: cover;
}

.user-avatar-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, var(--primary), #8b5cf6);
  color: white;
  font-weight: 700;
  font-size: 0.85rem;
}

.user-status-dot {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 10px;
  height: 10px;
  background: var(--success);
  border-radius: 50%;
  border: 2px solid var(--surface);
}

.user-name {
  font-size: 0.88rem;
  font-weight: 600;
  color: var(--text-primary);
  max-width: 120px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* ===== Login Button ===== */
.login-btn {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 8px 20px;
  background: linear-gradient(135deg, var(--primary) 0%, var(--primary-dark) 100%);
  color: white;
  border-radius: var(--radius-xl);
  font-size: 0.88rem;
  font-weight: 600;
  text-decoration: none;
  transition: all var(--duration-normal) var(--ease-out);
  box-shadow: 0 2px 8px rgba(14,165,233,0.3);
}

.login-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(14,165,233,0.4);
  color: white;
}

/* ===== Responsive ===== */
@media (max-width: 768px) {
  .header-inner { padding: 0 var(--space-md); gap: var(--space-sm); }
  .nav-link span { display: none; }
  .search-wrapper { display: none; }
  .user-name { display: none; }
}
</style>
