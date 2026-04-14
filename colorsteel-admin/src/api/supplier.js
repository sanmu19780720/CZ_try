import http from './http'

export function fetchSupplierPage(params) {
  return http.get('/suppliers', { params })
}
