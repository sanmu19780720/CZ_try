import http from './http'

export function fetchWarehousePage(params) {
  return http.get('/warehouses', { params })
}
