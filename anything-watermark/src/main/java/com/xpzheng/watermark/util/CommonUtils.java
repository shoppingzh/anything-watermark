package com.xpzheng.watermark.util;

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
    
    public static void main(String[] args) {
        System.out.println(rgba2Hex(255, 255, 255, 254));
    }

}
