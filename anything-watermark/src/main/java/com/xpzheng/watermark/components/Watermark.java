package com.xpzheng.watermark.components;

/**
 * ˮӡ
 * 
 * @author xpzheng
 *
 */
public abstract class Watermark {

    private float x;
    private float y;
    private float rotation;
    private float opacity;
    private boolean front = false;

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
        return "Watermark [x=" + x + ", y=" + y + ", rotation=" + rotation + ", opacity=" + opacity + ", front=" + front + "]";
    }

    public static class Builder {

        float x;
        float y;
        float rotation;
        float opacity;
        boolean front;

        public Builder position(float x, float y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder center() {
            this.x = 0.5f;
            this.y = 0.5f;
            return this;
        }

        public Builder leftTop() {
            this.x = 0;
            this.y = 0.99f;
            return this;
        }

        public Builder leftBottom() {
            this.x = 0;
            this.y = -0.99f;
            return this;
        }

        public Builder rightTop() {
            this.x = -0.99f;
            this.y = 0;
            return this;
        }

        public Builder rightBottom() {
            this.x = -0.99f;
            this.y = -0.99f;
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

        private void init(Watermark watermark) {
            watermark.x = this.x;
            watermark.y = this.y;
            watermark.rotation = this.rotation;
            watermark.opacity = this.opacity;
            watermark.front = this.front;
        }
    }

    public static void main(String[] args) {
        Watermark watermark = new Watermark.Builder().center().opactiy(0.5f).createText("Hello, world!");
        System.out.println(watermark);
    }

}
