import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'
import createAutoImport from './auto-import'
import svgLoader from 'vite-svg-loader'

export default function createVitePlugins(viteEnv, isBuild = false) {
  return [
      vue(),
      vueDevTools(),

      svgLoader({
          defaultImport: 'component'  // Import mặc định dưới dạng Vue component
      }),
      createAutoImport()
  ]
}
