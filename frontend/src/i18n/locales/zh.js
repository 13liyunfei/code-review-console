// 中文语言包
export default {
  app: {
    name: '代码审查控制台'
  },
  lang: {
    switchTo: 'English'
  },
  menu: {
    dashboard: '仪表盘',
    skills: 'Skills 市场',
    knowledge: '团队知识'
  },
  dash: {
    skillTotal: '技能总数',
    skillEnabled: '已启用',
    skillCustom: '自定义规则',
    knowledgeTotal: '知识总数',
    knowledgeIndexed: '已索引(RAG)',
    categoryDist: '技能维度分布',
    col: { category: '维度', total: '技能数', enabled: '已启用', custom: '自定义' }
  },
  skills: {
    count: '共 {n} 个技能（内置 + 团队自定义），启停即时生效并持久化',
    addCustom: '+ 新增自定义规则',
    col: { name: '名称', category: '维度', description: '说明', type: '类型', enabled: '启用', action: '操作' },
    custom: '自定义',
    builtin: '内置',
    delete: '删除',
    yamlHeader: 'YAML 低代码规则批量导入',
    yamlHint: '安全 / 合规团队无需编写 Java：维护一份 YAML 规则清单即可注入审查引擎（即时生效 + 持久化）',
    yamlPlaceholder: 'rules:\n  - name: 禁止提交 TODO\n    category: style\n    severity: MINOR\n    pattern: \'(?i)//.*\\b(todo|fixme)\\b\'\n    title: 遗留 TODO 标记\n    description: 提交中仍包含待办标记\n    suggestion: 登记到任务系统并移除',
    yamlImport: '导入 YAML 规则',
    dialogTitle: '新增团队自定义规则',
    form: { name: '规则名', category: '维度', severity: '严重级别', pattern: '匹配正则', title: '标题', description: '描述', suggestion: '修复建议' },
    cancel: '取消',
    submit: '提交',
    msg: {
      toggled: '已更新启停状态',
      fail: '操作失败',
      deleted: '已删除',
      required: '请填写规则名与匹配正则',
      added: '已新增自定义规则',
      importPartial: '导入 {ok} 条，失败 {err} 条：{errs}',
      importOk: '成功导入 {ok} 条 YAML 规则',
      importFail: 'YAML 导入失败'
    }
  },
  know: {
    uploadHeader: '上传团队知识（规范文档 / 操作手册 / 视频）',
    source: '来源名',
    sourcePlaceholder: '如 支付域规范',
    category: '关联维度',
    optional: '可选',
    type: '类型',
    typeDoc: '文档',
    typeManual: '操作手册',
    typeVideo: '视频',
    dragHint: '拖入文件，或点击上传（视频可另附文字稿）',
    textPlaceholder: '可选：粘贴文档正文 / 视频文字稿（将用于 RAG 检索；视频无文字稿时仅保存元数据）',
    upload: '上传并入库',
    listHeader: '知识列表',
    col: { source: '来源', type: '类型', indexed: '已索引', chunks: '段落数', size: '大小', createdAt: '创建时间', action: '操作' },
    yes: '是',
    no: '否',
    delete: '删除',
    msg: { required: '请选择文件或粘贴文字稿', added: '已入库', deleted: '已删除' }
  },
  cats: { security: '安全', logic: '逻辑', performance: '性能', style: '规范', architecture: '架构' }
}
