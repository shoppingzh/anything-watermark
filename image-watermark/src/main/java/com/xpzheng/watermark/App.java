/**
 * 
 */
package com.xpzheng.watermark;

import java.io.File;
import java.io.IOException;

/**
 * @author xpzheng
 *
 */
public class App {

    public static void main(String[] args) throws IOException, Exception {
        ImageUtils.watermark(new File("d:/1.jpg"), "d:/2.jpg", "你好，水印！", 0, 40, 0.5f, 40f);
    }

}
