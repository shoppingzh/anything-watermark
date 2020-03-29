package com.xpzheng.watermark.maker;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import com.xpzheng.watermark.components.Watermark;
import com.xpzheng.watermark.util.MathUtils;

/**
 * PDF水印生成器
 * 
 * @author xpzheng
 *
 */
public class PdfWatermarkMaker extends AbstractWatermarkMaker implements TextGrowable {

    public PdfWatermarkMaker() {
        super();
    }

    public PdfWatermarkMaker(File src, File dest) {
        super(src, dest);
    }

    @Override
    public void make(Watermark watermark) throws WatermarkException {
        super.make(watermark);
        PdfReader reader = null;
        PdfStamper stamper = null;
        try {
            reader = new PdfReader(new FileInputStream(this.src));
            int pages = reader.getNumberOfPages();
            if (pages <= 0) {
                // 这里直接返回，不要抛错，给0页的pdf加水印，不加即为成功
                return;
            }
            final float size = watermark.getSize();
            stamper = new PdfStamper(reader, new FileOutputStream(this.dest));
            if (watermark instanceof TextWatermark) {
                TextWatermark textWatermark = (TextWatermark) watermark;
                float textSize = getBaseFontSize() * (1 + size * getFontSizeGrowFactor());
                Font f = new Font(BaseFont.createFont("song.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED),
                        textSize); // 字体与字体大小
                Color textColor = textWatermark.getTextColor();
                f.setColor(textColor.getR(), textColor.getG(), textColor.getB()); // 字体颜色
                Phrase p = new Phrase(textWatermark.getContent(), f);
                for (int i = 1; i <= pages; i++) {
                    Rectangle rect = reader.getPageSize(i);
                    final float mw = rect.getWidth();
                    final float mh = rect.getHeight();

                    PdfContentByte layer = textWatermark.isFront() ? stamper.getOverContent(i)
                            : stamper.getUnderContent(i); // 前置/后置
                    float opacity = textWatermark.getOpacity(); // 透明度
                    if (opacity < 1) {
                        PdfGState gs = new PdfGState();
                        gs.setFillOpacity(opacity);
                        layer.setGState(gs);
                    }

                    float tx = MathUtils.transCoord(textWatermark.getX(), mw);
                    float ty = mh - MathUtils.transCoord(textWatermark.getY(), mh); // pdf的坐标原点在左下角，此处要作坐标变换
                    // FIXME 高度计算的方法有待改进
                    float tw = ColumnText.getWidth(p), th = textSize;
                    Align xAlign = textWatermark.getxAlign(), yAlign = textWatermark.getyAlign();
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

                    ColumnText.showTextAligned(layer, Element.ALIGN_LEFT, p, tx, ty, -textWatermark.getRotation());
                }
            } else if (watermark instanceof ImageWatermark) {
                ImageWatermark imageWatermark = (ImageWatermark) watermark;
                for (int i = 1; i <= pages; i++) {
                    Rectangle rect = reader.getPageSize(i);
                    final float mw = rect.getWidth();
                    final float mh = rect.getHeight();
                    float tx = MathUtils.transCoord(imageWatermark.getX(), mw);
                    float ty = mh - MathUtils.transCoord(imageWatermark.getY(), mh);

                    PdfContentByte layer = imageWatermark.isFront() ? stamper.getOverContent(i)
                            : stamper.getUnderContent(i);
                    float opacity = imageWatermark.getOpacity();
                    if (opacity < 1) {
                        PdfGState gs = new PdfGState();
                        gs.setFillOpacity(opacity);
                        layer.setGState(gs);
                    }

                    // 获取图片的绘制尺寸
                    Image img = Image.getInstance(imageWatermark.getFile().getAbsolutePath());
                    Rectangle2D bounds = MathUtils.getScaleBounds(mw, mh, img.getScaledWidth(), img.getScaledHeight(),
                            size);
                    float iw = (float) bounds.getWidth(), ih = (float) bounds.getHeight();

                    Align xAlign = imageWatermark.getxAlign(), yAlign = imageWatermark.getyAlign();
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
            }

        } catch (FileNotFoundException e) {
            throw new WatermarkException(WatermarkException.ERR_TARGET_NOT_FOUND);
        } catch (IOException e) {
            throw new WatermarkException(WatermarkException.ERR_READ_WRITE_ERROR);
        } catch (DocumentException e) {
            throw new WatermarkException(WatermarkException.ERR_INNER_ERROR);
        } finally {
            if (stamper != null) {
                try {
                    stamper.close();
                } catch (Exception e) {
                    // 安静关闭
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
        return 12f;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.xpzheng.watermark.TextGrowable#getFontSizeGrowFactor()
     */
    @Override
    public float getFontSizeGrowFactor() {
        return 8f;
    }
}
