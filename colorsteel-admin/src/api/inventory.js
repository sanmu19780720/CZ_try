import http from './http'

export function fetchInventoryPage(params) {
  return http.get('/inventories', { params })
}

export function getInventory(id) {
  return http.get(`/inventories/${id}`)
}

export function createInventory(data) {
  return http.post('/inventories', data)
}

export function updateInventory(id, data) {
  return http.put(`/inventories/${id}`, data)
}

export function deleteInventory(id) {
  return http.delete(`/inventories/${id}`)
}
