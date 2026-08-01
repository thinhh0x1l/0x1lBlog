export default defineNuxtConfig({
  compatibilityDate: '2025-07-15',
  devtools: { enabled: false },

  modules: [
    '@element-plus/nuxt',
    '@pinia/nuxt',
    '@vueuse/nuxt',
  ],

  css: [
    '~/assets/scss/index.scss',
    'element-plus/dist/index.css',
    'nprogress/nprogress.css',
  ],

  app: {
    head: {
      htmlAttrs: { lang: 'vi' },
      title: '0x1l Blog',
    },
  },

  runtimeConfig: {
    public: {
      apiBase: 'http://localhost:8090/api',
    },
  },

  components: {
    dirs: [
      { path: '~/components', pathPrefix: false },
    ],
  },

  elementPlus: {
    importStyle: 'scss',
  },

  routeRules: {
    '/home': { ssr: false },
    '/blog/**': { ssr: false },
    '/profile/**': { ssr: false },
    '/search': { ssr: false },
    '/trending': { ssr: false },
    '/series': { ssr: false },
    '/series/**': { ssr: false },
    '/category/**': { ssr: false },
    '/tag/**': { ssr: false },
    '/music': { ssr: false },
    '/music/**': { ssr: false },
    '/archives': { ssr: false },
    '/notifications': { ssr: false },
    '/bookmarks': { ssr: false },
    '/settings': { ssr: false },
    '/canvas': { ssr: false },
    '/about': { ssr: false },
    '/terms': { ssr: false },
    '/privacy': { ssr: false },
    '/login': { ssr: false },
    '/oauth2/**': { ssr: false },
    '/tiptap': { ssr: false },
  },

  vite: {
    optimizeDeps: {
      include: [
        '@tiptap/starter-kit',
        '@tiptap/vue-3',
        'dayjs', // CJS
        'dayjs/plugin/*.js',
        'nprogress', // CJS
      ]
    },
    css: {
      preprocessorOptions: {
        scss: {
          additionalData: `@use "~/assets/scss/_variables.scss" as *; @use "~/assets/scss/_mixins.scss" as *;`,
          silenceDeprecations: ['import'],
        },
      },
    },
  },
})
