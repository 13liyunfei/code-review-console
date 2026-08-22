<template>
  <div>
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px">
      <div>共 {{ skills.length }} 个技能（内置 + 团队自定义），启停即时生效并持久化</div>
      <el-button type="primary" @click="dialogVisible = true">+ 新增自定义规则</el-button>
    </div>

    <el-card>
      <el-table :data="skills" border>
        <el-table-column prop="name" label="名称" width="200" />
        <el-table-column label="维度" width="110">
          <template #default="{ row }"><el-tag>{{ catLabel(row.category) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="description" label="说明" />
        <el-table-column label="类型" width="90">
          <template #default="{ row }">
            <el-tag v-if="row.custom" type="warning">自定义</el-tag>
            <el-tag v-else type="info">内置</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="启用" width="90">
          <template #default="{ row }">
            <el-switch :model-value="row.enabled" @change="(v) => onToggle(row, v)" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-button v-if="row.custom" type="danger" size="small" @click="onDelete(row)">删除</el-button>
            <span v-else>—</span>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-card style="margin-top: 16px">
      <template #header>YAML 低代码规则批量导入</template>
      <el-alert type="info" :closable="false" show-icon style="margin-bottom: 12px"
        title="安全 / 合规团队无需编写 Java：维护一份 YAML 规则清单即可注入审查引擎（即时生效 + 持久化）" />
      <el-input v-model="yamlText" type="textarea" :rows="8"
        placeholder="rules:&#10;  - name: 禁止提交 TODO&#10;    category: style&#10;    severity: MINOR&#10;    pattern: '(?i)//.*\b(todo|fixme)\b'&#10;    title: 遗留 TODO 标记&#10;    description: 提交中仍包含待办标记&#10;    suggestion: 登记到任务系统并移除" />
      <div style="margin-top: 12px; text-align: right">
        <el-button :disabled="!yamlText.trim()" type="success" @click="onImportYaml">
          导入 YAML 规则
        </el-button>
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" title="新增团队自定义规则" width="540px">
      <el-form :model="form" label-width="92px">
        <el-form-item label="规则名"><el-input v-model="form.name" placeholder="如 禁止提交 TODO" /></el-form-item>
        <el-form-item label="维度">
          <el-select v-model="form.category" style="width: 100%">
            <el-option v-for="c in cats" :key="c.value" :label="c.label" :value="c.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="严重级别">
          <el-select v-model="form.severity" style="width: 100%">
            <el-option label="BLOCKER" value="BLOCKER" />
            <el-option label="MAJOR" value="MAJOR" />
            <el-option label="MINOR" value="MINOR" />
            <el-option label="INFO" value="INFO" />
          </el-select>
        </el-form-item>
        <el-form-item label="匹配正则">
          <el-input v-model="form.pattern" placeholder="如 (?i)select\s+\*\s+from" />
        </el-form-item>
        <el-form-item label="标题"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="描述"><el-input v-model="form.description" type="textarea" /></el-form-item>
        <el-form-item label="修复建议"><el-input v-model="form.suggestion" type="textarea" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="onSubmit">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getSkills, toggleSkill, addCustom, removeCustom, importYaml } from '@/api'

const skills = ref([])
const dialogVisible = ref(false)
const yamlText = ref('')
const cats = [
  { label: '安全', value: 'security' },
  { label: '逻辑', value: 'logic' },
  { label: '性能', value: 'performance' },
  { label: '规范', value: 'style' },
  { label: '架构', value: 'architecture' }
]
const form = ref({
  name: '', category: 'security', severity: 'MAJOR',
  pattern: '', title: '', description: '', suggestion: ''
})

const catLabel = (c) => (cats.find((x) => x.value === c) || {}).label || c

const load = async () => { skills.value = await getSkills() }
onMounted(load)

const onToggle = async (row, val) => {
  try {
    await toggleSkill(row.name, val)
    row.enabled = val
    ElMessage.success('已更新启停状态')
  } catch (e) {
    ElMessage.error('操作失败')
  }
}
const onDelete = async (row) => {
  await removeCustom(row.id)
  ElMessage.success('已删除')
  load()
}
const onSubmit = async () => {
  if (!form.value.name || !form.value.pattern) {
    ElMessage.warning('请填写规则名与匹配正则')
    return
  }
  await addCustom(form.value)
  ElMessage.success('已新增自定义规则')
  dialogVisible.value = false
  load()
}

const onImportYaml = async () => {
  try {
    const res = await importYaml(yamlText.value)
    const ok = res.imported || 0
    const errs = res.errors || []
    if (errs.length) {
      ElMessage.warning(`导入 ${ok} 条，失败 ${errs.length} 条：${errs.join('；')}`)
    } else {
      ElMessage.success(`成功导入 ${ok} 条 YAML 规则`)
    }
    yamlText.value = ''
    load()
  } catch (e) {
    ElMessage.error('YAML 导入失败')
  }
}
</script>
