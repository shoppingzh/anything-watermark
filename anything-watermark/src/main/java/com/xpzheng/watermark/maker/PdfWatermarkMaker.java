package com.xpzheng.watermark.maker;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.xpzheng.watermark.AbstractWatermarkMaker;
import com.xpzheng.watermark.TextGrowable;
import com.xpzheng.watermark.WatermarkException;
import com.xpzheng.watermark.components.Align;
import com.xpzheng.watermark.components.Color;
import com.xpzheng.watermark.components.ImageWatermark;
import com.xpzheng.watermark.components.TextWatermark;
import com.xpzheng.watermark.util.MathUtils;

/**
 * PDF水印生成器
 * 
 * @author xpzheng
 *
 */
public class PdfWatermarkMaker extends AbstractWatermarkMaker implements TextGrowable {

    public PdfWatermarkMaker(File src, File dest) {
        super(src, dest);
    }

    @Override
    protected void makeForText(TextWatermark watermark) {
        PdfReader reader = null;
        PdfStamper stamper = null;
        final float size = watermark.getSize();
        float textSize = getBaseFontSize() * (1 + size * getFontSizeGrowFactor());
        try {
            reader = new PdfReader(new FileInputStream(this.src));
            stamper = new PdfStamper(reader, new FileOutputStream(this.dest));
            int pages = reader.getNumberOfPages();
            Font f = new Font(BaseFont.createFont("song.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED), textSize);
            Color textColor = watermark.getTextColor();
            f.setColor(textColor.getR(), textColor.getG(), textColor.getB()); // 字体颜色
            Phrase p = new Phrase(watermark.getContent(), f);
            for (int i = 1; i <= pages; i++) {
                Rectangle rect = reader.getPageSize(i);
                final float mw = rect.getWidth();
                final float mh = rect.getHeight();

                PdfContentByte layer = watermark.isFront() ? stamper.getOverContent(i) : stamper.getUnderContent(i); // 前置/后置
                float opacity = watermark.getOpacity(); // 透明度
                if (opacity < 1) {
                    PdfGState gs = new PdfGState();
                    gs.setFillOpacity(opacity);
                    layer.setGState(gs);
                }

                float tx = MathUtils.transCoord(watermark.getX(), mw);
                float ty = mh - MathUtils.transCoord(watermark.getY(), mh); // pdf的坐标原点在左下角，此处要作坐标变换
                // FIXME 高度计算的方法有待改进
                float tw = ColumnText.getWidth(p), th = textSize;
                Align xAlign = watermark.getxAlign(), yAlign = watermark.getyAlign();
                if (xAlign == Align.CENTER) {
                    tx = tx - tw / 2;
                } else if (xAlign == Align.END) {
                    tx = tx - tw;
                }
                if (yAlign == Align.CENTER) {
                    ty = ty - th / 2;
                } else if (yAlign == Align.START) { // 这里同样是为了坐标转换
                    ty = ty - th;
                }
                // 边缘检测与处理
                if (tx <= 0) {
                    tx = this.edgeOffset;
                } else if (tx + tw >= mw) {
                    tx = mw - tw - this.edgeOffset;
                }
                if (ty <= 0) {
                    ty = this.edgeOffset;
                } else if (ty + th >= mh) {
                    ty = mh - this.edgeOffset;
                }
//                if (watermark.getRotation() % 360 != 0) {
//                    tx = tx + tw / 2;
//                    ty = tx + th / 2;
//                }
                ColumnText.showTextAligned(layer, Element.ALIGN_LEFT, p, tx, ty, -watermark.getRotation());
            }
        } catch (DocumentException | IOException e) {
            throw new WatermarkException(WatermarkException.ERR_INNER_ERROR);
        } finally {
            if (stamper != null) {
                try {
                    stamper.close();
                } catch (DocumentException | IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                reader.close();
            }
        }
    }

    @Override
    protected void makeForImage(ImageWatermark watermark) {
        final float size = watermark.getSize();

        PdfReader reader = null;
        PdfStamper stamper = null;
        try {
            reader = new PdfReader(new FileInputStream(this.src));
            stamper = new PdfStamper(reader, new FileOutputStream(this.dest));
            int pages = reader.getNumberOfPages();

            // 取得图片
            Image img = Image.getInstance(watermark.getFile().getAbsolutePath());

            for (int i = 1; i <= pages; i++) {
                Rectangle rect = reader.getPageSize(i);
                final float mw = rect.getWidth();
                final float mh = rect.getHeight();
                float tx = MathUtils.transCoord(watermark.getX(), mw);
                float ty = mh - MathUtils.transCoord(watermark.getY(), mh);

                PdfContentByte layer = watermark.isFront() ? stamper.getOverContent(i) : stamper.getUnderContent(i);

                // 设置透明度
                float opacity = watermark.getOpacity();
                if (opacity < 1) {
                    PdfGState gs = new PdfGState();
                    gs.setFillOpacity(opacity);
                    layer.setGState(gs);
                }

                Rectangle2D bounds = MathUtils.getScaleBounds(mw, mh, img.getScaledWidth(), img.getScaledHeight(),
                        size);
                float iw = (float) bounds.getWidth(), ih = (float) bounds.getHeight();

                Align xAlign = watermark.getxAlign(), yAlign = watermark.getyAlign();
                if (xAlign == Align.CENTER) {
                    tx = tx - iw / 2;
                } else if (xAlign == Align.END) {
                    tx = tx - iw;
                }
                if (yAlign == Align.CENTER) {
                    ty = ty - ih / 2;
                } else if (yAlign == Align.START) {
                    ty = ty - ih;
                }
                // 边缘检测与处理
                if (tx <= 0) {
                    tx = this.edgeOffset;
                } else if (tx + iw >= mw) {
                    tx = mw - iw - this.edgeOffset;
                }
                if (ty <= 0) {
                    ty = this.edgeOffset;
                } else if (ty + ih >= mh) {
                    ty = mh - ih - this.edgeOffset;
                }
                layer.addImage(img, iw, 0, 0, ih, tx, ty);
            }

        } catch (IOException | DocumentException e) {
            throw new WatermarkException(WatermarkException.ERR_INNER_ERROR);
        } finally {
            if (stamper != null) {
                try {
                    stamper.close();
                } catch (DocumentException | IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                reader.close();
            }

        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.xpzheng.watermark.TextGrowable#getBaseFontSize()
     */
    @Override
    public float getBaseFontSize() {
        return 8f;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.xpzheng.watermark.TextGrowable#getFontSizeGrowFactor()
     */
    @Override
    public float getFontSizeGrowFactor() {
        return 15f;
    }

}
