import { createRouter, createWebHashHistory } from 'vue-router'
import Layout from '@/components/Layout.vue'
import Dashboard from '@/views/Dashboard.vue'
import SkillsMarket from '@/views/SkillsMarket.vue'
import Knowledge from '@/views/Knowledge.vue'

const routes = [
  {
    path: '/',
    component: Layout,
    children: [
      { path: '', redirect: '/dashboard' },
      { path: 'dashboard', name: 'dashboard', component: Dashboard, meta: { title: '仪表盘' } },
      { path: 'skills', name: 'skills', component: SkillsMarket, meta: { title: 'Skills 市场' } },
      { path: 'knowledge', name: 'knowledge', component: Knowledge, meta: { title: '团队知识' } }
    ]
  }
]

export default createRouter({
  history: createWebHashHistory(),
  routes
})
