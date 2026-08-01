<template>
  <footer class="footer">
    <div class="footer-inner">
      <div class="footer-content">
        <div class="footer-brand">
          <div class="footer-logo">
            <svg width="24" height="24" viewBox="0 0 32 32" fill="none"><rect width="32" height="32" rx="8" fill="url(#fg)"/><path d="M8 12h16M8 16h12M8 20h8" stroke="white" stroke-width="2" stroke-linecap="round"/><defs><linearGradient id="fg" x1="0" y1="0" x2="32" y2="32"><stop stop-color="#0ea5e9"/><stop offset="1" stop-color="#8b5cf6"/></linearGradient></defs></svg>
            <span>0x1lBlog</span>
          </div>
          <p class="footer-desc">Nơi chia sẻ kiến thức và kinh nghiệm lập trình</p>
        </div>
        <div class="footer-links">
          <a href="https://github.com/thinhh0x1l" target="_blank">GitHub</a>
          <router-link to="/about">Về tôi</router-link>
          <router-link to="/archives">Lưu trữ</router-link>
        </div>
      </div>
      <div class="footer-bottom">
        <span>© {{ currentYear }} 0x1lBlog</span>
      </div>
    </div>
  </footer>
</template>
<script setup>
import { computed } from 'vue'
const currentYear = computed(() => new Date().getFullYear())
</script>
<style scoped lang="scss">
.footer { background: var(--surface); border-top: 1px solid var(--border-light); margin-top: var(--space-2xl); }
.footer-inner { max-width: var(--page-max-width); margin: 0 auto; padding: var(--space-xl) var(--space-lg); }
.footer-content { display: flex; justify-content: space-between; align-items: flex-start; gap: var(--space-xl); margin-bottom: var(--space-lg); }
.footer-brand { max-width: 300px; }
.footer-logo { display: flex; align-items: center; gap: var(--space-sm); font-size: 1.125rem; font-weight: 700; margin-bottom: var(--space-sm); }
.footer-desc { font-size: 0.875rem; color: var(--text-muted); }
.footer-links { display: flex; gap: var(--space-lg); }
.footer-links a { font-size: 0.875rem; color: var(--text-secondary); }
.footer-links a:hover { color: var(--primary); }
.footer-bottom { padding-top: var(--space-md); border-top: 1px solid var(--border-light); text-align: center; }
.footer-bottom span { font-size: 0.8rem; color: var(--text-muted); }
</style>
