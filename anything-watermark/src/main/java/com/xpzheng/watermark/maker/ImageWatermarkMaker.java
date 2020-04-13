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
import com.xpzheng.watermark.util.MathUtils;

/**
 * 图片水印生成器
 * 
 * @author xpzheng
 *
 */
public class ImageWatermarkMaker extends AbstractWatermarkMaker {

    public static final String FORMAT_PNG = "png";
    public static final String FORMAT_JPG = "jpg";
    public static final String FORMAT_GIF = "gif";

    /**
     * 当水印贴边时与边缘的留白距离
     */
    private static final float EDGE_OFFSET = 15;

    /**
     * 文字水印最小高度占比
     */
    private static final float MIN_TEXT_PERCENT = 0.035f;

    /**
     * 文字水印最大高度占比
     */
    private static final float MAX_TEXT_PERCENT = 0.2f;

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
    private BufferedImage srcImage;

    public ImageWatermarkMaker(File src, File dest) {
        super(src, dest);
        try {
            this.srcImage = ImageIO.read(src);
        } catch (IOException e) {
            throw new WatermarkException(WatermarkException.ERR_READ_WRITE_ERROR);
        }
    }

    public ImageWatermarkMaker(File src, File dest, String format) {
        this(src, dest);
        this.format = format;
    }

    /**
     * 位置：√
     * 大小：√
     * 旋转：√
     * 透明：√
     * 颜色：√
     */
    @Override
    protected void makeForText(TextWatermark watermark) {
        Graphics2D g = srcImage.createGraphics();
        float mw = srcImage.getWidth(), mh = srcImage.getHeight(); // 图片宽高
        float x = watermark.getX(), y = watermark.getY();
        Align xAlign = watermark.getxAlign(), yAlign = watermark.getyAlign();
        float tx = MathUtils.transCoord(x, mw), ty = MathUtils.transCoord(y, mh);
        float size = watermark.getSize();
        // 计算该字体绘制出的水印高度，如果不足最小高度(或大于最大高度)，则自动伸缩到最佳高度
        if (size < MIN_TEXT_PERCENT) {
            size = MIN_TEXT_PERCENT;
        }
        if (size > MAX_TEXT_PERCENT) {
            size = MAX_TEXT_PERCENT;
        }
        final String text = watermark.getContent();
        float textSize = 12f;
        Font f = new Font(BASEFONT.getName(), Font.BOLD, (int) textSize); // 字体样式、大小
        g.setFont(f);
        
        final float textHeight = Math.min(mw, mh) * size;
        Rectangle2D originalBounds = g.getFontMetrics(f).getStringBounds(text, g);
        textSize = (float) (textSize * textHeight / originalBounds.getHeight());
        f = new Font(f.getName(), Font.BOLD, (int) textSize);
        g.setFont(f);

        Color textColor = watermark.getTextColor();
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
        
        // 由于从底部开始绘制文字，需要再将y轴分量向下平移半个文字的高度；另外，处理基线的y轴偏移量
        ty = ty + th / 2 + offsetY;
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
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, watermark.getOpacity())); // 透明度(使用组合像素的方式)
        g.rotate(Math.toRadians(watermark.getRotation()), tx + tw / 2, ty - th / 2); // 旋转画笔
        g.drawString(text, tx, ty);
        g.dispose(); // 释放内存
        writeToDest(); // 写出
    }

    /**
     * 位置：√
     * 大小：√
     * 旋转：√
     * 透明：√
     */
    @Override
    protected void makeForImage(ImageWatermark watermark) {
        Graphics2D g = srcImage.createGraphics();
        float mw = srcImage.getWidth(), mh = srcImage.getHeight(); // 图片宽高
        float x = watermark.getX(), y = watermark.getY();
        Align xAlign = watermark.getxAlign(), yAlign = watermark.getyAlign();
        float tx = MathUtils.transCoord(x, mw), ty = MathUtils.transCoord(y, mh);
        float size = watermark.getSize();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, watermark.getOpacity())); // 透明度(使用组合像素的方式)
        try {
            BufferedImage mask = ImageIO.read(watermark.getFile());
            // 根据size计算绘制尺寸
            Rectangle2D bounds = MathUtils.getScaleBounds(mw, mh, mask.getWidth(), mask.getHeight(), size);
            float iw = (float) bounds.getWidth(), ih = (float) bounds.getHeight();
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
            g.rotate(Math.toRadians(watermark.getRotation()), tx + iw / 2, ty + ih / 2);
            g.drawImage(mask, (int) tx, (int) ty, (int) iw, (int) ih, null);
        } catch (IOException e) {
            throw new WatermarkException(WatermarkException.ERR_READ_WRITE_ERROR);
        }
        g.dispose(); // 释放内存
        writeToDest(); // 写出
    }

    /**
     * 写出原始图片到目标文件中
     * 
     * @throws WatermarkException
     */
    private void writeToDest() throws WatermarkException {
        try {
            ImageIO.write(this.srcImage, this.format, this.dest);
        } catch (IOException e) {
            throw new WatermarkException(WatermarkException.ERR_INNER_ERROR);
        }
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
