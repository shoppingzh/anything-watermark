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
     * ����ˮӡ
     * 
     * @param pdfFile Դ�ļ�
     * @param dest Ŀ���ļ�
     * @param text ˮӡ����
     * @param x ˮӡx����
     * @param y ˮӡy����
     * @param rotate ��ת�Ƕ�
     * @param opacity ͸����
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
        Font f = new Font(BaseFont.createFont("song.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED), 26);
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

            // ע�������м����󣬲���Ҫ�����ı����Ȼ����У����Զ�����
            ColumnText.showTextAligned(layer, Element.ALIGN_CENTER, p, tx, ty, rotate);
        }
        stamper.close();
        reader.close();
        System.out.println("ˮӡ���ɳɹ���");
    }

    /**
     * ͼƬˮӡ
     * 
     * @param pdfFile Դ�ļ�
     * @param dest Ŀ��λ��
     * @param image ͼƬλ��
     * @param x ˮӡx����
     * @param y ˮӡy����
     * @param rotate ��ת�Ƕ�
     * @param opacity ͸����
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
        System.out.println("ˮӡ���ɳɹ���");
    }

    /**
     * ת������<br>
     * ������ֵλ��(0, 1)ʱ�����������᳤�ȵı��������㣬������ʵ��ֵ���㣬������ֵС��0ʱ������ȡֵ���������߳���Ϊ100������ܳ��ֵ���������¼��֣�<br>
     * 0 0 0.2 20 -0.2 80 45 45 -45 55
     * 
     * @param coord ԭʼ����
     * @param full �����ܳ���
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
