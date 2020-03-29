package com.xpzheng.watermark.components;

import java.math.BigInteger;

/**
 * 颜色
 * @author xpzheng
 *
 */
public class Color {

    /**
     * default color
     */
    public static final Color DEFAULT = Color.valueOf(64, 64, 64, 255);

    private int r;
    private int g;
    private int b;
    private int a = 0xff;

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    /**
     * 通过rgba分量创建Color
     * @param r
     * @param g
     * @param b
     * @param a
     * @return
     */
    public static Color valueOf(int r, int g, int b, int a) {
        Color color = new Color();
        color.r = r;
        color.g = g;
        color.b = b;
        color.a = a;
        return color;
    }

    /**
     * 将16进制的颜色值转为Color(不包含alpha)
     * @param hex
     * @return
     */
    public static Color valueOf(String hex) {
        if (hex == null) {
            throw new IllegalArgumentException("颜色值为空！");
        }
        hex = hex.trim();
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }
        if (hex.length() != 3 && hex.length() != 6) {
            throw new IllegalArgumentException("非法颜色值！");
        }
        if (hex.length() == 3) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0, len = hex.length(); i < len; i++) {
                char c = hex.charAt(i);
                sb.append(c).append(c);
            }
            hex = sb.toString();
        }
        int r = 0, g = 0, b = 0;
        for (int i = 0, len = hex.length(); i < len; i += 2) {
            if (i + 1 >= len) {
                break;
            }
            String h = "" + hex.charAt(i) + hex.charAt(i + 1);
            int c = new BigInteger(h, 16).intValue();
            if (i == 0) {
                r = c;
            } else if (i == 2) {
                g = c;
            } else if (i == 4) {
                b = c;
            }
        }
        return valueOf(r, g, b, 0xff);
    }
}
