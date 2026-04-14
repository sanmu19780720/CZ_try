import http from './http'

export const getHome = () => http.get('/portal/home')
export const getProductCategories = () => http.get('/portal/product-categories')
export const getProducts = (params) => http.get('/portal/products', { params })
export const getProductDetail = (id) => http.get(`/portal/products/${id}`)
export const getCases = (params) => http.get('/portal/cases', { params })
export const getCaseDetail = (id) => http.get(`/portal/cases/${id}`)
export const createInquiry = (data) => http.post('/portal/inquiries', data)
