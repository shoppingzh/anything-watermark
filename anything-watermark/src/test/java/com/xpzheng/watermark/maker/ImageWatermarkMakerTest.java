package com.xpzheng.watermark.maker;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Test;

import com.xpzheng.watermark.components.Align;
import com.xpzheng.watermark.components.Color;
import com.xpzheng.watermark.components.TextWatermark;
import com.xpzheng.watermark.components.Watermark;

public class ImageWatermarkMakerTest {
    
    private static final String BASE_PATH = "d:/watermark/image/";
    private static final File SRC = new File(BASE_PATH + "1.jpg");
    
//    @Test
//    public void testFullDemo() {
//        Watermark watermark = new Watermark.Builder()
//            .center()
//            .opactiy(0.85f)
//            .rotate(30)
//            .createText("给我一张过去的CD！", 100, Color.valueOf(0, 255, 255, 255));
//        Watermark watermark2 = new Watermark.Builder()
//            .rightBottom()
//            .opactiy(0.5f)
//            .rotate(180)
//            .createImage("d:/watermark/logo.png");
//        new ImageWatermarkMaker(SRC, new File(BASE_PATH + "1-full-text.jpg")).make(watermark);
//        new ImageWatermarkMaker(SRC, new File(BASE_PATH + "1-full-image.jpg")).make(watermark2);
//    }
//    
//    @Test
//    public void testLeftTop() {
//        Watermark watermark = new Watermark.Builder().leftTop().createText("你好，水印！", 40, Color.valueOf(0, 255, 255, 255));
//        Watermark watermark2 = new Watermark.Builder().leftTop().createImage("d:/watermark/logo.png");
//        new ImageWatermarkMaker(SRC, new File(BASE_PATH + "1-leftTop-text.jpg")).make(watermark);
//        new ImageWatermarkMaker(SRC, new File(BASE_PATH + "1-leftTop-image.jpg")).make(watermark2);
//    }
//    
//    @Test
//    public void testLeftBottom() {
//        Watermark watermark = new Watermark.Builder().leftBottom().createText("你好，水印！", 40, Color.valueOf(0, 255, 255, 255));
//        Watermark watermark2 = new Watermark.Builder().leftBottom().createImage("d:/watermark/logo.png");
//        new ImageWatermarkMaker(SRC, new File(BASE_PATH + "1-leftBottom-text.jpg")).make(watermark);
//        new ImageWatermarkMaker(SRC, new File(BASE_PATH + "1-leftBottom-image.jpg")).make(watermark2);
//    }
//    
//    @Test
//    public void testCenter() {
//        Watermark watermark = new Watermark.Builder().center().createText("你好，水印！", 40, Color.valueOf(0, 255, 255, 255));
//        Watermark watermark2 = new Watermark.Builder().center().createImage("d:/watermark/logo.png");
//        new ImageWatermarkMaker(SRC, new File(BASE_PATH + "1-center-text.jpg")).make(watermark);
//        new ImageWatermarkMaker(SRC, new File(BASE_PATH + "1-center-image.jpg")).make(watermark2);
//    }
//    
//    @Test
//    public void testRightTop() {
//        Watermark watermark = new Watermark.Builder().rightTop().createText("你好，水印！", 40, Color.valueOf(0, 255, 255, 255));
//        Watermark watermark2 = new Watermark.Builder().rightTop().createImage("d:/watermark/logo.png");
//        new ImageWatermarkMaker(SRC, new File(BASE_PATH + "1-rightTop-text.jpg")).make(watermark);
//        new ImageWatermarkMaker(SRC, new File(BASE_PATH + "1-rightTop-image.jpg")).make(watermark2);
//    }
//    
//    @Test
//    public void testRightBottom() {
//        Watermark watermark = new Watermark.Builder().rightBottom().createText("你好，水印！", 40, Color.valueOf(0, 255, 255, 255));
//        Watermark watermark2 = new Watermark.Builder().rightBottom().createImage("d:/watermark/logo.png");
//        new ImageWatermarkMaker(SRC, new File(BASE_PATH + "1-rightBottom-text.jpg")).make(watermark);
//        new ImageWatermarkMaker(SRC, new File(BASE_PATH + "1-rightBottom-image.jpg")).make(watermark2);
//    }
//    
//    @Test
//    public void testOpacity() {
//        Watermark watermark = new Watermark.Builder()
//            .center()
//            .opactiy(0.5f)
//            .createText("你好，水印！", 40, Color.valueOf(0, 255, 255, 255));
//        Watermark watermark2 = new Watermark.Builder()
//            .center()
//            .opactiy(0.75f)
//            .createImage("d:/watermark/logo.png");
//        new ImageWatermarkMaker(SRC, new File(BASE_PATH + "1-opacity-text.jpg")).make(watermark);
//        new ImageWatermarkMaker(SRC, new File(BASE_PATH + "1-opacity-image.jpg")).make(watermark2);
//    }
//    
//    @Test
//    public void testRotation() {
//        Watermark watermark = new Watermark.Builder()
//            .center()
//            .rotate(75)
//            .createText("你好，水印！", 40, Color.valueOf(0, 255, 255, 255));
//        Watermark watermark2 = new Watermark.Builder()
//            .center()
//            .rotate(-75)
//            .createImage("d:/watermark/logo.png");
//        new ImageWatermarkMaker(SRC, new File(BASE_PATH + "1-rotation-text.jpg")).make(watermark);
//        new ImageWatermarkMaker(SRC, new File(BASE_PATH + "1-rotation-image.jpg")).make(watermark2);
//    }
//    
//    @Test
//    public void testFontSize() {
//        TextWatermark watermark = new Watermark.Builder()
//            .center()
//            .rotate(75)
//            .createText("你好，水印！", 20, Color.valueOf(0, 255, 255, 255));
//        new ImageWatermarkMaker(SRC, new File(BASE_PATH + "1-fontSize-20.jpg")).make(watermark);
//        
//        watermark.setTextSize(30);
//        new ImageWatermarkMaker(SRC, new File(BASE_PATH + "1-fontSize-30.jpg")).make(watermark);
//        
//        watermark.setTextSize(40);
//        new ImageWatermarkMaker(SRC, new File(BASE_PATH + "1-fontSize-40.jpg")).make(watermark);
//        
//        watermark.setTextSize(50);
//        new ImageWatermarkMaker(SRC, new File(BASE_PATH + "1-fontSize-50.jpg")).make(watermark);
//        
//        watermark.setTextSize(60);
//        new ImageWatermarkMaker(SRC, new File(BASE_PATH + "1-fontSize-60.jpg")).make(watermark);
//        
//        watermark.setTextSize(70);
//        new ImageWatermarkMaker(SRC, new File(BASE_PATH + "1-fontSize-70.jpg")).make(watermark);
//
//        watermark.setTextSize(120);
//        new ImageWatermarkMaker(SRC, new File(BASE_PATH + "1-fontSize-120.jpg")).make(watermark);
//        
//        watermark.setTextSize(200);
//        new ImageWatermarkMaker(SRC, new File(BASE_PATH + "1-fontSize-200.jpg")).make(watermark);
//    }
    
    public void testDifferentSize() {
        Watermark watermark = new Watermark.Builder()
//            .position(0.5f, 0.5f)
//            .align(Align.CENTER, Align.CENTER)
            .rightBottom()
            .opactiy(0.5f)
            .size(0.01f)
            .createImage("d:/1.png");
//            .createText("夜", 12, Color.valueOf(255, 255, 255, 255));
        File path = new File(BASE_PATH + "size");
        File outDir = new File(BASE_PATH + "size/output");
        if (!outDir.exists()) {
            outDir.mkdirs();
        }
        File[] files = path.listFiles();
        if (files.length > 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    continue;
                }
                String filename = FilenameUtils.getBaseName(file.getName()) + "-text." + FilenameUtils.getExtension(file.getName());
                new ImageWatermarkMaker(file, new File(outDir, filename), ImageWatermarkMaker.FORMAT_JPG).make(watermark);
            }
        }
    }
    
}
