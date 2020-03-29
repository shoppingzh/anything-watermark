/**
 * 
 */
package com.xpzheng.watermark;

/**
 * @author xpzheng
 *
 */
public interface TextGrowable {
    
    /**
     * 文字水印绘制的基础字体大小
     * @return
     */
    float getBaseFontSize();
    
    /**
     * 随着水印size的增大，字体大小扩大的因数
     * @return
     */
    float getFontSizeGrowFactor();

}
