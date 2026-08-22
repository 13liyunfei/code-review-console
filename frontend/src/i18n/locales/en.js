// English language pack
export default {
  app: {
    name: 'Code Review Console'
  },
  lang: {
    switchTo: '中文'
  },
  menu: {
    dashboard: 'Dashboard',
    skills: 'Skills Market',
    knowledge: 'Team Knowledge'
  },
  dash: {
    skillTotal: 'Skills Total',
    skillEnabled: 'Enabled',
    skillCustom: 'Custom Rules',
    knowledgeTotal: 'Knowledge Items',
    knowledgeIndexed: 'Indexed (RAG)',
    categoryDist: 'Skills by Category',
    col: { category: 'Category', total: 'Skills', enabled: 'Enabled', custom: 'Custom' }
  },
  skills: {
    count: '{n} skills (built-in + team custom); toggles take effect immediately and persist',
    addCustom: '+ Add Custom Rule',
    col: { name: 'Name', category: 'Category', description: 'Description', type: 'Type', enabled: 'Enabled', action: 'Action' },
    custom: 'Custom',
    builtin: 'Built-in',
    delete: 'Delete',
    yamlHeader: 'Import YAML Rules (Low-code)',
    yamlHint: 'Security / compliance teams don\u0027t need to write Java: maintain a YAML rule list to inject into the review engine (applies immediately and persists)',
    yamlPlaceholder: 'rules:\n  - name: Forbid committing TODO\n    category: style\n    severity: MINOR\n    pattern: \'(?i)//.*\\b(todo|fixme)\\b\'\n    title: Leftover TODO marker\n    description: The commit still contains a TODO marker\n    suggestion: Register it in the issue tracker and remove',
    yamlImport: 'Import YAML Rules',
    dialogTitle: 'Add Team Custom Rule',
    form: { name: 'Rule name', category: 'Category', severity: 'Severity', pattern: 'Regex pattern', title: 'Title', description: 'Description', suggestion: 'Fix suggestion' },
    cancel: 'Cancel',
    submit: 'Submit',
    msg: {
      toggled: 'Toggle updated',
      fail: 'Operation failed',
      deleted: 'Deleted',
      required: 'Please fill in rule name and regex pattern',
      added: 'Custom rule added',
      importPartial: 'Imported {ok}, {err} failed: {errs}',
      importOk: 'Successfully imported {ok} YAML rules',
      importFail: 'YAML import failed'
    }
  },
  know: {
    uploadHeader: 'Upload Team Knowledge (specs / runbooks / videos)',
    source: 'Source name',
    sourcePlaceholder: 'e.g. Payment-domain spec',
    category: 'Category',
    optional: 'Optional',
    type: 'Type',
    typeDoc: 'Document',
    typeManual: 'Runbook',
    typeVideo: 'Video',
    dragHint: 'Drop a file here, or click to upload (videos may include a transcript)',
    textPlaceholder: 'Optional: paste the document body / video transcript (used for RAG retrieval; video without transcript stores metadata only)',
    upload: 'Upload & Store',
    listHeader: 'Knowledge List',
    col: { source: 'Source', type: 'Type', indexed: 'Indexed', chunks: 'Chunks', size: 'Size', createdAt: 'Created At', action: 'Action' },
    yes: 'Yes',
    no: 'No',
    delete: 'Delete',
    msg: { required: 'Please select a file or paste a transcript', added: 'Stored', deleted: 'Deleted' }
  },
  cats: { security: 'Security', logic: 'Logic', performance: 'Performance', style: 'Style', architecture: 'Architecture' }
}
