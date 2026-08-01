import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
      'router': fileURLToPath(new URL('./src/router', import.meta.url)),
      'store': fileURLToPath(new URL('./src/store', import.meta.url))
    },
    extensions: ['.ts', '.js', '.vue', '.json']
  },
  css: {
    preprocessorOptions: {
      scss: {
        additionalData: `@import "@/assets/scss/_variables.scss";\n@import "@/assets/scss/_mixins.scss";\n`,
        api: 'modern-compiler',
        silenceDeprecations: ['import']
      }
    }
  },
  server: {
    host: true,
    port: 5174
  }
})
