import axios from 'axios'

const service = axios.create({
  baseURL: process.env.NODE_ENV == 'development' ? 'http://localhost:8080/api' : '/api',
  timeout: 6000000
})

// 处理结果
service.interceptors.response.use((resp) => {
  return resp.data
}, (err) => {
  return Promise.reject(err)
})

export default service
