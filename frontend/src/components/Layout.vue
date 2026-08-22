<template>
  <el-container style="height: 100vh">
    <el-aside width="220px" style="background: #001529">
      <div style="color: #fff; font-size: 18px; font-weight: 600; padding: 18px 16px">
        代码审查控制台
      </div>
      <el-menu
        background-color="#001529"
        text-color="#fff"
        active-text-color="#409EFF"
        router
        :default-active="active"
      >
        <el-menu-item index="/dashboard">
          <el-icon><DataLine /></el-icon><span>仪表盘</span>
        </el-menu-item>
        <el-menu-item index="/skills">
          <el-icon><Files /></el-icon><span>Skills 市场</span>
        </el-menu-item>
        <el-menu-item index="/knowledge">
          <el-icon><Folder /></el-icon><span>团队知识</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header
        style="display: flex; align-items: center; border-bottom: 1px solid #eee; font-weight: 600; font-size: 16px"
      >
        {{ title }}
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
import { DataLine, Files, Folder } from '@element-plus/icons-vue'

const route = useRoute()
const title = computed(() => route.meta.title || '控制台')
const active = computed(() => route.path)
</script>
