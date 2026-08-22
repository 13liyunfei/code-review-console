import { createI18n } from 'vue-i18n'
import zh from './locales/zh'
import en from './locales/en'

// 语言持久化：localStorage 优先，默认中文
const saved = typeof localStorage !== 'undefined' ? localStorage.getItem('console-lang') : null

export default createI18n({
  legacy: false,
  globalInjection: true,
  locale: saved || 'zh',
  fallbackLocale: 'zh',
  messages: { zh, en }
})
