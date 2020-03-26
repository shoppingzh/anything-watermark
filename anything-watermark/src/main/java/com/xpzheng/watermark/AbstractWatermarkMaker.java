package com.xpzheng.watermark;

import java.io.File;

import com.xpzheng.watermark.components.ImageWatermark;
import com.xpzheng.watermark.components.TextWatermark;
import com.xpzheng.watermark.components.Watermark;

/**
 * 水印生成器
 * 
 * @author xpzheng
 *
 */
public abstract class AbstractWatermarkMaker implements WatermarkMaker {

    protected File src;
    protected File dest;
    protected float edgeOffset = 15;

    public AbstractWatermarkMaker() {
        super();
    }

    public AbstractWatermarkMaker(File src, File dest) {
        super();
        this.src = src;
        this.dest = dest;
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

    public float getEdgeOffset() {
        return edgeOffset;
    }

    public void setEdgeOffset(float edgeOffset) {
        this.edgeOffset = edgeOffset;
    }

    @Override
    public void make(Watermark watermark) throws WatermarkException {
        check();
        checkWatermarkValid(watermark);
    }

    /**
     * 检查参数
     */
    protected void check() {
        if (src == null || !src.exists()) {
            throw new WatermarkException(WatermarkException.ERR_SOURCE_NOT_FOUND);
        }
        if (dest == null) {
            throw new WatermarkException(WatermarkException.ERR_TARGET_NOT_FOUND);
        }
    }

    /**
     * 检查水印的合法性
     * 
     * @param watermark
     */
    private void checkWatermarkValid(Watermark watermark) {
        if (watermark instanceof TextWatermark) {
            TextWatermark textWatermark = (TextWatermark) watermark;
            if (textWatermark.getContent() == null) {
                throw new WatermarkException(WatermarkException.ERR_WATERMARK_INVALID);
            }
        } else if (watermark instanceof ImageWatermark) {
            ImageWatermark imageWatermark = (ImageWatermark) watermark;
            if (imageWatermark.getFile() != null && !imageWatermark.getFile().exists()) {
                throw new WatermarkException(WatermarkException.ERR_WATERMARK_INVALID);
            }
        }
    }

}
