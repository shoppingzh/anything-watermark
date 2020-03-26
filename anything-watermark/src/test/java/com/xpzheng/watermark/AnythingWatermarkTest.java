//package com.xpzheng.watermark;
//
//import java.io.File;
//
//import org.junit.jupiter.api.Test;
//
//
//public class AnythingWatermarkTest {
//
//    private static final File PDF = new File("d:/watermark/1.pdf");
//    private static final File VIDEO = new File("d:/watermark/1.mp4");
//
//    @Test
//    public void testMakePdf1() {
//        AnythingWatermark.make(PDF, new File("d:/watermark/2.pdf"), Utils.defaultTextWatermark());
//    }
//
//    @Test
//    public void testMakePdf2() {
//        AnythingWatermark.make(PDF, new File("d:/watermark/3.pdf"), Utils.defaultImageWatermark());
//    }
//
//    @Test
//    public void testMakeVideo1() {
//        AnythingWatermark.make(VIDEO, new File("d:/watermark/2.mp4"), Utils.defaultTextWatermark());
//    }
//
//    public void testMakeVideo2() {
//        AnythingWatermark.make(VIDEO, new File("d:/watermark/3.mp4"), Utils.defaultImageWatermark());
//    }
//    
//}
