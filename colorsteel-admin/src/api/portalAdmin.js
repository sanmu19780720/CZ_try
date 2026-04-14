import http from './http'

export const uploadPortalImage = async (file) => {
  const formData = new FormData()
  formData.append('file', file)
  return http.post('/admin/portal/upload-image', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  })
}

export const getPortalSiteConfig = () => http.get('/admin/portal/site-config')
export const updatePortalSiteConfig = (data) => http.put('/admin/portal/site-config', data)

export const fetchPortalBannerPage = (params) => http.get('/admin/portal/banners', { params })
export const createPortalBanner = (data) => http.post('/admin/portal/banners', data)
export const updatePortalBanner = (id, data) => http.put(`/admin/portal/banners/${id}`, data)
export const deletePortalBanner = (id) => http.delete(`/admin/portal/banners/${id}`)
export const sortPortalBanners = (items) => http.put('/admin/portal/banners/sort', { items })

export const fetchPortalProductPage = (params) => http.get('/admin/portal/products', { params })
export const createPortalProduct = (data) => http.post('/admin/portal/products', data)
export const updatePortalProduct = (id, data) => http.put(`/admin/portal/products/${id}`, data)
export const deletePortalProduct = (id) => http.delete(`/admin/portal/products/${id}`)
export const sortPortalProducts = (items) => http.put('/admin/portal/products/sort', { items })
export const updatePortalProductHome = (id, showOnHome) =>
  http.put(`/admin/portal/products/${id}/home`, null, { params: { showOnHome } })

export const fetchPortalCasePage = (params) => http.get('/admin/portal/cases', { params })
export const createPortalCase = (data) => http.post('/admin/portal/cases', data)
export const updatePortalCase = (id, data) => http.put(`/admin/portal/cases/${id}`, data)
export const deletePortalCase = (id) => http.delete(`/admin/portal/cases/${id}`)
export const addPortalCaseImage = (id, data) => http.post(`/admin/portal/cases/${id}/images`, data)
export const deletePortalCaseImage = (id, imageId) => http.delete(`/admin/portal/cases/${id}/images/${imageId}`)
export const updatePortalCaseRecommended = (id, isRecommended) =>
  http.put(`/admin/portal/cases/${id}/recommended`, null, { params: { isRecommended } })
export const sortPortalCases = (items) => http.put('/admin/portal/cases/sort', { items })

export const fetchPortalPriceCardPage = (params) => http.get('/admin/portal/price-cards', { params })
export const createPortalPriceCard = (data) => http.post('/admin/portal/price-cards', data)
export const updatePortalPriceCard = (id, data) => http.put(`/admin/portal/price-cards/${id}`, data)
export const deletePortalPriceCard = (id) => http.delete(`/admin/portal/price-cards/${id}`)
export const sortPortalPriceCards = (items) => http.put('/admin/portal/price-cards/sort', { items })

export const fetchPortalInquiryPage = (params) => http.get('/admin/portal/inquiries', { params })
export const followPortalInquiry = (id, data) => http.put(`/admin/portal/inquiries/${id}/follow`, data)
