<template>
  <div>
    <Row>
      <i-col span="16">
        <div class="preview-box">
          <div id="watermark" :style="styles">
            <div v-if="type == 1">{{ text }}</div>
            <div v-else-if="type == 2">
              
            </div>
          </div>
        </div>
      </i-col>
      <i-col span="8">
        <Form :label-width="80">
          <FormItem label="水印类型">
            <RadioGroup v-model="type">
              <Radio :label="1">文字水印</Radio>
              <Radio :label="2">图片水印</Radio>
            </RadioGroup>
          </FormItem>
          <FormItem v-show="type ===  1" label="文字内容">
            <i-input v-model="text"></i-input>
          </FormItem>
          <FormItem v-show="type == 1" label="文字颜色">
            <ColorPicker v-model="textColor"/>
          </FormItem>
          <FormItem v-show="type == 1" label="文字大小">
            <InputNumber :max="40" :min="10" :step="2" v-model="textSize" ></InputNumber>
          </FormItem>
          <FormItem label="旋转角度">
            <Slider v-model="rotation" :min="0" :max="360" :step="1"></Slider>
          </FormItem>
          <FormItem label="透明度">
            <Slider v-model="opacity" :min="0" :max="100" :step="1"></Slider>
          </FormItem>
          <FormItem>
            <Button type="primary" long>好了 <Icon type="md-arrow-round-forward" @click="$emit('designEnd')" /></Button>
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
      type: 1,
      text: '',
      textColor: '#000',
      textSize: 14,
      rotation: 0,
      opacity: 100,
      x: 150,
      y: 120
    }
  },
  computed: {
    styles: function() {
      return {
        transform: `rotate(${this.rotation}deg)`,
        opacity: this.opacity / 100,
        color: this.textColor,
        fontSize: this.textSize + 'px',
        left: this.x + 'px',
        top: this.y + 'px'
      };
    }
  },
  methods: {
  }
}
</script>

<style scoped>
  .preview-box{ height: 500px; background-color: #f3f4f7; border: 1px solid #eee; border-radius: 5px; position: relative }
  #watermark{ min-width: 80px; min-height: 40px; padding: 3px 5px; background-color: rgba(65, 65, 65, .05); border: 1px solid #ddd; position: absolute; border-radius: 3px; display: flex; align-items: center; justify-content: center; }
</style>
