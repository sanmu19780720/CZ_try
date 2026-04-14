import http from './http'

export function fetchPurchaseOrderPage(params) {
  return http.get('/purchase-orders', { params })
}

export function getPurchaseOrderDetail(id) {
  return http.get(`/purchase-orders/${id}`)
}

export function createPurchaseOrder(data) {
  return http.post('/purchase-orders', data)
}

export function approvePurchaseOrder(id) {
  return http.post(`/purchase-orders/${id}/approve`)
}

export function cancelPurchaseOrder(id) {
  return http.post(`/purchase-orders/${id}/cancel`)
}
