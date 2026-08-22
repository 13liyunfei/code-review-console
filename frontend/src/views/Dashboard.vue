<template>
  <div>
    <el-row :gutter="16">
      <el-col :span="4">
        <el-card><el-statistic title="技能总数" :value="stats.skillTotal" /></el-card>
      </el-col>
      <el-col :span="4">
        <el-card><el-statistic title="已启用" :value="stats.skillEnabled" /></el-card>
      </el-col>
      <el-col :span="4">
        <el-card><el-statistic title="自定义规则" :value="stats.skillCustom" /></el-card>
      </el-col>
      <el-col :span="4">
        <el-card><el-statistic title="知识总数" :value="stats.knowledgeTotal" /></el-card>
      </el-col>
      <el-col :span="4">
        <el-card><el-statistic title="已索引(RAG)" :value="stats.knowledgeIndexed" /></el-card>
      </el-col>
    </el-row>

    <el-card style="margin-top: 16px" header="技能维度分布">
      <el-table :data="byCategory" border>
        <el-table-column prop="category" label="维度" width="160" />
        <el-table-column prop="total" label="技能数" />
        <el-table-column prop="enabled" label="已启用" />
        <el-table-column prop="custom" label="自定义" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getStats, getSkills } from '@/api'

const stats = ref({
  skillTotal: 0, skillEnabled: 0, skillCustom: 0,
  knowledgeTotal: 0, knowledgeIndexed: 0
})
const byCategory = ref([])

onMounted(async () => {
  stats.value = await getStats()
  const skills = await getSkills()
  const map = {}
  for (const s of skills) {
    const c = s.category
    if (!map[c]) map[c] = { category: c, total: 0, enabled: 0, custom: 0 }
    map[c].total++
    if (s.enabled) map[c].enabled++
    if (s.custom) map[c].custom++
  }
  byCategory.value = Object.values(map)
})
</script>
