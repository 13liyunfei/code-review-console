import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import router from './router'
import i18n from './i18n'
import axios from 'axios'

axios.defaults.baseURL = ''
window.axios = axios

createApp(App).use(router).use(ElementPlus).use(i18n).mount('#app')
