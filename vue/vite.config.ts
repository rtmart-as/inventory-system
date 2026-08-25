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
  server: {
    port: 8081,
    host: '0.0.0.0', // 解决 use --host to expose问题
    hmr: true, // 热更新
    open: true, // 自动打开浏览器
    proxy: {
      '/api': {
        target: 'http://localhost:8089', //后端地址：ychs-base-parent(IDEA中启动)，端口8089
        // 使用EasyMock地址
        // target: 'https://www.fastmock.site/mock/8c70dce529c6c4880cfe889aa62b4103/api',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }

  },
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  define: {
    'process.env': {
      'BASE_API_IMG': "http://localhost:8089",
      'BASE_API':"http://localhost:8089"
    }
  }
})
