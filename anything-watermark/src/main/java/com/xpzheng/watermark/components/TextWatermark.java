package com.xpzheng.watermark.components;

/**
 * 文字水印
 * 
 * @author xpzheng
 *
 */
public class TextWatermark extends Watermark {

    private String content;
    private float textSize = 14f;
    private Color textColor = Color.valueOf(0, 0, 0, 0);

    public TextWatermark(String content) {
        super();
        this.content = content;
    }

    public TextWatermark(String content, float fontSize) {
        super();
        this.content = content;
        this.textSize = fontSize;
    }

    public TextWatermark(String content, float fontSize, Color color) {
        super();
        this.content = content;
        this.textSize = fontSize;
        this.textColor = color;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

}
