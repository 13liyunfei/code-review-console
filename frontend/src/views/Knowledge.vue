<template>
  <div>
    <el-card :header="t('know.uploadHeader')" style="margin-bottom: 16px">
      <el-form :inline="true" label-width="80px">
        <el-form-item :label="t('know.source')">
          <el-input v-model="source" :placeholder="t('know.sourcePlaceholder')" />
        </el-form-item>
        <el-form-item :label="t('know.category')">
          <el-select v-model="category" clearable :placeholder="t('know.optional')" style="width: 140px">
            <el-option v-for="c in cats" :key="c.value" :label="c.label" :value="c.value" />
          </el-select>
        </el-form-item>
        <el-form-item :label="t('know.type')">
          <el-select v-model="type" style="width: 140px">
            <el-option :label="t('know.typeDoc')" value="document" />
            <el-option :label="t('know.typeManual')" value="manual" />
            <el-option :label="t('know.typeVideo')" value="video" />
          </el-select>
        </el-form-item>
      </el-form>

      <el-upload
        class="upload"
        drag
        :auto-upload="false"
        :limit="1"
        :on-change="onFileChange"
        :file-list="fileList"
      >
        <el-icon style="font-size: 48px; color: #c0c4cc"><UploadFilled /></el-icon>
        <div>{{ t('know.dragHint') }}</div>
      </el-upload>

      <el-input
        v-model="text"
        type="textarea"
        :rows="3"
        style="margin: 10px 0"
        :placeholder="t('know.textPlaceholder')"
      />
      <el-button type="primary" @click="onUpload">{{ t('know.upload') }}</el-button>
    </el-card>

    <el-card :header="t('know.listHeader')">
      <el-table :data="list" border>
        <el-table-column prop="source" :label="t('know.col.source')" width="180" />
        <el-table-column :label="t('know.col.type')" width="100">
          <template #default="{ row }"><el-tag>{{ typeLabel(row.type) }}</el-tag></template>
        </el-table-column>
        <el-table-column :label="t('know.col.indexed')" width="90">
          <template #default="{ row }">
            <el-tag :type="row.indexed ? 'success' : 'info'">{{ row.indexed ? t('know.yes') : t('know.no') }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="chunkCount" :label="t('know.col.chunks')" width="80" />
        <el-table-column :label="t('know.col.size')" width="100">
          <template #default="{ row }">{{ fmtSize(row.sizeBytes) }}</template>
        </el-table-column>
        <el-table-column prop="createdAt" :label="t('know.col.createdAt')" width="210" />
        <el-table-column :label="t('know.col.action')" width="90">
          <template #default="{ row }">
            <el-button type="danger" size="small" @click="onDelete(row)">{{ t('know.delete') }}</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useI18n } from 'vue-i18n'
import { ElMessage } from 'element-plus'
import { UploadFilled } from '@element-plus/icons-vue'
import { getKnowledge, uploadKnowledge, deleteKnowledge } from '@/api'

const { t } = useI18n()
const list = ref([])
const source = ref('')
const category = ref('')
const type = ref('document')
const text = ref('')
const fileList = ref([])
let fileObj = null

const cats = ['security', 'logic', 'performance', 'style', 'architecture'].map((v) => ({
  value: v,
  label: t(`cats.${v}`)
}))
const typeLabel = (x) => ({ document: t('know.typeDoc'), manual: t('know.typeManual'), video: t('know.typeVideo') }[x] || x)
const fmtSize = (b) =>
  b == null ? '-' : b < 1024 ? b + 'B' : (b / 1024).toFixed(1) + 'KB'

const load = async () => { list.value = await getKnowledge() }
onMounted(load)

const onFileChange = (file, fl) => {
  fileObj = file.raw
  fileList.value = fl.slice(-1)
}
const onUpload = async () => {
  if (!fileObj && !text.value) {
    ElMessage.warning(t('know.msg.required'))
    return
  }
  const fd = new FormData()
  if (fileObj) fd.append('file', fileObj)
  if (source.value) fd.append('source', source.value)
  if (category.value) fd.append('category', category.value)
  fd.append('type', type.value)
  if (text.value) fd.append('text', text.value)
  await uploadKnowledge(fd)
  ElMessage.success(t('know.msg.added'))
  fileList.value = []
  fileObj = null
  text.value = ''
  load()
}
const onDelete = async (row) => {
  await deleteKnowledge(row.id)
  ElMessage.success(t('know.msg.deleted'))
  load()
}
</script>
