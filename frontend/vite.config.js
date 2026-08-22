import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { fileURLToPath, URL } from 'node:url'

// 构建产物直接输出到控制台后端的静态资源目录，由 Spring Boot 托管；
// 开发态通过 dev server 的 /api 代理转发到控制台后端（:8081）。
export default defineConfig({
  plugins: [vue()],
  base: './',
  resolve: {
    alias: { '@': fileURLToPath(new URL('./src', import.meta.url)) }
  },
  build: {
    outDir: '../src/main/resources/static',
    emptyOutDir: true
  },
  server: {
    port: 5173,
    proxy: {
      '/api': 'http://localhost:8081'
    }
  }
})
