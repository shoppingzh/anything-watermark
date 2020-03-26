package com.xpzheng.watermark.components;

import java.io.File;

/**
 * 图片水印
 * 
 * @author xpzheng
 *
 */
public class ImageWatermark extends Watermark {

    private float width;
    private float height;
    private File file;
    private byte[] data;

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

}
