package com.xpzheng.watermark.components;

/**
 * 文字水印
 * @author xpzheng
 *
 */
public class TextWatermark extends Watermark {

    private String content;
    private float fontSize = 14f;
    private Color color = Color.valueOf(0, 0, 0, 0);

    public TextWatermark(String content) {
        super();
        this.content = content;
    }

    public TextWatermark(String content, float fontSize) {
        super();
        this.content = content;
        this.fontSize = fontSize;
    }

    public TextWatermark(String content, float fontSize, Color color) {
        super();
        this.content = content;
        this.fontSize = fontSize;
        this.color = color;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "TextWatermark [content=" + content + ", fontSize=" + fontSize + ", color=" + color + ", getX()=" + getX()
            + ", getY()=" + getY() + ", getRotation()=" + getRotation() + ", getOpacity()=" + getOpacity() + ", isFront()="
            + isFront() + "]";
    }

}
