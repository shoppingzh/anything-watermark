package com.xpzheng.watermark.maker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.xpzheng.watermark.WatermarkMaker;
import com.xpzheng.watermark.components.Color;
import com.xpzheng.watermark.components.TextWatermark;
import com.xpzheng.watermark.components.Watermark;

public class PdfWatermarkMaker extends WatermarkMaker {

    public PdfWatermarkMaker() {
        super();
    }

    public PdfWatermarkMaker(File src, File dest, Watermark watermark) {
        super(src, dest, watermark);
    }

    @Override
    public void make() throws Exception {
        PdfReader reader = new PdfReader(new FileInputStream(this.src));
        int pages = reader.getNumberOfPages();
        if (pages <= 0) {
            return;
        }
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(this.dest));

        if (this.watermark instanceof TextWatermark) {
            TextWatermark watermark = (TextWatermark) this.watermark;
            Font f = new Font(BaseFont.createFont("song.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED), watermark.getFontSize());
            Color color = watermark.getColor();
            f.setColor(color.getR(), color.getG(), color.getB());
            Phrase p = new Phrase(watermark.getContent(), f);

            for (int i = 1; i <= pages; i++) {
                Rectangle rect = reader.getPageSize(i);
                float width = rect.getWidth();
                float height = rect.getHeight();

                PdfContentByte layer = stamper.getOverContent(i);
                float opacity = watermark.getOpacity();
                if (opacity < 1) {
                    PdfGState gs = new PdfGState();
                    gs.setFillOpacity(opacity);
                    layer.setGState(gs);
                }

                float tx = transCoord(watermark.getX(), width);
                float ty = height - transCoord(watermark.getY(), height);

                ColumnText.showTextAligned(layer, Element.ALIGN_CENTER, p, tx, ty, watermark.getRotation());
            }
        }

        stamper.close();
        reader.close();
    }

}
