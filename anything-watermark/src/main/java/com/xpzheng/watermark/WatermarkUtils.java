package com.xpzheng.watermark;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

import com.xpzheng.watermark.components.Watermark;
import com.xpzheng.watermark.maker.PdfWatermarkMaker;

public class WatermarkUtils {

    private static final String[] PDF_SUFFIX = { "pdf" };
    private static final String[] VIDEO_SUFFIX = { "mp4" };
    private static final String[] IMAGE_SUFFIX = { "jpg", "jpeg" };

    /**
     * 为某个文件添加水印
     * 
     * @param src       源文件
     * @param dest      目标文件
     * @param watermark 水印
     * @throws Exception
     */
    public static void watermark(File src, File dest, Watermark watermark) throws Exception {
        if (src == null || !src.exists()) {
            throw new IllegalArgumentException("源文件不存在！");
        }
        String filename = src.getName();
        boolean pdf = canWatermark(filename, PDF_SUFFIX);
        boolean video = canWatermark(filename, VIDEO_SUFFIX);
        boolean image = canWatermark(filename, IMAGE_SUFFIX);
        if (pdf) {
            new PdfWatermarkMaker(src, dest, watermark).make();
        } else if (video) {
            
        } else if (image) {
            
        } else {
            throw new RuntimeException("该类型的文件无法添加水印！");
        }
        
    }

    private static boolean canWatermark(String filename, String[] suffixes) {
        String extension = FilenameUtils.getExtension(filename);
        for (String suffix : suffixes) {
            if (suffix.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }
    

}
