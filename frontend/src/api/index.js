import axios from 'axios'

export const api = axios.create({ baseURL: '' })

export const getSkills = () => api.get('/api/skills').then((r) => r.data)
export const toggleSkill = (name, enabled) =>
  api.post(`/api/skills/${name}/toggle`, { enabled })
export const addCustom = (payload) => api.post('/api/skills/custom', payload)
export const removeCustom = (id) => api.delete(`/api/skills/custom/${id}`)
export const importYaml = (yaml) =>
  api.post('/api/skills/yaml', yaml, { headers: { 'Content-Type': 'text/plain' } })

export const getKnowledge = () => api.get('/api/knowledge').then((r) => r.data)
export const uploadKnowledge = (formData) =>
  api.post('/api/knowledge', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
export const deleteKnowledge = (id) => api.delete(`/api/knowledge/${id}`)

export const getStats = () => api.get('/api/stats').then((r) => r.data)

export const getCustomAgents = () => api.get('/api/agents').then((r) => r.data)
export const createCustomAgent = (payload) => api.post('/api/agents', payload)
export const updateCustomAgent = (id, payload) => api.put(`/api/agents/${id}`, payload)
export const deleteCustomAgent = (id) => api.delete(`/api/agents/${id}`)
export const toggleCustomAgent = (id, enabled) =>
  api.post(`/api/agents/${id}/toggle`, { enabled })
