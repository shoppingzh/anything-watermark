/*package com.xpzheng.watermark.maker;

import java.io.File;

import org.junit.jupiter.api.Test;

import com.xpzheng.watermark.components.Color;
import com.xpzheng.watermark.components.Watermark;

public class PdfWatermarkMakerTest {
    
    public static final String BASE_PATH = "d:/watermark/pdf/";
    
    public static final File SRC = new File(BASE_PATH + "1.pdf");
    
//    public void testFullDemo() {
//        Watermark watermark = new Watermark.Builder()
//            .center()
//            .opactiy(0.85f)
//            .rotate(45)
//            .front(true)
//            .createText("你好，水印！", 35, Color.valueOf(145, 145, 145, 255));
//        Watermark watermark2 = new Watermark.Builder()
//        .rightBottom()
//        .opactiy(0.55f)
//        .rotate(0)
//        .front(true)
//        .createImage("d:/watermark/logo.png");
//        new PdfWatermarkMaker(SRC, new File(BASE_PATH + "1-full-text.pdf")).make(watermark);
//        new PdfWatermarkMaker(SRC, new File(BASE_PATH + "1-full-image.pdf")).make(watermark2);
//    }
//    
//    @Test
//    public void testPositionLeftTop() {
//        Watermark watermark = new Watermark.Builder()
//            .leftTop()
//            .createText("左上角水印");
//        new PdfWatermarkMaker(SRC, new File(BASE_PATH + "1-leftTop.pdf")).make(watermark);
//    }
//    
//    @Test
//    public void testPositionLeftBottom() {
//        Watermark watermark = new Watermark.Builder()
//            .leftBottom()
//            .createText("左下角水印");
//        new PdfWatermarkMaker(SRC, new File(BASE_PATH + "1-leftBottom.pdf")).make(watermark);
//    }
//
//    @Test
//    public void testPositionCenter() {
//        Watermark watermark = new Watermark.Builder()
//            .center()
//            .createText("中间水印");
//        new PdfWatermarkMaker(SRC, new File(BASE_PATH + "1-center.pdf")).make(watermark);
//    }
//    
//    @Test
//    public void testPositionRightTop() {
//        Watermark watermark = new Watermark.Builder()
//            .rightTop()
//            .createText("右上角水印");
//        new PdfWatermarkMaker(SRC, new File(BASE_PATH + "1-rightTop.pdf")).make(watermark);
//    }
//    
//    @Test
//    public void testPositionRightBottom() {
//        Watermark watermark = new Watermark.Builder()
//            .rightBottom()
//            .createText("右下角水印");
//        new PdfWatermarkMaker(SRC, new File(BASE_PATH + "1-rightBottom.pdf")).make(watermark);
//    }
    
//    @Test
//    public void testDiffrentSizeWatermark() {
//        Watermark watermark = new Watermark.Builder()
//            .center()
//            .createText("你好，水印！");
////            .createImage("d:/watermark/logo3.png");
//        for(float size = 0.005f; size <= 0.4; size += 0.005f) {
//            watermark.setSize(size);
//            new PdfWatermarkMaker(SRC, new File("d:/watermark/pdf/1-" + size + ".pdf")).make(watermark);
//        }
//    }

}
*/