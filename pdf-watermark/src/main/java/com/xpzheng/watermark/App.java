package com.xpzheng.watermark;

import java.io.File;

import com.xpzheng.watermark.pdf.PdfUtils;

public class App {

    public static void main(String[] args) {
        try {
            PdfUtils.textWatermark(new File("d:/1.pdf"), "d:/watermark-text.pdf", "Hello, world! 你好世界！", 0.5f, 0.5f, -45, .45f);
            PdfUtils.imageWatermark(new File("d:/1.pdf"), "d:/watermark-image.pdf", "d:/watermark.png", 0.5f, 0.5f, -30, .25f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
