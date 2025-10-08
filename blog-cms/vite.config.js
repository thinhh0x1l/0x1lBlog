import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
      'components': fileURLToPath(new URL('./src/components', import.meta.url)),
      'network': fileURLToPath(new URL('./src/network', import.meta.url)),
      'plugins': fileURLToPath(new URL('./src/plugins', import.meta.url)),
      'router': fileURLToPath(new URL('./src/router', import.meta.url)),
    },
  },
})
