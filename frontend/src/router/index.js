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
      { path: 'dashboard', name: 'dashboard', component: Dashboard, meta: { title: 'menu.dashboard' } },
      { path: 'skills', name: 'skills', component: SkillsMarket, meta: { title: 'menu.skills' } },
      { path: 'knowledge', name: 'knowledge', component: Knowledge, meta: { title: 'menu.knowledge' } }
    ]
  }
]

export default createRouter({
  history: createWebHashHistory(),
  routes
})
