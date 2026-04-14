import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/store/user'

const http = axios.create({
  baseURL: '/api/v1',
  timeout: 60000,
})

http.interceptors.request.use((config) => {
  const store = useUserStore()
  if (store.token) {
    config.headers.Authorization = `Bearer ${store.token}`
  }
  return config
})

http.interceptors.response.use(
  (res) => {
    const body = res.data
    if (body.code !== 0) {
      ElMessage.error(body.message || '请求失败')
      return Promise.reject(new Error(body.message || '请求失败'))
    }
    return body.data
  },
  (err) => {
    const status = err.response?.status
    if (status === 401) {
      useUserStore().logout()
      import('@/router').then((m) => m.default.push({ name: 'Login' }))
    }
    const msg = err.response?.data?.message || err.message || '网络错误'
    ElMessage.error(msg)
    return Promise.reject(err)
  }
)

export default http
