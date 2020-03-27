package com.xpzheng.watermark.maker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import com.xpzheng.watermark.AbstractWatermarkMaker;
import com.xpzheng.watermark.WatermarkException;
import com.xpzheng.watermark.components.Color;
import com.xpzheng.watermark.components.TextWatermark;
import com.xpzheng.watermark.components.Watermark;
import com.xpzheng.watermark.util.CommonUtils;

/**
 * Word文档水印生成器
 * 
 * @author xpzheng
 *
 */
public class WordWatermarkMaker extends AbstractWatermarkMaker {

    private static final String DOC = "doc";
    private static final String DOCX = "docx";

    public WordWatermarkMaker(File src, File dest) {
        super(src, dest);
    }

    @Override
    public void make(Watermark watermark) throws WatermarkException {
        super.make(watermark);
        String extension = FilenameUtils.getExtension(this.src.getName());
        if (DOC.equalsIgnoreCase(extension)) {
            makeForDoc(watermark);
        } else if (DOCX.equalsIgnoreCase(extension)) {
            makeForDocx(watermark);
        }
    }

    private void makeForDoc(Watermark watermark) {
        try(HWPFDocument doc = new HWPFDocument(new FileInputStream(this.src))) {
            
            System.out.println(doc);
            
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new WatermarkException(WatermarkException.ERR_SOURCE_NOT_FOUND);
        } catch (IOException e) {
            throw new WatermarkException(WatermarkException.ERR_INNER_ERROR);
        }
    }

    private void makeForDocx(Watermark watermark) {
        try (XWPFDocument doc = new XWPFDocument(new FileInputStream(this.src))) {
            XWPFHeaderFooterPolicy headerFooterPolicy = doc.getHeaderFooterPolicy();
            if (headerFooterPolicy == null) {
                headerFooterPolicy = doc.createHeaderFooterPolicy();
            }

            if (watermark instanceof TextWatermark) {
                TextWatermark textWatermark = (TextWatermark) watermark;
                headerFooterPolicy.createWatermark(textWatermark.getContent());
                XWPFHeader header = headerFooterPolicy.getHeader(XWPFHeaderFooterPolicy.DEFAULT);
                XWPFParagraph paragraph = header.getParagraphArray(0);

                org.apache.xmlbeans.XmlObject[] xmlobjects = paragraph.getCTP().getRArray(0).getPictArray(0)
                    .selectChildren(new javax.xml.namespace.QName("urn:schemas-microsoft-com:vml", "shape"));

                if (xmlobjects.length > 0) {
                    com.microsoft.schemas.vml.CTShape ctshape = (com.microsoft.schemas.vml.CTShape) xmlobjects[0];
                    // 文字颜色
                    Color textColor = textWatermark.getTextColor();
                    ctshape.setFillcolor(String.format("#%s",
                        CommonUtils.rgb2Hex(textColor.getR(), textColor.getG(), textColor.getB())));
                    // 旋转角度
                    ctshape.setStyle(ctshape.getStyle() + String.format(";rotation:%f", textWatermark.getRotation()));
                }
            } else {
                // 暂不支持图片水印
                throw new WatermarkException(WatermarkException.ERR_WATERMARK_INVALID);
            }
            // 写出
            doc.write(new FileOutputStream(this.dest));
        } catch (FileNotFoundException e) {
            throw new WatermarkException(WatermarkException.ERR_SOURCE_NOT_FOUND);
        } catch (IOException e) {
            throw new WatermarkException(WatermarkException.ERR_INNER_ERROR);
        }
    }

}
