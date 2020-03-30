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
          action="/file/upload"
          :on-success="handleUploaded"
          :on-error="handleUploadError">
            <div style="padding: 60px 0">
              <Icon type="ios-cloud-upload" size="52" style="color: #3399ff"></Icon>
              <p>选择一个文件或将文件拖动至此</p>
              <p>
                <Tag type="border" color="primary">PDF</Tag> <Tag type="border" color="warning">视频</Tag> <Tag type="border" color="success">图片</Tag> <Tag type="border" color="purple">Word</Tag>
              </p>
            </div>
        </Upload>
      </div>
      <div v-show="step === 1" class="steps__item" style="position: relative;">
        <watermark-designer
          ref="designer"
          @designEnd="handleDesignEnd">
        </watermark-designer>
        <Spin size="large" fix v-if="designing"></Spin>
      </div>
      <div v-show="step === 2" class="steps__item">
        <div v-if="result" class="result-box" @mouseenter="moreAction = true" @mouseleave="moreAction = false">
          <img v-if="resultType == 'image'" :src="resultSrc">
          <video v-else-if="resultType == 'video'" :src="resultSrc" controls></video>
          <div v-else-if="resultType == 'pdf'" style="height: 100%; overflow: auto;">
            <pdf :src="resultSrc"></pdf>
          </div>
          <a v-else :href="'/watermark/' + result" target="_blank">
            {{result}}
          </a>
          <Row v-show="moreAction" type="flex" class="result-box__overlay">
            <i-col :span="12" class="result-box__overlay__item" style="background-color: rgba(255, 153, 0, .75)" @click.native="handleDownload">
              <Row type="flex" class="result-box__action" align="middle" justify="center">
                <div>
                  <Icon type="md-download" :size="40" /> 下载
                </div>
              </Row>
            </i-col>
            <i-col :span="12" class="result-box__overlay__item" style="background-color: rgba(25, 190, 107, .75)" @click.native="step = 1">
              <Row type="flex" class="result-box__action" align="middle" justify="center">
                <div>
                  <Icon type="md-construct" :size="40" /> 重新设计
                </div>
              </Row>
            </i-col>
          </Row>
        </div>
      </div>
    </div>
    
  </div>
</template>

<script>

import WatermarkDesigner from './components/WatermarkDesigner'
import http from '@/utils/http'
import pdf from 'vue-pdf'

export default {
  name: 'App',
  components: {
    WatermarkDesigner,
    pdf
  },
  data() {
    return {
      step: 0,
      designing: false,
      src: null,
      result: null,
      moreAction: false
    }
  },
  computed: {
    resultType() {
      const result = this.result
      if (!result) {
        return null
      }
      const ext = result.substring(result.lastIndexOf('.') + 1)
      if (ext == 'pdf') {
        return 'pdf'
      }
      if (ext == 'docx') {
        return 'word'
      }
      if (ext == 'mp4') {
        return 'video'
      }
      return 'image'
    },
    resultSrc() {
      return `/watermark/${this.result}`
    }
  },
  methods: {
    handleUploaded(resp) {
      this.step = 1;
      if (resp.success) {
        this.src = resp.data
      }
    },
    handleUploadError() {
      this.$Message.error('上传失败，请检查网络设置')
    },
    handleDesignEnd() {
      this.designing = true
      http({
        url: '/watermark/generate',
        method: 'post',
        params: Object.assign({
          src: this.src
        }, this.$refs.designer.config())
      }).then((resp) => {
        this.designing = false
        if (resp.success) {
          this.step = 2
          this.result = resp.data
        }
      }).catch(() => {
        this.designing = false
      })
    },
    handleDownload() {
      window.open(this.resultSrc)
    }
  }
}
</script>

<style>
  #app{ margin: 20px 150px; }

  .steps{ margin-top: 45px; }
  .result-box{ height: 650px; position: relative; overflow: auto; }
  .result-box img, .result-box video{ object-fit: contain; width: 100%; height: 100%; object-position: center; }
  .result-box__overlay{ position: absolute; left: 0; right: 0; top: 0; bottom: 0; }
  .result-box__overlay__item{ height: 100%; cursor: pointer; }
  .result-box__action{ height: 100%; color: #fff; }
</style>
