import http from './http'

export function fetchProjectPage(params) {
  return http.get('/projects', { params })
}

export function getProject(id) {
  return http.get(`/projects/${id}`)
}

export function createProject(data) {
  return http.post('/projects', data)
}

export function getProjectProfit(id) {
  return http.get(`/projects/${id}/profit`)
}

export function getProjectLaborCost(id) {
  return http.get(`/projects/${id}/labor-cost`)
}
