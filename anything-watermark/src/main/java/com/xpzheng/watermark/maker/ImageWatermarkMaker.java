package com.xpzheng.watermark.maker;

import java.awt.AlphaComposite;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.xpzheng.watermark.AbstractWatermarkMaker;
import com.xpzheng.watermark.WatermarkException;
import com.xpzheng.watermark.components.Align;
import com.xpzheng.watermark.components.Color;
import com.xpzheng.watermark.components.ImageWatermark;
import com.xpzheng.watermark.components.TextWatermark;
import com.xpzheng.watermark.components.Watermark;
import com.xpzheng.watermark.util.MathUtils;

public class ImageWatermarkMaker extends AbstractWatermarkMaker {

    public static final String FORMAT_PNG = "png";
    public static final String FORMAT_JPG = "jpg";
    public static final String FORMAT_GIF = "gif";

    /**
     * 当水印贴边时与边缘的留白距离
     */
    private static final float EDGE_OFFSET = 15;

    private static Font BASEFONT;

    static {
        try {
            BASEFONT = Font.createFont(Font.TRUETYPE_FONT,
                ImageWatermarkMaker.class.getClassLoader().getResourceAsStream("song.ttf"));
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String format = FORMAT_JPG;

    public ImageWatermarkMaker() {
        super();
    }

    public ImageWatermarkMaker(File src, File dest) {
        super(src, dest);
    }

    public ImageWatermarkMaker(File src, File dest, String format) {
        super(src, dest);
        this.format = format;
    }

    @Override
    public void make(Watermark watermark) throws WatermarkException {
        super.make(watermark);

        try {
            BufferedImage img = ImageIO.read(this.src);
            Graphics2D g = img.createGraphics();

            float mw = img.getWidth(), mh = img.getHeight(); // 图片宽高
            float x = watermark.getX(), y = watermark.getY();
            Align xAlign = watermark.getxAlign(), yAlign = watermark.getyAlign();
            float tx = MathUtils.transCoord(x, mw), ty = MathUtils.transCoord(y, mh);

            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, watermark.getOpacity())); // 透明度(使用组合像素的方式)
            if (watermark instanceof TextWatermark) {
                TextWatermark textWatermark = (TextWatermark) watermark;
                final String text = textWatermark.getContent();
                Font f = new Font(BASEFONT.getName(), Font.PLAIN, (int) textWatermark.getTextSize()); // 字体样式、大小
                g.setFont(f);
                Color textColor = textWatermark.getTextColor();
                g.setColor(new java.awt.Color(textColor.getR(), textColor.getG(), textColor.getB(), textColor.getA())); // 文字颜色
                // 计算文本的绘制坐标
                // 1. 换算比例坐标、反向坐标为真实坐标
                // 2. 根据对齐方式计算对齐后的新坐标
                // 3. 边缘处理，所有超出的部分贴边并留出一部分空白
                // 4. 绘制时，要考虑基线的影响，需将y轴坐标抬起基线到文字底部的高度
                FontMetrics fontMetrics = g.getFontMetrics(f);
                Rectangle2D bounds = fontMetrics.getStringBounds(text, g);
                float tw = (float) bounds.getWidth(), th = (float) bounds.getHeight();
                float offsetY = (float) (th + bounds.getY()); // 字符绘制基线距离字符底部的偏移量
                // 对齐
                if (xAlign == Align.CENTER) {
                    tx = tx - tw / 2;
                } else if (xAlign == Align.END) {
                    tx = tx - tw;
                }

                if (yAlign == Align.CENTER) {
                    ty = ty - th / 2;
                }
                // 边缘处理
                if (tx <= 0) {
                    tx = EDGE_OFFSET;
                } else if (tx + tw >= mw) {
                    tx = mw - tw - EDGE_OFFSET;
                }
                if (ty <= 0) {
                    ty = th + EDGE_OFFSET;
                } else if (ty >= mh) {
                    ty = mh - EDGE_OFFSET;
                }
                // 旋转画笔
                g.rotate(Math.toRadians(textWatermark.getRotation()), tx + tw / 2, ty - th / 2);
                g.drawString(text, tx, ty - offsetY);
            } else if (watermark instanceof ImageWatermark) {
                ImageWatermark imageWatermark = (ImageWatermark) watermark;
                BufferedImage mask = ImageIO.read(imageWatermark.getFile());
                float iw = mask.getWidth(), ih = mask.getHeight();
                // 对齐
                if (xAlign == Align.CENTER) {
                    tx = tx - iw / 2;
                } else if (xAlign == Align.END) {
                    tx = tx - iw;
                }
                if (yAlign == Align.CENTER) {
                    ty = ty - ih / 2;
                } else if (yAlign == Align.END) {
                    ty = ty - ih;
                }
                // 边缘处理
                if (tx <= 0) {
                    tx = EDGE_OFFSET;
                } else if (tx + iw >= mw) {
                    tx = mw - iw - EDGE_OFFSET;
                }
                if (ty <= 0) {
                    ty = EDGE_OFFSET;
                } else if (ty + ih >= mh) {
                    ty = mh - ih - EDGE_OFFSET;
                }
                // 旋转画笔
                g.rotate(Math.toRadians(imageWatermark.getRotation()), tx + iw / 2, ty + ih / 2);
                g.drawImage(mask, (int) tx, (int) ty, (int) iw, (int) ih, null);
            }
            g.dispose(); // 释放内存
            ImageIO.write(img, this.format, this.dest); // 写出
        } catch (IOException e) {
            throw new WatermarkException(WatermarkException.ERR_READ_WRITE_ERROR);
        }
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

}
