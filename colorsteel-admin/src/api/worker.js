import http from './http'

export function fetchWorkerPage(params) {
  return http.get('/workers', { params })
}

export function createWorker(data) {
  return http.post('/workers', data)
}
