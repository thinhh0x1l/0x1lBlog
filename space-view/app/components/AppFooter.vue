<template>
  <footer class="footer">
    <div class="footer-inner">
      <div class="footer-links">
        <NuxtLink to="/about">Về tôi</NuxtLink>
        <NuxtLink to="/archives">Lưu trữ</NuxtLink>
        <NuxtLink to="/terms">Điều khoản</NuxtLink>
        <NuxtLink to="/privacy">Quyền riêng tư</NuxtLink>
        <a href="https://github.com/thinhh0x1l" target="_blank">GitHub</a>
      </div>
      <p class="footer-copy">© {{ currentYear }} 0x1lBlog. Được xây dựng với ❤️</p>
    </div>
  </footer>
</template>

<script setup lang="ts">
const currentYear = computed(() => new Date().getFullYear())
</script>

<style scoped lang="scss">
.footer {
  border-top: 1px solid var(--border-light);
  background: var(--surface);
}
.footer-inner {
  max-width: var(--page-max-width);
  margin: 0 auto;
  padding: 24px;
  text-align: center;
}
.footer-links {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  gap: 24px;
  margin-bottom: 12px;
}
.footer-links a {
  font-size: 0.85rem;
  color: var(--text-muted);
  text-decoration: none;
  transition: color 0.12s;
  &:hover { color: var(--primary); }
}
.footer-copy {
  font-size: 0.8rem;
  color: var(--text-muted);
}
</style>
