<template>
  <header class="header">
    <div class="header-inner">
      <NuxtLink to="/" class="logo">
        <svg width="32" height="32" viewBox="0 0 36 36" fill="none">
          <rect width="36" height="36" rx="8" fill="url(#logo-grad)"/>
          <path d="M10 13h16M10 17.5h12M10 22h8" stroke="white" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round"/>
          <defs>
            <linearGradient id="logo-grad" x1="0" y1="0" x2="36" y2="36">
              <stop stop-color="#0ea5e9"/>
              <stop offset="1" stop-color="#8b5cf6"/>
            </linearGradient>
          </defs>
        </svg>
        <span class="logo-text">0x1l<span class="logo-text-accent">Blog</span></span>
      </NuxtLink>

      <div class="search-box">
        <svg class="search-icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg>
        <input
          v-model="searchQuery"
          type="text"
          class="search-input"
          placeholder="Tìm kiếm bài viết, tag, tác giả..."
          @keyup.enter="handleSearch"
        />
        <kbd v-if="!searchQuery" class="search-kbd">⌘K</kbd>
      </div>

      <nav class="header-nav">
        <NuxtLink to="/home" class="header-nav-link" active-class="header-nav-link--active">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="m3 9 9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/><polyline points="9 22 9 12 15 12 15 22"/></svg>
        </NuxtLink>
        <NuxtLink to="/archives" class="header-nav-link" active-class="header-nav-link--active">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M4 19.5v-15A2.5 2.5 0 0 1 6.5 2H20v20H6.5a2.5 2.5 0 0 1 0-5H20"/></svg>
        </NuxtLink>
        <NuxtLink to="/about" class="header-nav-link" active-class="header-nav-link--active">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><path d="M12 16v-4"/><path d="M12 8h.01"/></svg>
        </NuxtLink>
      </nav>

      <div class="header-actions">
        <ThemeToggle />
        <template v-if="isLoggedIn">
          <button class="header-icon-btn" @click="navigateTo('/notifications')" title="Thông báo">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M6 8a6 6 0 0 1 12 0c0 7 3 9 3 9H3s3-2 3-9"/><path d="M10.3 21a1.94 1.94 0 0 0 3.4 0"/></svg>
            <span v-if="unreadCount > 0" class="header-notif-dot"></span>
          </button>

          <el-dropdown trigger="click" @command="handleCommand" placement="bottom-end">
            <div class="header-user">
              <img v-if="user?.avatarUrl" :src="user.avatarUrl" class="header-user-avatar" />
              <div v-else class="header-user-avatar header-user-avatar--placeholder">{{ user?.displayName?.charAt(0) || 'U' }}</div>
              <span class="header-user-name">{{ user?.displayName }}</span>
              <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><polyline points="6 9 12 15 18 9"/></svg>
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
          <NuxtLink to="/login" class="login-btn">Đăng nhập</NuxtLink>
        </template>
      </div>
    </div>
  </header>
</template>

<script setup lang="ts">
const authStore = useAuthStore()
const searchQuery = ref('')
const unreadCount = ref(3)
const isLoggedIn = computed(() => authStore.isLoggedIn)
const user = computed(() => authStore.user)

const handleSearch = () => {
  if (searchQuery.value.trim()) navigateTo({ path: '/search', query: { q: searchQuery.value } })
}

const handleCommand = (cmd: string) => {
  if (cmd === 'profile') navigateTo(`/profile/${user.value?.id}`)
  else if (cmd === 'bookmarks') navigateTo('/bookmarks')
  else if (cmd === 'settings') navigateTo('/settings')
  else if (cmd === 'logout') { authStore.logout(); navigateTo('/home') }
}
</script>

<style scoped lang="scss">
.header {
  position: sticky;
  top: 0;
  z-index: var(--z-sticky);
  background: var(--surface);
  border-bottom: 1px solid var(--border-light);
}

.header-inner {
  max-width: var(--page-max-width);
  margin: 0 auto;
  padding: 0 24px;
  height: 52px;
  display: flex;
  align-items: center;
  gap: 16px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  text-decoration: none;
  flex-shrink: 0;
}
.logo-text {
  font-size: 1.15rem;
  font-weight: 800;
  color: var(--text-primary);
  letter-spacing: -0.02em;
}
.logo-text-accent { color: var(--primary); }

.search-box {
  flex: 1;
  max-width: 420px;
  display: flex;
  align-items: center;
  gap: 8px;
  background: var(--bg-secondary);
  border: 1px solid transparent;
  border-radius: 6px;
  padding: 0 12px;
  height: 36px;
  transition: all 0.15s;
  &:hover { background: var(--surface); border-color: var(--border); }
  &:focus-within { background: var(--surface); border-color: var(--primary); box-shadow: 0 0 0 2px var(--primary-50); }
}
.search-icon { color: var(--text-muted); flex-shrink: 0; }
.search-input {
  flex: 1; border: none; background: transparent; outline: none;
  font-size: 0.85rem; color: var(--text-primary); font-family: inherit;
  &::placeholder { color: var(--text-muted); }
}
.search-kbd {
  padding: 2px 6px; background: var(--surface); border: 1px solid var(--border);
  border-radius: 3px; font-size: 0.7rem; font-family: var(--font-mono);
  color: var(--text-muted); line-height: 1;
}

.header-nav {
  display: flex;
  gap: 2px;
}
.header-nav-link {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  border-radius: 6px;
  color: var(--text-muted);
  text-decoration: none;
  transition: all 0.12s;
  &:hover { background: var(--bg-secondary); color: var(--text-primary); }
  &.header-nav-link--active { color: var(--primary); }
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-left: auto;
}

.header-icon-btn {
  position: relative;
  display: flex; align-items: center; justify-content: center;
  width: 36px; height: 36px;
  border: none; background: transparent; border-radius: 6px;
  color: var(--text-muted); cursor: pointer;
  transition: all 0.12s;
  &:hover { background: var(--bg-secondary); color: var(--text-primary); }
}
.header-notif-dot {
  position: absolute; top: 6px; right: 6px;
  width: 8px; height: 8px; background: var(--danger);
  border-radius: 50%; border: 2px solid var(--surface);
}

.header-user {
  display: flex; align-items: center; gap: 8px;
  padding: 4px 8px 4px 4px; border-radius: 6px;
  cursor: pointer; transition: background 0.12s;
  &:hover { background: var(--bg-secondary); }
}
.header-user-avatar { width: 32px; height: 32px; border-radius: 50%; object-fit: cover; }
.header-user-avatar--placeholder {
  display: flex; align-items: center; justify-content: center;
  background: linear-gradient(135deg, var(--primary), #8b5cf6);
  color: #fff; font-weight: 700; font-size: 0.8rem;
}
.header-user-name {
  font-size: 0.85rem; font-weight: 500; color: var(--text-primary);
  max-width: 100px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;
}

.login-btn {
  display: inline-flex; align-items: center;
  padding: 7px 20px; background: var(--primary);
  color: white; border-radius: 6px;
  font-size: 0.85rem; font-weight: 600;
  text-decoration: none; transition: all 0.15s;
  &:hover { background: var(--primary-dark); color: white; }
}

@media (max-width: 768px) {
  .header-inner { padding: 0 12px; gap: 8px; }
  .header-nav { display: none; }
  .search-box { display: none; }
  .header-user-name { display: none; }
}
</style>
