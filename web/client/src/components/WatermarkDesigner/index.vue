<template>
  <div>
    <Row>
      <i-col span="16">
        <div ref="box" class="preview-box">
          <div ref="watermark" id="watermark" :style="styles">
            <div v-if="conf.type == 1">{{ conf.text }}</div>
            <div v-else-if="conf.type == 2">
              
            </div>
          </div>
        </div>
      </i-col>
      <i-col span="8">
        <Form :label-width="80">
          <FormItem label="水印类型">
            <RadioGroup v-model="position">
              <Radio :label="1">左上</Radio>
              <Radio :label="2">右上</Radio>
              <Radio :label="3">中间</Radio>
              <Radio :label="4">左下</Radio>
              <Radio :label="5">左下</Radio>
            </RadioGroup>
          </FormItem>
          <FormItem label="水印类型">
            <RadioGroup v-model="conf.type">
              <Radio :label="1">文字水印</Radio>
              <Radio :label="2">图片水印</Radio>
            </RadioGroup>
          </FormItem>
          <FormItem v-show="conf.type ===  1" label="文字内容">
            <i-input v-model="conf.text"></i-input>
          </FormItem>
          <FormItem v-show="conf.type == 1" label="文字颜色">
            <ColorPicker v-model="conf.textColor"/>
          </FormItem>
          <FormItem v-show="conf.type == 1" label="文字大小">
            <InputNumber :max="40" :min="10" :step="2" v-model="conf.textSize" ></InputNumber>
          </FormItem>
          <FormItem v-show="conf.type == 2" label="水印图片">
            <Upload action="">
                <Button icon="ios-cloud-upload-outline">选择文件</Button>
            </Upload>
          </FormItem>
          <FormItem label="旋转角度">
            <Slider v-model="conf.rotation" :min="0" :max="360" :step="1"></Slider>
          </FormItem>
          <FormItem label="透明度">
            <Slider v-model="conf.opacity" :min="0" :max="100" :step="1"></Slider>
          </FormItem>
          <FormItem>
            <Button type="primary" long  @click="$emit('designEnd')">好了 <Icon type="md-arrow-round-forward" /></Button>
          </FormItem>
        </Form>
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
        text: '',
        textColor: '#000',
        textSize: 14,
        rotation: 0,
        opacity: 100,
        x: 0,
        y: 0
      }
    }
  },
  computed: {
    position: {
      get() {
        const x = this.conf.x
        const y = this.conf.y
        if (x == 0 && y == 0) {
          return 1
        } else if (x == 1 && y == 0) {
          return 2
        } else if (x == 0.5 && y == 0.5) {
          return 3
        } else if (x == 0 && y == 1) {
          return 4
        } else if (x == 1 && y == 1) {
          return 5
        } else {
          return -1
        }
      },
      set(val) {
        if (val == 1) {
          this.conf.x = 0
          this.conf.y = 0
        } else if (val == 2) {
          this.conf.x = 1
          this.conf.y = 0
        } else if (val == 3) {
          this.conf.x = 0.5
          this.conf.y = 0.5
        } else if (val == 4) {
          this.conf.x = 0
          this.conf.y = 1
        } else if (val == 5) {
          this.conf.x = 1
          this.conf.y = 1
        } else {
          this.conf.x = 0
          this.conf.y = 0
        }
      }
    },
    styles: function() {
      const conf = this.conf
      const box = this.$refs.box
      const watermark = this.$refs.watermark
      const x = conf.x, y = conf.y
      
      let left = 0, top = 0
      if (box && watermark) { // 防止DOM没有准备好
        const mw = box.offsetWidth, mh = box.offsetHeight
        const w = watermark.offsetWidth, h = watermark.offsetHeight

        if(x === 0.5) {
          left = mw / 2 - w / 2
        } else if (x === 1) {
          left = mw - w
        }
        if (y === 0.5) {
          top = mh / 2 - h / 2
        } else if(y === 1) {
          top = mh - h
        }
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
    }
  }
}
</script>

<style scoped>
  .preview-box{ height: 500px; background-color: #f3f4f7; border: 1px solid #eee; border-radius: 5px; position: relative }
  #watermark{ min-width: 80px; min-height: 40px; padding: 3px 5px; background-color: rgba(65, 65, 65, .05); border: 1px solid #ddd; position: absolute; border-radius: 3px; display: flex; align-items: center; justify-content: center; }
</style>
