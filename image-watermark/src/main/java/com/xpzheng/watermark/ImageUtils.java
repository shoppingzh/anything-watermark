/**
 * 
 */
package com.xpzheng.watermark;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @author xpzheng
 *
 */
public class ImageUtils {

    public static void watermark(File src, String dest, String text, float x, float y, float opactity, float rotation)
            throws IOException {
        BufferedImage img = ImageIO.read(src);
        Graphics2D g = img.createGraphics();
        g.rotate(-0.5);
        g.setColor(new Color(0, 0, 0, 200));
        g.drawString(text, x, y);
        g.dispose();

        ImageIO.write(img, "jpg", new File(dest));
    }

}
