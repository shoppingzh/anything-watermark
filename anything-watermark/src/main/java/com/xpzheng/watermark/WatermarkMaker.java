package com.xpzheng.watermark;

import java.io.File;

import com.xpzheng.watermark.components.Watermark;

/**
 * 水印生成器
 * @author xpzheng
 *
 */
public abstract class WatermarkMaker {

    protected File src;
    protected File dest;
    protected Watermark watermark;

    public WatermarkMaker() {
        super();
    }

    public WatermarkMaker(File src, File dest, Watermark watermark) {
        super();
        this.src = src;
        this.dest = dest;
        this.watermark = watermark;
    }

    public File getSrc() {
        return src;
    }

    public void setSrc(File src) {
        this.src = src;
    }

    public File getDest() {
        return dest;
    }

    public void setDest(File dest) {
        this.dest = dest;
    }

    public Watermark getWatermark() {
        return watermark;
    }

    public void setWatermark(Watermark watermark) {
        this.watermark = watermark;
    }

    /**
     * 制作水印
     * 
     * @throws Exception
     */
    public abstract void make() throws Exception;
    
    /**
     * 坐标转换
     * @param coord
     * @param full
     * @return
     */
    public static float transCoord(float coord, float full) {
        float ac = Math.abs(coord);
        if (ac > 0 && ac < 1) {
            coord = full * coord;
        }
        return coord < 0 ? full + coord : coord;
    }

}
