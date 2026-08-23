<template>
  <div>
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px">
      <div>
        {{ t('agents.count', { n: agents.length }) }}
        <el-tag size="small" type="info" style="margin-left: 8px">{{ t('agents.teamHint') }}</el-tag>
      </div>
      <el-button type="primary" @click="openCreate">{{ t('agents.add') }}</el-button>
    </div>

    <el-card>
      <el-alert
        v-if="listError"
        type="error"
        :closable="false"
        show-icon
        :title="listError"
        style="margin-bottom: 12px"
      />
      <el-table :data="agents" border>
        <el-table-column prop="name" :label="t('agents.col.name')" width="200" />
        <el-table-column :label="t('agents.col.focus')" min-width="240">
          <template #default="{ row }">
            <el-tag
              v-for="(f, i) in row.focusPoints.slice(0, 3)"
              :key="i"
              size="small"
              type="warning"
              style="margin: 2px 4px 2px 0"
            >{{ f }}</el-tag>
            <el-tag v-if="row.focusPoints.length > 3" size="small" type="info">
              +{{ row.focusPoints.length - 3 }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column :label="t('agents.col.severity')" width="100">
          <template #default="{ row }">
            <el-tag :type="severityType(row.severityBias)">{{ row.severityBias || 'MAJOR' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column :label="t('agents.col.enabled')" width="90">
          <template #default="{ row }">
            <el-switch :model-value="row.enabled" @change="(v) => onToggle(row, v)" />
          </template>
        </el-table-column>
        <el-table-column prop="version" :label="t('agents.col.version')" width="80" />
        <el-table-column :label="t('agents.col.updated')" width="150">
          <template #default="{ row }">{{ fmtTime(row.updatedAt) }}</template>
        </el-table-column>
        <el-table-column :label="t('agents.col.action')" width="150">
          <template #default="{ row }">
            <el-button size="small" @click="openEdit(row)">{{ t('agents.edit') }}</el-button>
            <el-button size="small" type="danger" @click="onDelete(row)">{{ t('agents.delete') }}</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-card style="margin-top: 16px">
      <template #header>{{ t('agents.securityHeader') }}</template>
      <el-alert type="info" :closable="false" show-icon :title="t('agents.securityHint')" />
      <ul style="color: #606266; line-height: 2; margin: 10px 0 0; padding-left: 18px">
        <li v-for="(s, i) in securityList" :key="i">{{ s }}</li>
      </ul>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="editingId ? t('agents.editTitle') : t('agents.addTitle')" width="560px">
      <el-form :model="form" label-width="96px">
        <el-form-item :label="t('agents.form.name')">
          <el-input v-model="form.name" :placeholder="t('agents.form.name')" />
        </el-form-item>
        <el-form-item :label="t('agents.form.description')">
          <el-input v-model="form.description" type="textarea" :rows="2" :placeholder="t('agents.form.description')" />
        </el-form-item>
        <el-form-item :label="t('agents.form.focus')">
          <el-input
            v-model="form.focusText"
            type="textarea"
            :rows="4"
            :placeholder="t('agents.form.focusPlaceholder')"
          />
        </el-form-item>
        <el-form-item :label="t('agents.form.severity')">
          <el-select v-model="form.severityBias" style="width: 100%">
            <el-option v-for="s in severities" :key="s" :label="s" :value="s" />
          </el-select>
        </el-form-item>
        <el-form-item :label="t('agents.form.enabled')">
          <el-switch v-model="form.enabled" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">{{ t('agents.cancel') }}</el-button>
        <el-button type="primary" @click="onSubmit">{{ t('agents.submit') }}</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useI18n } from 'vue-i18n'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getCustomAgents,
  createCustomAgent,
  updateCustomAgent,
  deleteCustomAgent,
  toggleCustomAgent
} from '@/api'

const { t, tm } = useI18n()
const agents = ref([])
const listError = ref('')
const dialogVisible = ref(false)
const editingId = ref(null)
const severities = ['BLOCKER', 'MAJOR', 'MINOR', 'INFO']
const form = ref(emptyForm())
// i18n 数组值用 tm() 拿原始数组（t() 在模板里不会展开数组，会把 key 字符串逐字渲染）
const securityList = computed(() => tm('agents.securityList') || [])

function emptyForm() {
  return { name: '', description: '', focusText: '', severityBias: 'MAJOR', enabled: true }
}

const severityType = (s) =>
  s === 'BLOCKER' ? 'danger' : s === 'MAJOR' ? 'warning' : s === 'MINOR' ? 'info' : 'default'

const fmtTime = (ts) => (ts ? String(ts).replace('T', ' ').slice(0, 16) : '—')

const load = async () => {
  listError.value = ''
  try {
    const data = await getCustomAgents()
    if (data && data.error) {
      listError.value = data.message || t('agents.msg.fail')
      agents.value = []
      return
    }
    agents.value = Array.isArray(data) ? data : []
  } catch (e) {
    listError.value = extractMsg(e, t('agents.msg.fail'))
    agents.value = []
  }
}
onMounted(load)

const openCreate = () => {
  editingId.value = null
  form.value = emptyForm()
  dialogVisible.value = true
}

const openEdit = (row) => {
  editingId.value = row.id
  form.value = {
    name: row.name || '',
    description: row.description || '',
    focusText: (row.focusPoints || []).join('\n'),
    severityBias: row.severityBias || 'MAJOR',
    enabled: row.enabled !== false
  }
  dialogVisible.value = true
}

const onToggle = async (row, val) => {
  try {
    const res = await toggleCustomAgent(row.id, val)
    if (res.data && res.data.error) {
      ElMessage.error(extractMsg(res.data, t('agents.msg.fail')))
      return
    }
    row.enabled = val
    ElMessage.success(t('agents.msg.toggled'))
  } catch (e) {
    ElMessage.error(extractMsg(e, t('agents.msg.fail')))
  }
}

const onDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      t('agents.confirmDelete', { name: row.name }),
      t('agents.delete'),
      { type: 'warning', confirmButtonText: t('agents.delete'), cancelButtonText: t('agents.cancel') }
    )
  } catch {
    return // 用户取消
  }
  try {
    const res = await deleteCustomAgent(row.id)
    if (res.data && res.data.error) {
      ElMessage.error(extractMsg(res.data, t('agents.msg.fail')))
      return
    }
    ElMessage.success(t('agents.msg.deleted'))
    load()
  } catch (e) {
    ElMessage.error(extractMsg(e, t('agents.msg.fail')))
  }
}

const onSubmit = async () => {
  if (!form.value.name.trim()) {
    ElMessage.warning(t('agents.msg.required'))
    return
  }
  const payload = {
    name: form.value.name.trim(),
    description: form.value.description.trim(),
    focusPoints: form.value.focusText
      .split('\n')
      .map((s) => s.trim())
      .filter(Boolean),
    severityBias: form.value.severityBias,
    enabled: form.value.enabled
  }
  try {
    let res
    if (editingId.value) {
      // 乐观锁：带上当前版本，冲突由引擎返回 409
      const cur = agents.value.find((a) => a.id === editingId.value)
      res = await updateCustomAgent(editingId.value, { ...payload, version: cur ? cur.version : 0 })
    } else {
      res = await createCustomAgent(payload)
    }
    const d = res && res.data
    if (d && d.error) {
      // 引擎注入预检 400 / 乐观锁 409：展示明确拒绝原因
      ElMessage.error(extractMsg(d, t('agents.msg.fail')))
      return
    }
    ElMessage.success(editingId.value ? t('agents.msg.updated') : t('agents.msg.added'))
    dialogVisible.value = false
    load()
  } catch (e) {
    ElMessage.error(extractMsg(e, t('agents.msg.fail')))
  }
}

const extractMsg = (e, fallback) => {
  const d = e && e.response ? e.response.data : e
  if (d && d.message) return d.message
  if (d && typeof d === 'string' && d.trim()) return d.trim()
  if (e && e.message && typeof e.message === 'string') return e.message
  return fallback
}
</script>
