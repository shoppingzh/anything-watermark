<template>
  <div id="app">
    <Steps :current="step">
      <Step title="选择文件" content="支持pdf、视频、图片">
      </Step>
      <Step title="设计水印" content="设计你想要的水印效果"></Step>
      <Step title="生成" content="生成水印并下载"></Step>
    </Steps>
    <div class="steps">
      <div v-show="step === 0" class="steps__item">
        <Upload
          v-if="step === 0"
          type="drag"
          action="http://localhost/file/upload"
          :on-success="handleUploaded">
            <div style="padding: 60px 0">
              <Icon type="ios-cloud-upload" size="52" style="color: #3399ff"></Icon>
              <p>选择一个文件或将文件拖动至此</p>
              <p>
                <Tag type="border" color="primary">PDF</Tag> <Tag type="border" color="warning">视频</Tag> <Tag type="border" color="success">图片</Tag>
              </p>
            </div>
        </Upload>
      </div>
      <div v-show="step === 1" class="steps__item">
        <watermark-designer
          ref="designer"
          @designEnd="handleDesignEnd"></watermark-designer>
      </div>
      <div v-show="step === 2" class="steps__item">

      </div>
    </div>
    
  </div>
</template>

<script>

import WatermarkDesigner from './components/WatermarkDesigner'
import http from '@/utils/http'

export default {
  name: 'App',
  components: {
    WatermarkDesigner
  },
  data() {
    return {
      step: 0,
      src: null
    }
  },
  methods: {
    handleUploaded(resp) {
      this.step = 1;
      if (resp.success) {
        this.src = resp.data
      }
    },
    handleDesignEnd() {
      http({
        url: '/watermark/generate',
        method: 'post',
        params: Object.assign({
          src: this.src
        }, this.$refs.designer.config())
      }).then((data) => {
        console.log(data)
      })
    }
  }
}
</script>

<style>
  #app{ margin: 20px 150px; }

  .steps{ margin-top: 45px; }
</style>
