<template>
  <el-container style="height: 100vh">
    <el-aside width="220px" style="background: #001529">
      <div style="color: #fff; font-size: 18px; font-weight: 600; padding: 18px 16px">
        {{ t('app.name') }}
      </div>
      <el-menu
        background-color="#001529"
        text-color="#fff"
        active-text-color="#409EFF"
        router
        :default-active="active"
      >
        <el-menu-item index="/dashboard">
          <el-icon><DataLine /></el-icon><span>{{ t('menu.dashboard') }}</span>
        </el-menu-item>
        <el-menu-item index="/skills">
          <el-icon><Files /></el-icon><span>{{ t('menu.skills') }}</span>
        </el-menu-item>
        <el-menu-item index="/agents">
          <el-icon><User /></el-icon><span>{{ t('menu.agents') }}</span>
        </el-menu-item>
        <el-menu-item index="/knowledge">
          <el-icon><Folder /></el-icon><span>{{ t('menu.knowledge') }}</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header
        style="display: flex; align-items: center; justify-content: space-between; border-bottom: 1px solid #eee; font-weight: 600; font-size: 16px"
      >
        <span>{{ title }}</span>
        <el-button size="small" @click="toggleLang">{{ t('lang.switchTo') }}</el-button>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useRoute } from 'vue-router'
import { computed } from 'vue'
import { useI18n } from 'vue-i18n'
import { DataLine, Files, Folder, User } from '@element-plus/icons-vue'

const { t, locale } = useI18n()
const route = useRoute()
const title = computed(() => {
  const key = route.meta.title
  return key ? t(key) : t('menu.dashboard')
})
const active = computed(() => route.path)

const toggleLang = () => {
  locale.value = locale.value === 'zh' ? 'en' : 'zh'
  localStorage.setItem('console-lang', locale.value)
}
</script>
