//package com.xpzheng.watermark.maker;
//
//import java.io.File;
//
//import org.junit.jupiter.api.Test;
//
//import com.xpzheng.watermark.components.Watermark;
//
//public class VideoWatermarkMakerTest {
//
//    private static final File SRC = new File("d:/watermark/video/1.mp4");
//
//    @Test
//    public void testPositionLeftTop() {
//        Watermark watermark = new Watermark.Builder().leftTop().createText("左上角水印");
//        new VideoWatermarkMaker(SRC, new File("d:/watermark/leftTop.mp4")).make(watermark);
//    }
//
//    @Test
//    public void testPositionLeftBottom() {
//        Watermark watermark = new Watermark.Builder().leftBottom().createText("左下角水印");
//        new VideoWatermarkMaker(SRC, new File("d:/watermark/leftBottom.mp4")).make(watermark);
//    }
//
//    @Test
//    public void testPositionCenter() {
//        Watermark watermark = new Watermark.Builder().center().createText("中间水印");
//        new VideoWatermarkMaker(SRC, new File("d:/watermark/center.mp4")).make(watermark);
//    }
//
//    @Test
//    public void testPositionRightTop() {
//        Watermark watermark = new Watermark.Builder().rightTop().createText("右上角水印");
//        new VideoWatermarkMaker(SRC, new File("d:/watermark/rightTop.mp4")).make(watermark);
//    }
//
//    @Test
//    public void testPositionRightBottom() {
//        Watermark watermark = new Watermark.Builder().rightBottom().createText("右下角水印");
//        new VideoWatermarkMaker(SRC, new File("d:/watermark/rightBottom.mp4")).make(watermark);
//    }
//    
//    @Test
//    public void testOpacity() {
//        Watermark watermark = new Watermark.Builder().center().opactiy(0.5f).createText("你好，水印！");
//        Watermark watermark2 = new Watermark.Builder().center().opactiy(0.75f).createImage("d:/watermark/logo.png");
//        new VideoWatermarkMaker(SRC, new File("d:/watermark/1-opacity-text.mp4")).make(watermark);
//        new VideoWatermarkMaker(SRC, new File("d:/watermark/1-opacity-image.mp4")).make(watermark2);
//    }
//    
//    @Test
//    public void testH264() {
//        Watermark watermark = new Watermark.Builder().center().opactiy(0.5f).createText("你好，水印！");
//        new VideoWatermarkMaker(SRC, new File("d:/watermark/1-h264.mp4")).make(watermark);
//    }
//
//}
