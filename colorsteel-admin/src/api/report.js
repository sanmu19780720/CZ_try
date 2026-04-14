import http from './http'

export function getDailyReport(date) {
  return http.get('/reports/daily', { params: { date } })
}

export function getMonthlyReport(year, month) {
  return http.get('/reports/monthly', { params: { year, month } })
}

export function getProjectProfitReport(from, to) {
  return http.get('/reports/project-profit', { params: { from, to } })
}
