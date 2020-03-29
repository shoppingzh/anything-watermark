package com.xpzheng.watermark.components;

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
    private int a;

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
     * Create color
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
    
}
