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
    agents: '自定义 Agent',
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
  cats: { security: '安全', logic: '逻辑', performance: '性能', style: '规范', architecture: '架构' },
  agents: {
    count: '共 {n} 个业务方自定义审查 Agent（团队级，随 PR 并行审查）',
    teamHint: '当前团队: default',
    add: '+ 新增自定义 Agent',
    edit: '编辑',
    delete: '删除',
    editTitle: '编辑自定义 Agent',
    addTitle: '新增自定义 Agent',
    cancel: '取消',
    submit: '提交',
    col: {
      name: '名称',
      focus: '审查要点',
      severity: '严重级别',
      enabled: '启用',
      version: '版本',
      updated: '更新时间',
      action: '操作'
    },
    form: {
      name: 'Agent 名称',
      description: '说明',
      focus: '审查要点（每行一条）',
      focusPlaceholder: '每行一个审查要点，例如：\n检查是否存在硬编码密钥/密码\n检查是否使用 SELECT *\n检查资金类操作是否有权限校验',
      severity: '严重级别偏置',
      enabled: '启用'
    },
    securityHeader: '安全设计（Prompt 注入防护）',
    securityHint: '业务方自定义 Agent 采用「声明式骨架 + 内容槽」设计：系统指令骨架由引擎硬编码且不可覆盖，业务方只能填写审查要点等内容槽。',
    securityList: [
      '写库前注入预检：名称 / 说明 / 要点命中越权句式（如「忽略以上指令」）直接拒绝保存（400）',
      '审查时 diff 内容中的可疑指令会被标注为 [INJECTION-RISK]，不会切换角色执行',
      '可降级：自定义 Agent 异常 / 超时 / 无模型时返回空结果，不影响内置 5 个 Agent',
      '可追踪可回放：每次展开 / 结果都写入轨迹 JSONL（agent.custom.expanded 事件），支持断点续跑'
    ],
    confirmDelete: '确定删除自定义 Agent「{name}」？',
    msg: {
      toggled: '已更新启停状态',
      deleted: '已删除',
      updated: '已保存修改',
      added: '已新增自定义 Agent',
      required: '请填写 Agent 名称',
      fail: '操作失败'
    }
  }
}
