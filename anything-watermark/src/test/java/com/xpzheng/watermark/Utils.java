package com.xpzheng.watermark;

import com.xpzheng.watermark.components.Color;
import com.xpzheng.watermark.components.Watermark;

public class Utils {
    
    public static Watermark defaultTextWatermark() {
        return new Watermark.Builder()
            .leftBottom()
            .front(true)
            .opactiy(.85f)
            .rotate(-45)
            .createText("你好，水印！", 28, Color.valueOf(200, 200, 200, 255));
    }
    
    public static Watermark defaultImageWatermark() {
        return new Watermark.Builder()
            .leftBottom()
            .front(true)
            .opactiy(.75f)
            .rotate(0)
            .createImage("d:/watermark/logo.png");
    }

}
