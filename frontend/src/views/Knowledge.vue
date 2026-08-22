<template>
  <div>
    <el-card header="上传团队知识（规范文档 / 操作手册 / 视频）" style="margin-bottom: 16px">
      <el-form :inline="true" label-width="80px">
        <el-form-item label="来源名">
          <el-input v-model="source" placeholder="如 支付域规范" />
        </el-form-item>
        <el-form-item label="关联维度">
          <el-select v-model="category" clearable placeholder="可选" style="width: 140px">
            <el-option v-for="c in cats" :key="c.value" :label="c.label" :value="c.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="type" style="width: 140px">
            <el-option label="文档" value="document" />
            <el-option label="操作手册" value="manual" />
            <el-option label="视频" value="video" />
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
        <div>拖入文件，或点击上传（视频可另附文字稿）</div>
      </el-upload>

      <el-input
        v-model="text"
        type="textarea"
        :rows="3"
        style="margin: 10px 0"
        placeholder="可选：粘贴文档正文 / 视频文字稿（将用于 RAG 检索；视频无文字稿时仅保存元数据）"
      />
      <el-button type="primary" @click="onUpload">上传并入库</el-button>
    </el-card>

    <el-card header="知识列表">
      <el-table :data="list" border>
        <el-table-column prop="source" label="来源" width="180" />
        <el-table-column prop="type" label="类型" width="100">
          <template #default="{ row }"><el-tag>{{ typeLabel(row.type) }}</el-tag></template>
        </el-table-column>
        <el-table-column label="已索引" width="90">
          <template #default="{ row }">
            <el-tag :type="row.indexed ? 'success' : 'info'">{{ row.indexed ? '是' : '否' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="chunkCount" label="段落数" width="80" />
        <el-table-column label="大小" width="100">
          <template #default="{ row }">{{ fmtSize(row.sizeBytes) }}</template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="210" />
        <el-table-column label="操作" width="90">
          <template #default="{ row }">
            <el-button type="danger" size="small" @click="onDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { UploadFilled } from '@element-plus/icons-vue'
import { getKnowledge, uploadKnowledge, deleteKnowledge } from '@/api'

const list = ref([])
const source = ref('')
const category = ref('')
const type = ref('document')
const text = ref('')
const fileList = ref([])
let fileObj = null

const cats = [
  { label: '安全', value: 'security' },
  { label: '逻辑', value: 'logic' },
  { label: '性能', value: 'performance' },
  { label: '规范', value: 'style' },
  { label: '架构', value: 'architecture' }
]
const typeLabel = (t) => ({ document: '文档', manual: '手册', video: '视频' }[t] || t)
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
    ElMessage.warning('请选择文件或粘贴文字稿')
    return
  }
  const fd = new FormData()
  if (fileObj) fd.append('file', fileObj)
  if (source.value) fd.append('source', source.value)
  if (category.value) fd.append('category', category.value)
  fd.append('type', type.value)
  if (text.value) fd.append('text', text.value)
  await uploadKnowledge(fd)
  ElMessage.success('已入库')
  fileList.value = []
  fileObj = null
  text.value = ''
  load()
}
const onDelete = async (row) => {
  await deleteKnowledge(row.id)
  ElMessage.success('已删除')
  load()
}
</script>
