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

    // 不对外开放无参构造函数
    protected AbstractWatermarkMaker() {
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

    public File getDest() {
        return dest;
    }

    public float getEdgeOffset() {
        return edgeOffset;
    }

    public void setEdgeOffset(float edgeOffset) {
        this.edgeOffset = edgeOffset;
    }

    @Override
    public final void make(Watermark watermark) throws WatermarkException {
        check();
        checkWatermarkValid(watermark);
        if (!beforeMake(watermark)) {
            return;
        }
        if (watermark instanceof TextWatermark) {
            makeForText((TextWatermark) watermark);
        } else if (watermark instanceof ImageWatermark) {
            makeForImage((ImageWatermark) watermark);
        } else {
            throw new WatermarkException(WatermarkException.ERR_UNSUPPORTED);
        }
        afterMake(watermark);
    }

    /**
     * 创建水印前(准备工作)
     * 
     * @param watermark
     * @return
     */
    protected boolean beforeMake(Watermark watermark) {
        return true;
    }

    /**
     * 创建文字水印
     * 
     * @param watermark
     */
    protected abstract void makeForText(TextWatermark watermark);

    /**
     * 创建图片水印
     * 
     * @param watermark
     */
    protected abstract void makeForImage(ImageWatermark watermark);

    /**
     * 创建水印后(清理工作)
     * 
     * @param watermark
     */
    protected void afterMake(Watermark watermark) {
        // NOP
    }

    /**
     * 检查参数
     */
    private void check() {
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
