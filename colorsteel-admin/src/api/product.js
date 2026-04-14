import http from './http'

export function fetchProductPage(params) {
  return http.get('/products', { params })
}

export function getProduct(id) {
  return http.get(`/products/${id}`)
}

export function createProduct(data) {
  return http.post('/products', data)
}

export function updateProduct(id, data) {
  return http.put(`/products/${id}`, data)
}

export function deleteProduct(id) {
  return http.delete(`/products/${id}`)
}
