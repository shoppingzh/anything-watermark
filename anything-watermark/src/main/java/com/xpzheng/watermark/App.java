/**
 * 
 */
package com.xpzheng.watermark;

import java.io.File;

import com.xpzheng.watermark.components.Color;
import com.xpzheng.watermark.components.TextWatermark;
import com.xpzheng.watermark.components.Watermark;

/**
 * @author xpzheng
 *
 */
public class App {

    public static void main(String[] args) throws Exception {
        TextWatermark watermark = new Watermark.Builder().center().opactiy(0.45f).rotate(-45).createText("内部使用");
        watermark.setColor(Color.valueOf(0, 255, 0, 1));
        WatermarkUtils.watermark(new File("d:/1.pdf"), new File("d:/hello.pdf"), watermark);
    }

}
