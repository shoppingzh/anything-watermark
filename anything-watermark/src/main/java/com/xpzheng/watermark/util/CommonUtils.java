package com.xpzheng.watermark.util;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

/**
 * 通用工具集合
 * 
 * @author xpzheng
 *
 */
public class CommonUtils {

    /**
     * rgb颜色转换hex字符串
     * 
     * @param r
     * @param g
     * @param b
     * @return
     */
    public static String rgb2Hex(int r, int g, int b) {
        return String.format("%02x%02x%02x", r % 256, g % 256, b % 256);
    }

    /**
     * rgba颜色转换hex字符串
     * 
     * @param r
     * @param g
     * @param b
     * @param a
     * @return
     */
    public static String rgba2Hex(int r, int g, int b, int a) {
        return String.format("%02x%02x%02x%02x", r % 256, g % 256, b % 256, a % 256);
    }
    
    /**
     * 测量文本获得文本的宽高信息
     * @param g   画笔
     * @param f   字体
     * @param text 被测量文本
     * @return
     */
    public static Rectangle2D measureText(Graphics g, Font f, String text) {
        FontMetrics fontMetrics = g.getFontMetrics(f);
        return fontMetrics.getStringBounds(text, g);
    }
    
    /**
     * 去除文件路径中多余的左斜线(或右斜线)，如：<br>
     * /usr//home -> /usr/home<br>
     * d:\file\\image\1.jpg -> d:\file\image\1.jpg
     * 
     * @param filePath  文件路径
     * @return
     */
    public static String removeExtraSlash(String filePath) {
        return filePath == null ? null : filePath.replaceAll("[\\/\\\\]{2,}", "/");
    }
}
