<template>
  <div>
    <Row>
      <i-col span="16">
        <div ref="box" class="preview-box">
          <div ref="watermark" id="watermark" :style="styles">
            <div v-if="conf.type == 1" style="white-space: nowrap;">{{ conf.text }}</div>
            <div v-else-if="conf.type == 2">
              <img v-if="conf.image" :src="conf.image">
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
            <ColorPicker v-model="conf.textColor" alpha :colors="['#333', '#666', '#999', '#aaa', '#bbb', '#ccc', '#ddd']"/>
          </FormItem>
          <FormItem v-show="conf.type == 1">
            <template #label><Icon type="ios-document-outline" /> 字体大小</template>
            <InputNumber :max="40" :min="10" :step="2" v-model="conf.textSize" ></InputNumber>
          </FormItem>
          <FormItem v-show="conf.type == 2">
            <template #label><Icon type="md-image" /> 水印图片</template>
            <Upload action="">
                <Button icon="ios-cloud-upload-outline">选择文件</Button>
            </Upload>
          </FormItem>
          <!-- 通用设置 -->
          <FormItem label="快捷设置">
            <Button type="success" ghost @click="handleFastDesign"><Icon type="ios-hammer-outline" /> 打开快捷设置面板</Button>
          </FormItem>
          <FormItem label="横轴位置">
            <Slider v-model="xOffset" :min="0" :max="100" :step="1" show-input></Slider>
          </FormItem>
          <FormItem label="横轴对齐方式">
            <RadioGroup v-model="conf.xAlign">
              <Radio :label="1">左</Radio>
              <Radio :label="0.5">中</Radio>
              <Radio :label="0">右</Radio>
            </RadioGroup>
          </FormItem>
          <FormItem label="纵轴位置">
            <Slider v-model="yOffset" :min="0" :max="100" :step="1" show-input></Slider>
          </FormItem>
          <FormItem label="纵轴对齐方式">
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
    
  </div>
</template>

<script>
export default {
  data() {
    return {
      conf: {
        type: 1,
        x: 0,
        y: 0,
        xAlign: 0,
        yAlign: 0,
        rotation: 0,
        opacity: 100,
        text: '内部使用，请勿盗版！',
        textColor: '#000',
        textSize: 14,
        image: 'https://avatars2.githubusercontent.com/u/18109723?s=460&u=7cdf35a8819de78fe3b0c2611b66967082f519ac&v=4'
      },
      fast: false
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
    styles: function() {
      const conf = this.conf
      const box = this.$refs.box
      const watermark = this.$refs.watermark
      const x = conf.x, y = conf.y
      const xa = conf.xAlign, ya = conf.yAlign
      // 文字影响宽度，在这里给个引用，保证能更新位置
      this.conf.type
      this.conf.text
      this.conf.image
      
      let left = 0, top = 0
      if (box && watermark) { // 防止DOM没有准备好
        const mw = box.offsetWidth, mh = box.offsetHeight
        const w = watermark.offsetWidth, h = watermark.offsetHeight

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
        fontSize: conf.textSize + 'px',
        left: left + 'px',
        top: top + 'px'
      };
    }
  },
  mounted() {
  },
  methods: {
    config() {
      return Object.assign({}, this.conf);
    },
    handleFastDesign() {
      this.fast = true
    },
    handleFastPdf1() {
      this.fast = false
      const conf = this.conf
      conf.textSize = 24
      conf.x = 0.5
      conf.y = 0.5
      conf.xAlign = 0.5
      conf.yAlign = 0.5
      conf.textColor = '#ccc'
      conf.rotation = 30
      conf.opacity = 30
    },
    handleFastPdf2() {
      this.fast = false
      const conf = this.conf
      conf.x = 0.95
      conf.y = 0.1
      conf.xAlign = 1
      conf.yAlign = 1
      conf.rotation = 0
      conf.opacity = 50
      conf.textColor = '#ccc'
      conf.textSize = 16
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
      conf.textSize = 16
    },
    handleFastImage1() {
      this.fast = false
      const conf = this.conf
      conf.textSize = 14
      conf.x = 0.5
      conf.y = 1
      conf.xAlign = 0.5
      conf.yAlign = 1
      conf.textColor = '#999'
      conf.rotation = 0
      conf.opacity = 80
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
  .preview-box{ height: 500px; background-color: #f3f4f7; border: 1px solid #eee; border-radius: 5px; position: relative }
  #watermark{ min-width: 50px; min-height: 30px; padding: 3px 5px; overflow: hidden; transition: all .2s; position: absolute; border-radius: 3px; display: flex; align-items: center; justify-content: center; }
  @keyframes ani-bling {
    0% { border: 1px solid transparent; }
    50% { border: 1px solid #ccc; }
    100% { border: 1px solid transparent; }
  }

  .fast-setting__item { margin-bottom: 15px; }
  .fast-setting__label{ color: #aaa; font-size: 14px; margin-bottom: 5px; }
</style>
