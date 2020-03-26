import axios from 'axios'

const service = axios.create({
  baseURL: 'http://localhost',
  timeout: 6000000
})

// 处理结果
service.interceptors.response.use((resp) => {
  return resp.data
}, (err) => {
  return Promise.reject(err)
})

export default service
