package com.xpzheng.watermark;

import com.xpzheng.watermark.components.Watermark;

/**
 * 水印生成器
 * 
 * @author xpzheng
 *
 */
public interface WatermarkMaker {

    /**
     * 按照指定的水印配置生成水印
     * 
     * @param watermark 水印配置信息
     * @throws Exception
     */
    void make(Watermark watermark) throws WatermarkException;

}
