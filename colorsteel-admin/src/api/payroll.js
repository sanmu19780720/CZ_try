import http from './http'

export function fetchPayrollPage(params) {
  return http.get('/payrolls', { params })
}

export function createPayroll(data) {
  return http.post('/payrolls', data)
}
