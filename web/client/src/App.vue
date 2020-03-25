<template>
  <div id="app">
    <!-- <Upload
        multiple
        type="drag"
        action="//jsonplaceholder.typicode.com/posts/">
        <div style="padding: 20px 0">
            <Icon type="ios-cloud-upload" size="52" style="color: #3399ff"></Icon>
            <p>Click or drag files here to upload</p>
        </div>
    </Upload> -->
    <watermark-designer></watermark-designer>
  </div>
</template>

<script>

import WatermarkDesigner from './components/WatermarkDesigner'

export default {
  name: 'App',
  components: {
    WatermarkDesigner
  }
}
</script>

<style>
  #app{ padding: 10px; }
</style>
