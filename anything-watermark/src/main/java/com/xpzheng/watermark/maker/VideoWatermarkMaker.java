package com.xpzheng.watermark.maker;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xpzheng.watermark.AbstractWatermarkMaker;
import com.xpzheng.watermark.Config;
import com.xpzheng.watermark.TextGrowable;
import com.xpzheng.watermark.WatermarkException;
import com.xpzheng.watermark.components.Align;
import com.xpzheng.watermark.components.Color;
import com.xpzheng.watermark.components.ImageWatermark;
import com.xpzheng.watermark.components.TextWatermark;
import com.xpzheng.watermark.components.Watermark;
import com.xpzheng.watermark.util.CmdUtils;
import com.xpzheng.watermark.util.CommonUtils;

/**
 * 视频水印生成器
 * 
 * @author xpzheng
 *
 */
public class VideoWatermarkMaker extends AbstractWatermarkMaker implements TextGrowable {
    
    private static Logger LOG = LoggerFactory.getLogger(VideoWatermarkMaker.class); 
    
    public VideoWatermarkMaker(File src, File dest) {
        super(src, dest);
    }
    
    @Override
    public void make(Watermark watermark) throws WatermarkException {
        super.make(watermark);
        if (this.dest.exists()) {
            this.dest.delete();
        }
        String ffmpegHome = Config.getInstance().getFfmpegHome();
        if (ffmpegHome == null) {
            throw new WatermarkException(WatermarkException.ERR_MISS_CONFIG);
        }
        final float size = watermark.getSize();
        String fontPath = Config.getInstance().getFont();
        StringBuilder cmd = new StringBuilder(ffmpegHome).append(" ");
        cmd.append(String.format("-i %s ", this.src.getAbsolutePath())); // 输入视频并添加过滤器
        if (watermark instanceof TextWatermark) {
            TextWatermark textWatermark = (TextWatermark) watermark;
            cmd.append("-filter_complex \"drawtext=");
            cmd.append(String.format("fontfile=%s: ", convertPath(fontPath))); // 字体
            cmd.append(String.format("fontsize=%.1f: ", getBaseFontSize() * (1 + size * getFontSizeGrowFactor()))); // 字号
            Color textColor = textWatermark.getTextColor();
            cmd.append(String.format("fontcolor=%s: ", CommonUtils.rgba2Hex(textColor.getR(), textColor.getG(),
                textColor.getB(), (int) (textWatermark.getOpacity() * 255)))); // 文字颜色
            cmd.append(String.format("text='%s': ", textWatermark.getContent())); // 文本内容
            // 设置位置
            float x = textWatermark.getX(), y = textWatermark.getY();
            float ax = Math.abs(x), ay = Math.abs(y);
            Align xAlign = textWatermark.getxAlign(), yAlign = textWatermark.getyAlign();
            cmd.append("x=(");
            if (ax > 0 && ax <= 1) {
                cmd.append(String.format(x > 0 ? "(main_w * %f)" : "(main_w - main_w * %f)", ax));
            } else {
                cmd.append(String.format(x >= 0 ? "%f" : "(main_w + %f)", x));
            }
            if (xAlign == Align.CENTER) {
                cmd.append(" - text_w / 2");
            } else if (xAlign == Align.END) {
                cmd.append(" - text_w");
            }
            cmd.append("): ");

            cmd.append("y=(");
            if (ay > 0 && ay <= 1) {
                cmd.append(String.format(y > 0 ? "(main_h * %f)" : "(main_h - main_h * %f)", ay));
            } else {
                cmd.append(String.format(y >= 0 ? "%f" : "(main_h + %f)", y));
            }
            if (yAlign == Align.CENTER) {
                cmd.append(" - text_h / 2");
            } else if (yAlign == Align.END) {
                cmd.append(" - text_h");
            }
            cmd.append("): \"");
        } else if (watermark instanceof ImageWatermark) {
            ImageWatermark imageWatermark = (ImageWatermark) watermark;
            String filePath = imageWatermark.getFile().getAbsolutePath();
            float ratio = 1;
            try {
                BufferedImage image = ImageIO.read(imageWatermark.getFile());
                ratio = 1f * image.getWidth() / image.getHeight();
            } catch (IOException e) {
                e.printStackTrace();
            }
            cmd.append(String.format("-i %s -filter_complex \"[1:v][0:v]scale2ref=oh * %f:sqrt(main_w * main_h * %f * %f)[wm][video];[video][wm] overlay=", filePath, ratio, size, ratio)); // 水印图片
            
            // 设置位置
            float x = imageWatermark.getX(), y = imageWatermark.getY();
            float ax = Math.abs(x), ay = Math.abs(y);
            Align xAlign = imageWatermark.getxAlign(), yAlign = imageWatermark.getyAlign();
            cmd.append("x=(");
            if (ax > 0 && ax <= 1) {
                cmd.append(String.format(x > 0 ? "(main_w * %f)" : "(main_w - main_w * %f)", ax));
            } else {
                cmd.append(String.format(x >= 0 ? "%f" : "(main_w + %f)", x));
            }

            if (xAlign == Align.CENTER) {
                cmd.append(" - overlay_w / 2");
            } else if (xAlign == Align.END) {
                cmd.append(" - overlay_w");
            }
            cmd.append("): ");

            cmd.append("y=(");
            if (ay > 0 && ay <= 1) {
                cmd.append(String.format(y > 0 ? "(main_h * %f)" : "(main_h - main_h * %f)", ay));
            } else {
                cmd.append(String.format(y >= 0 ? "%f" : "(main_h + %f)", y));
            }
            if (yAlign == Align.CENTER) {
                cmd.append(" - overlay_h / 2");
            } else if (yAlign == Align.END) {
                cmd.append(" - overlay_h");
            }
            cmd.append("): \"");
        }
        cmd.append(String.format(" %s", this.dest.getAbsolutePath()));
        LOG.debug("视频水印命令执行: {}", cmd.toString());
        int status = CmdUtils.execute(cmd.toString());
        if (status != 0) {
            throw new WatermarkException(WatermarkException.ERR_INNER_ERROR);
        }
    }

    /**
     * 转换windows/linux路径为ffmpeg能识别的路径
     * 
     * @param path
     * @return
     */
    private static String convertPath(String path) {
        return path == null ? null : path.replaceAll("\\\\", "/").replaceAll(":", "\\\\\\\\:");
    }

    /* (non-Javadoc)
     * @see com.xpzheng.watermark.TextGrowable#getBaseFontSize()
     */
    @Override
    public float getBaseFontSize() {
        return 24f;
    }

    /* (non-Javadoc)
     * @see com.xpzheng.watermark.TextGrowable#getFontSizeGrowFactor()
     */
    @Override
    public float getFontSizeGrowFactor() {
        return 4f;
    }
}
