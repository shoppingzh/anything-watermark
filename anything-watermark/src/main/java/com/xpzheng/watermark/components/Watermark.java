package com.xpzheng.watermark.components;

import java.io.File;

/**
 * 水印
 * 
 * @author xpzheng
 *
 */
public abstract class Watermark {

    private float x;
    private float y;
    private Align xAlign;
    private Align yAlign;
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

        public TextWatermark createText(String content) {
            TextWatermark watermark = new TextWatermark(content);
            init(watermark);
            return watermark;
        }

        public TextWatermark createText(String content, float textSize, Color textColor) {
            TextWatermark watermark = createText(content);
            watermark.setTextSize(textSize);
            watermark.setTextColor(textColor);
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
            watermark.rotation = this.rotation;
            watermark.opacity = this.opacity;
            watermark.front = this.front;
        }
    }

}
