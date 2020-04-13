package com.xpzheng.watermark.components;

import java.io.File;

/**
 * 水印<br>
 * 设置项：<br>
 * x             横轴位置([0, 1]之间表示占横轴的百分比，负值表示反向位置)<br>
 * y             纵轴位置(同x)<br>
 * xAlign        横轴的对齐方式，可选左、中、右，将影响水印的绘制位置<br>
 * yAlign        见yAlign<br>
 * size          水印的大小，建议设置范围：[0.005 - 0.2]之间<br>
 * rotation      旋转角度<br>
 * opacity       不透明度(alpha = 100 - opactity)<br>
 * front         是否位于内容之上<br>
 * text          文字水印的内容<br>
 * textColor     文字水印的颜色<br>
 * image         图片水印的文件位置<br>
 * 
 * @author xpzheng
 *
 */
public abstract class Watermark {

    /**
     * 水印的最小尺寸
     */
    public static final float MIN_SIZE = 0.01f;

    private float x;
    private float y;
    private Align xAlign;
    private Align yAlign;
    private float size;
    private float rotation;
    private float opacity;
    private boolean front;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Align getxAlign() {
        return xAlign;
    }

    public void setxAlign(Align xAlign) {
        this.xAlign = xAlign;
    }

    public Align getyAlign() {
        return yAlign;
    }

    public void setyAlign(Align yAlign) {
        this.yAlign = yAlign;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getOpacity() {
        return opacity;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    public boolean isFront() {
        return front;
    }

    public void setFront(boolean front) {
        this.front = front;
    }

    @Override
    public String toString() {
        return "Watermark [x=" + x + ", y=" + y + ", xAlign=" + xAlign + ", yAlign=" + yAlign + ", rotation=" + rotation
            + ", opacity=" + opacity + ", front=" + front + "]";
    }

    public static class Builder {

        float x = 0;
        float y = 0;
        Align xAlign = Align.START;
        Align yAlign = Align.START;
        float size = Watermark.MIN_SIZE;
        float rotation = 0;
        float opacity = 1;
        boolean front = true;

        public Builder position(float x, float y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder center() {
            this.x = 0.5f;
            this.y = 0.5f;
            xAlign = Align.CENTER;
            yAlign = Align.CENTER;
            return this;
        }

        public Builder leftTop() {
            this.x = 0;
            this.y = 0;
            xAlign = Align.START;
            yAlign = Align.START;
            return this;
        }

        public Builder leftBottom() {
            this.x = 0;
            this.y = 1;
            xAlign = Align.START;
            yAlign = Align.END;
            return this;
        }

        public Builder rightTop() {
            this.x = 1;
            this.y = 0;
            xAlign = Align.END;
            yAlign = Align.START;
            return this;
        }

        public Builder rightBottom() {
            this.x = 1;
            this.y = 1;
            xAlign = Align.END;
            yAlign = Align.END;
            return this;
        }

        public Builder align(Align xAlign, Align yAlign) {
            this.xAlign = xAlign;
            this.yAlign = yAlign;
            return this;
        }

        public Builder size(float size) {
            this.size = size;
            return this;
        }

        public Builder rotate(float rotation) {
            this.rotation = rotation;
            return this;
        }

        public Builder opactiy(float opacity) {
            this.opacity = opacity;
            return this;
        }

        public Builder front(boolean front) {
            this.front = front;
            return this;
        }

        // Build Watermark

        public TextWatermark createText(String content) {
            TextWatermark watermark = new TextWatermark(content);
            init(watermark);
            return watermark;
        }

        public TextWatermark createText(String content, Color textColor) {
            TextWatermark watermark = createText(content);
            watermark.setTextColor(textColor);
            return watermark;
        }

        public TextWatermark createText(String content, float textSize, Color textColor) {
            TextWatermark watermark = createText(content, textColor);
            watermark.setTextSize(textSize);
            return watermark;
        }

        public ImageWatermark createImage(String imageFilename) {
            ImageWatermark watermark = new ImageWatermark();
            init(watermark);
            watermark.setFile(new File(imageFilename));
            return watermark;
        }

        private void init(Watermark watermark) {
            watermark.x = this.x;
            watermark.y = this.y;
            watermark.xAlign = this.xAlign;
            watermark.yAlign = this.yAlign;
            watermark.size = this.size;
            watermark.rotation = this.rotation;
            watermark.opacity = this.opacity;
            watermark.front = this.front;
        }
    }

}
