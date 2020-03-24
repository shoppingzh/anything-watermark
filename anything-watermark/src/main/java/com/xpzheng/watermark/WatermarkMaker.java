package com.xpzheng.watermark;

import java.io.File;

import com.xpzheng.watermark.components.Watermark;

/**
 * 水印生成器
 * @author xpzheng
 *
 */
public abstract class WatermarkMaker {

    private File src;
    private File dest;
    private Watermark watermark;

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

}
