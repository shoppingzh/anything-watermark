package com.xpzheng.watermark.pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

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

public class PdfUtils {

    /**
     * 文字水印
     * 
     * @param pdfFile 源文件
     * @param dest 目标文件
     * @param text 水印内容
     * @param x 水印x坐标
     * @param y 水印y坐标
     * @param rotate 旋转角度
     * @param opacity 透明度
     * @throws Exception
     */
    public static void textWatermark(File pdfFile, String dest, String text, final float x, final float y, float rotate,
        float opacity) throws Exception {
        PdfReader reader = new PdfReader(new FileInputStream(pdfFile));
        int pages = reader.getNumberOfPages();
        if (pages <= 0) {
            return;
        }

        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        Font f = new Font(BaseFont.createFont("d:/video/msyh.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED), 26);
        Phrase p = new Phrase(text, f);
        for (int i = 1; i <= pages; i++) {
            Rectangle rect = reader.getPageSize(i);
            float width = rect.getWidth();
            float height = rect.getHeight();

            PdfContentByte layer = stamper.getOverContent(i);
            if (opacity < 1) {
                PdfGState gs = new PdfGState();
                gs.setFillOpacity(opacity);
                layer.setGState(gs);
            }

            float tx = transCoord(x, width);
            float ty = height - transCoord(y, height);

            // 注：设置中间对齐后，不需要计算文本宽度然后居中，会自动居中
            ColumnText.showTextAligned(layer, Element.ALIGN_CENTER, p, tx, ty, rotate);
        }
        stamper.close();
        reader.close();
        System.out.println("水印生成成功！");
    }

    /**
     * 图片水印
     * 
     * @param pdfFile 源文件
     * @param dest 目标位置
     * @param image 图片位置
     * @param x 水印x坐标
     * @param y 水印y坐标
     * @param rotate 旋转角度
     * @param opacity 透明度
     * @throws Exception
     */
    public static void imageWatermark(File pdfFile, String dest, String image, final float x, final float y, float rotate,
        float opacity) throws Exception {
        PdfReader reader = new PdfReader(new FileInputStream(pdfFile));
        int pages = reader.getNumberOfPages();
        if (pages <= 0) {
            return;
        }

        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        for (int i = 1; i <= pages; i++) {
            Rectangle rect = reader.getPageSize(i);
            float width = rect.getWidth();
            float height = rect.getHeight();

            PdfContentByte layer = stamper.getOverContent(i);
            if (opacity < 1) {
                PdfGState gs = new PdfGState();
                gs.setFillOpacity(opacity);
                layer.setGState(gs);
            }

            float tx = transCoord(x, width);
            float ty = height - transCoord(y, height);

            Image img = Image.getInstance(image);
            float w = img.getScaledWidth();
            float h = img.getScaledHeight();
            layer.addImage(img, w, 0, 0, h, tx - w / 2, ty - h / 2);

        }
        stamper.close();
        reader.close();
        System.out.println("水印生成成功！");
    }

    /**
     * 转换坐标<br>
     * 当坐标值位于(0, 1)时，按照坐标轴长度的比例来计算，否则按照实际值计算，当坐标值小于0时，则反向取值。假设轴线长度为100，则可能出现的情况有以下几种：<br>
     * 0 0 0.2 20 -0.2 80 45 45 -45 55
     * 
     * @param coord 原始坐标
     * @param full 轴线总长度
     * @return
     */
    private static float transCoord(float coord, float full) {
        float ac = Math.abs(coord);
        if (ac > 0 && ac < 1) {
            coord = full * coord;
        }
        return coord < 0 ? full + coord : coord;
    }

}
