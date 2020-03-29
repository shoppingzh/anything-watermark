/**
 * 
 */
package com.xpzheng.watermark.controller;

import java.io.File;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xpzheng.watermark.AnythingWatermark;
import com.xpzheng.watermark.WatermarkException;
import com.xpzheng.watermark.components.Align;
import com.xpzheng.watermark.components.Color;
import com.xpzheng.watermark.components.Watermark;
import com.xpzheng.watermark.components.Watermark.Builder;
import com.xpzheng.watermark.util.WebUtils;
import com.xpzheng.watermark.web.AjaxResult;

/**
 * @author xpzheng
 *
 */
@RestController
@RequestMapping("watermark")
public class WatermarkController {
    
    public static final String WATERMARK_PATH = "watermark";
    
    @PostMapping("generate")
    public AjaxResult generate(String src, int type, float size, float x, float y, float xAlign, float yAlign, float rotation, float opacity,
            String text, String textColor, String image, HttpServletRequest req) {
        File uploadPath = new File(req.getServletContext().getRealPath("upload"));
        Builder builder = new Watermark.Builder()
          .size(size)
          .position(x, y)
          .align(xAlign == 0.5f ? Align.CENTER : (xAlign > 0.5f ? Align.END : Align.START), yAlign == 0.5f ? Align.CENTER : (yAlign > 0.5f ? Align.END : Align.START))
          .opactiy(opacity / 100)
          .rotate(rotation);
        Watermark watermark = type == 1 
            ? builder.createText(text, Color.valueOf(64, 64, 64, 255))
            : builder.createImage(uploadPath.getAbsolutePath() + "/" + image);
        
        String filename = UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(src);
        
        File path = WebUtils.getWebPath(req, WATERMARK_PATH);
        try {
            AnythingWatermark.make(new File(uploadPath, src), new File(path, filename), watermark);
        } catch (WatermarkException e) {
            e.printStackTrace();
            return AjaxResult.failure(e.getErrorCode());
        }
        return AjaxResult.success(filename);
    }

}
