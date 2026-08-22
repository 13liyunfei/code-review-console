<template>
  <div>
    <el-row :gutter="16">
      <el-col :span="4">
        <el-card><el-statistic :title="t('dash.skillTotal')" :value="stats.skillTotal" /></el-card>
      </el-col>
      <el-col :span="4">
        <el-card><el-statistic :title="t('dash.skillEnabled')" :value="stats.skillEnabled" /></el-card>
      </el-col>
      <el-col :span="4">
        <el-card><el-statistic :title="t('dash.skillCustom')" :value="stats.skillCustom" /></el-card>
      </el-col>
      <el-col :span="4">
        <el-card><el-statistic :title="t('dash.knowledgeTotal')" :value="stats.knowledgeTotal" /></el-card>
      </el-col>
      <el-col :span="4">
        <el-card><el-statistic :title="t('dash.knowledgeIndexed')" :value="stats.knowledgeIndexed" /></el-card>
      </el-col>
    </el-row>

    <el-card style="margin-top: 16px" :header="t('dash.categoryDist')">
      <el-table :data="byCategory" border>
        <el-table-column prop="category" :label="t('dash.col.category')" width="160" />
        <el-table-column prop="total" :label="t('dash.col.total')" />
        <el-table-column prop="enabled" :label="t('dash.col.enabled')" />
        <el-table-column prop="custom" :label="t('dash.col.custom')" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useI18n } from 'vue-i18n'
import { getStats, getSkills } from '@/api'

const { t } = useI18n()
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
