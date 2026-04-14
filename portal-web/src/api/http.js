import axios from 'axios'

const http = axios.create({
  baseURL: '/api',
  timeout: 20000,
})

http.interceptors.response.use((res) => {
  const body = res.data
  if (body?.code !== 0) {
    throw new Error(body?.message || '请求失败')
  }
  return body.data
})

export default http
