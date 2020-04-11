<template>
  <div>
    <Row>
      <i-col span="16">
        <div ref="box" class="preview-box">
          <slot name="container"></slot>
          <div class="watermark" :style="styles">
            <span ref="watermark" v-if="conf.type == 1" style="white-space: nowrap;">{{ conf.text }}</span>
            <div v-else-if="conf.type == 2">
              <img ref="watermark" v-if="conf.image" :src="imageUrl">
            </div>
          </div>
        </div>
      </i-col>
      <i-col span="8">
        <Form :label-width="120">
          <FormItem label="水印类型">
            <RadioGroup v-model="conf.type">
              <Radio :label="1">文字水印</Radio>
              <Radio :label="2">图片水印</Radio>
            </RadioGroup>
          </FormItem>
          <FormItem v-show="conf.type ===  1">
            <template #label><Icon type="ios-document-outline" /> 文字内容</template>
            <i-input v-model="conf.text"></i-input>
          </FormItem>
          <FormItem v-show="conf.type == 1">
            <template #label><Icon type="ios-document-outline" /> 文字颜色</template>
            <ColorPicker v-model="conf.textColor" format="hex" :colors="['#333', '#666', '#999', '#aaa', '#bbb', '#ccc', '#ddd', '#eee', '#fff']"/>
          </FormItem>
          <FormItem v-show="conf.type == 2">
            <template #label><Icon type="md-image" /> 水印图片</template>
            <Upload :action="apiPath + '/file/upload'" :on-success="handleImageUploaded">
                <Button icon="ios-cloud-upload-outline">选择文件</Button>
            </Upload>
          </FormItem>
          <!-- 通用设置 -->
          <FormItem label="快捷设置">
            <Button type="success" ghost @click="handleFastDesign"><Icon type="ios-hammer-outline" /> 打开快捷设置面板</Button>
          </FormItem>
          <FormItem label="水印大小">
            <Slider v-model="scaledSize" :min="1" :max="100"></Slider>
          </FormItem>
          <FormItem label="水印位置">
            <Row type="flex">
              <i-col :span="8">
                <Button @click="handlePosLeftTop">左上</Button>
              </i-col>
              <i-col :span="8">
              </i-col>
              <i-col :span="8">
                <Button @click="handlePosRightTop">右上</Button>
              </i-col>
            </Row>
            <Row type="flex">
              <i-col :span="8">
              </i-col>
              <i-col :span="8">
                <Button @click="handlePosCenter">中间</Button>
              </i-col>
              <i-col :span="8">
              </i-col>
            </Row>
            <Row type="flex">
              <i-col :span="8">
                <Button @click="handlePosLeftBottom">左下</Button>
              </i-col>
              <i-col :span="8">
              </i-col>
              <i-col :span="8">
                <Button @click="handlePosRightBottom">右下</Button>
              </i-col>
            </Row>
            <p style="margin-top: 5px;">
              <Button type="warning" size="small" ghost style="border: none;" @click="positionSetting = !positionSetting">
                <Icon type="md-cog" /> {{positionSetting ? '收起' : ''}}高级设置
              </Button> 
            </p>
          </FormItem>
          <FormItem v-show="positionSetting" label="横轴位置">
            <Slider v-model="xOffset" :min="0" :max="100" :step="1" show-input></Slider>
          </FormItem>
          <FormItem v-show="positionSetting" label="横轴对齐方式">
            <RadioGroup v-model="conf.xAlign">
              <Radio :label="1">左</Radio>
              <Radio :label="0.5">中</Radio>
              <Radio :label="0">右</Radio>
            </RadioGroup>
          </FormItem>
          <FormItem v-show="positionSetting" label="纵轴位置">
            <Slider v-model="yOffset" :min="0" :max="100" :step="1" show-input></Slider>
          </FormItem>
          <FormItem v-show="positionSetting" label="纵轴对齐方式">
            <RadioGroup v-model="conf.yAlign">
              <Radio :label="1">上</Radio>
              <Radio :label="0.5">中</Radio>
              <Radio :label="0">下</Radio>
            </RadioGroup>
          </FormItem>
          <FormItem label="旋转角度">
            <Slider v-model="conf.rotation" :min="0" :max="360" :step="1" show-input></Slider>
          </FormItem>
          <FormItem label="不透明度">
            <Slider v-model="conf.opacity" :min="0" :max="100" :step="1" show-input></Slider>
          </FormItem>
          <FormItem>
            <Button type="primary" long  @click="$emit('designEnd')">好了 <Icon type="md-arrow-round-forward" /></Button>
          </FormItem>
        </Form>
        <!-- 快捷设置对话框 -->
        <Modal v-model="fast" footer-hide	:transfer="false">
          <div>
            <div class="fast-setting__item">
              <p class="fast-setting__label">PDF: </p>
              <Button type="primary" ghost @click="handleFastPdf1">PDF常用水印1</Button> &nbsp;
              <Button type="primary" ghost @click="handleFastPdf2">PDF常用水印2</Button>
            </div>
            <div class="fast-setting__item">
              <p class="fast-setting__label">视频：</p>
              <Button type="warning" ghost @click="handleFastVideo1">视频常用水印1</Button>
            </div>
            <div class="fast-setting__item">
              <p class="fast-setting__label">图片：</p>
              <Button type="success" ghost @click="handleFastImage1">图片常用水印1</Button> &nbsp;
              <Button type="success" ghost @click="handleFastImage2">图片常用水印2</Button>
            </div>
          </div>
        </Modal>
      </i-col>
    </Row>
    <!-- 专门用来计算文字高度的 -->
    <div style="position: fixed; top: 0; left: 0; margin-left: -100%; margin-top: -100%;">
      <span ref="textTemplate" style="font-size: 12px;">{{ conf.text }}</span>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    
  },
  data() {
    return {
      conf: {
        type: 1,
        x: 0,
        y: 0,
        xAlign: 0,
        yAlign: 0,
        size: 0.01,
        rotation: 0,
        opacity: 100,
        text: '内部使用，请勿盗版！',
        textColor: '#000',
        image: null
      },
      fast: false,
      positionSetting: false,
      apiPath: process.env.NODE_ENV == 'development' ? 'http://localhost:8080/api' : ''
    }
  },
  computed: {
    xOffset: {
      get() {
        return this.conf.x * 100
      },
      set(val) {
        this.conf.x = val / 100
      } 
    },
    yOffset: {
      get() {
        return this.conf.y * 100
      },
      set(val) {
        this.conf.y = val / 100
      }
    },
    scaledSize: {
      get() {
        return this.conf.size * 500
      },
      set(newVal) {
        this.conf.size = newVal / 500
      }
    },
    imageUrl() {
      const image = this.conf.image
      return image ? this.apiPath + `/upload/${image}` : null
    },
    styles: function() {
      
      const conf = this.conf
      // 文字影响宽度，在这里给个引用，保证能更新位置
      this.conf.text
      this.conf.image
      const type = conf.type
      const box = this.$refs.box
      const watermark = this.$refs.watermark
      const size = conf.size
      const x = conf.x, y = conf.y
      const xa = conf.xAlign, ya = conf.yAlign
      
      let left = 0, top = 0
      if (box && watermark) { // 防止DOM没有准备好
        const mw = box.offsetWidth, mh = box.offsetHeight
        const ow = watermark.offsetWidth, oh = watermark.offsetHeight
        let w = ow, h = oh
        if (type == 1) {
          const template = this.$refs.textTemplate
          if (template) {
            const th = template.offsetHeight
            watermark.style.fontSize = Math.min(mh, mw) * size * 12 / th + 'px'
          }
          
        } else if (type == 2) {
          const ratio = ow / oh
          
          h = Math.sqrt(mw * mh * size / ratio)
          w = h * ratio
          watermark.style.width = w + 'px'
          watermark.style.height = h + 'px'
        }

        // 位置
        left = mw * x
        top = mh * y
        // 计算偏移量
        left -= w * xa
        top -= h * ya
      }
      return {
        transform: `rotate(${conf.rotation}deg)`,
        opacity: conf.opacity / 100,
        color: conf.textColor,
        left: left + 'px',
        top: top + 'px',
      };
    }
  },
  watch: {
  },
  mounted() {
  },
  methods: {
    config() {
      return Object.assign({}, this.conf);
    },
    // 水印图片上传完成
    handleImageUploaded(resp) {
      if (resp.success) {
        this.conf.image = resp.data
        this.conf.size = 8
      }
    },
    handleFastDesign() {
      this.fast = true
    },
    handlePosLeftTop() {
      const conf = this.conf
      conf.x = 0
      conf.y = 0
      conf.xAlign = 0
      conf.yAlign = 0
    },
    handlePosRightTop() {
      const conf = this.conf
      conf.x = 1
      conf.y = 0
      conf.xAlign = 1
      conf.yAlign = 0
    },
    handlePosCenter() {
      const conf = this.conf
      conf.x = 0.5
      conf.y = 0.5
      conf.xAlign = 0.5
      conf.yAlign = 0.5
    },
    handlePosLeftBottom() {
      const conf = this.conf
      conf.x = 0
      conf.y = 1
      conf.xAlign = 0
      conf.yAlign = 1
    },
    handlePosRightBottom() {
      const conf = this.conf
      conf.x = 1
      conf.y = 1
      conf.xAlign = 1
      conf.yAlign = 1
    },
    handleFastPdf1() {
      this.fast = false
      const conf = this.conf
      conf.x = 0.5
      conf.y = 0.5
      conf.xAlign = 0.5
      conf.yAlign = 0.5
      conf.textColor = '#666'
      conf.rotation = 30
      conf.opacity = 70
      conf.size = 0.05
    },
    handleFastPdf2() {
      this.fast = false
      const conf = this.conf
      conf.x = 0.95
      conf.y = 0.1
      conf.xAlign = 1
      conf.yAlign = 1
      conf.rotation = 0
      conf.opacity = 70
      conf.textColor = '#666'
      conf.size = 0.05
    },
    handleFastVideo1() {
      this.fast = false
      const conf = this.conf
      conf.x = 0.95
      conf.y = 0.95
      conf.xAlign = 1
      conf.yAlign = 1
      conf.rotation = 0
      conf.opacity = 50
      conf.textColor = '#666'
    },
    handleFastImage1() {
      this.fast = false
      const conf = this.conf
      conf.x = 0.5
      conf.y = 1
      conf.xAlign = 0.5
      conf.yAlign = 1
      conf.textColor = '#fff'
      conf.rotation = 0
      conf.opacity = 95
    },
    handleFastImage2() {
      this.fast = false
      this.handleFastImage1()
      const conf = this.conf
      conf.x = 1
      conf.xAlign = 1
    }
  }
}
</script>

<style scoped>
  .preview-box{ min-height: 500px; padding: 10px; background-color: #f3f4f7; border: 1px solid #eee; border-radius: 5px; position: relative; }
  .watermark{ overflow: hidden; transition: all .2s; position: absolute; border-radius: 3px; display: flex; align-items: center; justify-content: center; }
  @keyframes ani-bling {
    0% { border: 1px solid transparent; }
    50% { border: 1px solid #ccc; }
    100% { border: 1px solid transparent; }
  }

  .fast-setting__item { margin-bottom: 15px; }
  .fast-setting__label{ color: #aaa; font-size: 14px; margin-bottom: 5px; }
</style>
