package com.xpzheng.watermark;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

import com.xpzheng.watermark.components.Watermark;
import com.xpzheng.watermark.maker.ImageWatermarkMaker;
import com.xpzheng.watermark.maker.PdfWatermarkMaker;
import com.xpzheng.watermark.maker.VideoWatermarkMaker;
import com.xpzheng.watermark.maker.WordWatermarkMaker;

/**
 * 万物皆可水印门面工具类
 * 
 * @author xpzheng
 *
 */
public class AnythingWatermark {

    private static final String[] PDF_SUFFIX = { "pdf" };
    private static final String[] VIDEO_SUFFIX = { "mp4", "flv" };
    private static final String[] IMAGE_SUFFIX = { "jpg", "jpeg", "webp", "png", "gif" };
    private static final String[] WORD_SUFFIX = { "doc", "docx" };

    /**
     * 为某个文件添加水印
     * 
     * @param src 源文件
     * @param dest 目标文件
     * @param watermark 水印
     * @throws Exception
     */
    public static void make(File src, File dest, Watermark watermark) throws WatermarkException {
        if (src == null || !src.exists()) {
            throw new IllegalArgumentException("源文件不存在！");
        }
        String filename = src.getName();
        boolean pdf = canWatermark(filename, PDF_SUFFIX);
        boolean video = canWatermark(filename, VIDEO_SUFFIX);
        boolean image = canWatermark(filename, IMAGE_SUFFIX);
        boolean word = canWatermark(filename, WORD_SUFFIX);
        WatermarkMaker maker = null;
        if (pdf) {
            maker = new PdfWatermarkMaker(src, dest);
        } else if (video) {
            maker = new VideoWatermarkMaker(src, dest);
        } else if (image) {
            maker = new ImageWatermarkMaker(src, dest, ImageWatermarkMaker.FORMAT_JPG);
        } else if (word) {
            maker = new WordWatermarkMaker(src, dest);
        }

        if (maker == null) {
            throw new WatermarkException(WatermarkException.ERR_UNSUPPORTED);
        }

        maker.make(watermark);
    }

    /**
     * 判断指定文件名是否支持加水印
     * 
     * @param filename 文件名
     * @param suffixes 标准后缀
     * @return
     */
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
