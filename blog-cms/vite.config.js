import { defineConfig } from 'vite'
import createVitePlugins from "./vite/plugins/index.js";
import createViteResolve from "./vite/resolve/index.js";

// https://vite.dev/config/
export default defineConfig({
  plugins: createVitePlugins('env',false),

  resolve: createViteResolve(),

  server: {
    host: true,
    port: 5173
  }
})
