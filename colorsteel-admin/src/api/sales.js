import http from './http'

export function fetchSalesOrderPage(params) {
  return http.get('/sales-orders', { params })
}

export function getSalesOrderDetail(id) {
  return http.get(`/sales-orders/${id}`)
}

export function createSalesOrder(data) {
  return http.post('/sales-orders', data)
}

export function approveSalesOrder(id) {
  return http.post(`/sales-orders/${id}/approve`)
}

export function cancelSalesOrder(id) {
  return http.post(`/sales-orders/${id}/cancel`)
}
