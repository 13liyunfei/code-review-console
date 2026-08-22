<template>
  <div>
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px">
      <div>{{ t('skills.count', { n: skills.length }) }}</div>
      <el-button type="primary" @click="dialogVisible = true">{{ t('skills.addCustom') }}</el-button>
    </div>

    <el-card>
      <el-table :data="skills" border>
        <el-table-column prop="name" :label="t('skills.col.name')" width="200" />
        <el-table-column :label="t('skills.col.category')" width="110">
          <template #default="{ row }"><el-tag>{{ catLabel(row.category) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="description" :label="t('skills.col.description')" />
        <el-table-column :label="t('skills.col.type')" width="90">
          <template #default="{ row }">
            <el-tag v-if="row.custom" type="warning">{{ t('skills.custom') }}</el-tag>
            <el-tag v-else type="info">{{ t('skills.builtin') }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column :label="t('skills.col.enabled')" width="90">
          <template #default="{ row }">
            <el-switch :model-value="row.enabled" @change="(v) => onToggle(row, v)" />
          </template>
        </el-table-column>
        <el-table-column :label="t('skills.col.action')" width="100">
          <template #default="{ row }">
            <el-button v-if="row.custom" type="danger" size="small" @click="onDelete(row)">{{ t('skills.delete') }}</el-button>
            <span v-else>—</span>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-card style="margin-top: 16px">
      <template #header>{{ t('skills.yamlHeader') }}</template>
      <el-alert type="info" :closable="false" show-icon style="margin-bottom: 12px"
        :title="t('skills.yamlHint')" />
      <el-input v-model="yamlText" type="textarea" :rows="8" :placeholder="t('skills.yamlPlaceholder')" />
      <div style="margin-top: 12px; text-align: right">
        <el-button :disabled="!yamlText.trim()" type="success" @click="onImportYaml">
          {{ t('skills.yamlImport') }}
        </el-button>
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="t('skills.dialogTitle')" width="540px">
      <el-form :model="form" label-width="92px">
        <el-form-item :label="t('skills.form.name')"><el-input v-model="form.name" :placeholder="t('skills.form.name')" /></el-form-item>
        <el-form-item :label="t('skills.form.category')">
          <el-select v-model="form.category" style="width: 100%">
            <el-option v-for="c in cats" :key="c.value" :label="c.label" :value="c.value" />
          </el-select>
        </el-form-item>
        <el-form-item :label="t('skills.form.severity')">
          <el-select v-model="form.severity" style="width: 100%">
            <el-option label="BLOCKER" value="BLOCKER" />
            <el-option label="MAJOR" value="MAJOR" />
            <el-option label="MINOR" value="MINOR" />
            <el-option label="INFO" value="INFO" />
          </el-select>
        </el-form-item>
        <el-form-item :label="t('skills.form.pattern')">
          <el-input v-model="form.pattern" placeholder="e.g. (?i)select\s+\*\s+from" />
        </el-form-item>
        <el-form-item :label="t('skills.form.title')"><el-input v-model="form.title" /></el-form-item>
        <el-form-item :label="t('skills.form.description')"><el-input v-model="form.description" type="textarea" /></el-form-item>
        <el-form-item :label="t('skills.form.suggestion')"><el-input v-model="form.suggestion" type="textarea" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">{{ t('skills.cancel') }}</el-button>
        <el-button type="primary" @click="onSubmit">{{ t('skills.submit') }}</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useI18n } from 'vue-i18n'
import { ElMessage } from 'element-plus'
import { getSkills, toggleSkill, addCustom, removeCustom, importYaml } from '@/api'

const { t } = useI18n()
const skills = ref([])
const dialogVisible = ref(false)
const yamlText = ref('')
const cats = ['security', 'logic', 'performance', 'style', 'architecture'].map((v) => ({
  value: v,
  label: t(`cats.${v}`)
}))
const form = ref({
  name: '', category: 'security', severity: 'MAJOR',
  pattern: '', title: '', description: '', suggestion: ''
})

const catLabel = (c) => t(`cats.${c}`) !== `cats.${c}` ? t(`cats.${c}`) : (cats.find((x) => x.value === c) || {}).label || c

const load = async () => { skills.value = await getSkills() }
onMounted(load)

const onToggle = async (row, val) => {
  try {
    await toggleSkill(row.name, val)
    row.enabled = val
    ElMessage.success(t('skills.msg.toggled'))
  } catch (e) {
    ElMessage.error(t('skills.msg.fail'))
  }
}
const onDelete = async (row) => {
  await removeCustom(row.id)
  ElMessage.success(t('skills.msg.deleted'))
  load()
}
const onSubmit = async () => {
  if (!form.value.name || !form.value.pattern) {
    ElMessage.warning(t('skills.msg.required'))
    return
  }
  await addCustom(form.value)
  ElMessage.success(t('skills.msg.added'))
  dialogVisible.value = false
  load()
}

const onImportYaml = async () => {
  try {
    const res = await importYaml(yamlText.value)
    const ok = res.imported || 0
    const errs = res.errors || []
    if (errs.length) {
      ElMessage.warning(t('skills.msg.importPartial', { ok, err: errs.length, errs: errs.join('；') }))
    } else {
      ElMessage.success(t('skills.msg.importOk', { ok }))
    }
    yamlText.value = ''
    load()
  } catch (e) {
    ElMessage.error(t('skills.msg.importFail'))
  }
}
</script>
