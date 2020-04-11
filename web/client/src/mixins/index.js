let path = {
  data () {
    return {
      apiPath: process.env.NODE_ENV == 'development' ? 'http://localhost:8080/api' : '/api'
    }
  }
}

export default [path]

