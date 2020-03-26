package com.xpzheng.watermark.maker;

import java.io.File;

import com.xpzheng.watermark.AbstractWatermarkMaker;
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
public class VideoWatermarkMaker extends AbstractWatermarkMaker {

    public VideoWatermarkMaker(File src, File dest) {
        super(src, dest);
    }

    @Override
    public void make(Watermark watermark) throws WatermarkException {
        super.make(watermark);
        if (this.dest.exists()) {
            this.dest.delete();
        }
        // FIXME 路径写死了，需要使用活的路径
        String fontPath = "d:/watermark/song.ttf";
        StringBuilder cmd = new StringBuilder("ffmpeg ");
        cmd.append(String.format("-i %s -vf \"", this.src.getAbsolutePath())); // 输入视频并添加过滤器
        if (watermark instanceof TextWatermark) {
            TextWatermark textWatermark = (TextWatermark) watermark;
            cmd.append("drawtext=");
            cmd.append(String.format("fontfile=%s: ", convertPath(fontPath))); // 字体
            cmd.append(String.format("fontsize=%.1f: ", textWatermark.getTextSize())); // 字号
            Color textColor = textWatermark.getTextColor();
            cmd.append(String.format("fontcolor=%s: ", CommonUtils.rgba2Hex(textColor.getR(), textColor.getG(),
                textColor.getB(), (int) (textWatermark.getOpacity() * 255)))); // 文字颜色
            cmd.append(String.format("text='%s': ", textWatermark.getContent())); // 文本内容
            // 设置位置
            float x = textWatermark.getX(), y = textWatermark.getY();
            float ax = Math.abs(x), ay = Math.abs(y);
            Align xAlign = textWatermark.getxAlign(), yAlign = textWatermark.getyAlign();
            cmd.append("x=(");
            if (ax > 0 && ax < 1) {
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
            if (ay > 0 && ay < 1) {
                cmd.append(String.format(y > 0 ? "(main_h * %f)" : "(main_h - main_h * %f)", ay));
            } else {
                cmd.append(String.format(y >= 0 ? "%f" : "(main_h + %f)", y));
            }
            if (yAlign == Align.CENTER) {
                cmd.append(" - text_h / 2");
            } else if (yAlign == Align.END) {
                cmd.append(" - text_h");
            }
            cmd.append("): ");
        } else if (watermark instanceof ImageWatermark) {
            ImageWatermark imageWatermark = (ImageWatermark) watermark;
            String filePath = imageWatermark.getFile().getAbsolutePath();
            // 对文件路径的特殊字符进行转义
            filePath = convertPath(filePath);
            cmd.append(String.format("movie=%s [watermark]; [in][watermark] overlay=", filePath)); // 水印图片
            // 设置位置
            float x = imageWatermark.getX(), y = imageWatermark.getY();
            float ax = Math.abs(x), ay = Math.abs(y);
            Align xAlign = imageWatermark.getxAlign(), yAlign = imageWatermark.getyAlign();
            cmd.append("x=(");
            if (ax > 0 && ax < 1) {
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
            if (ay > 0 && ay < 1) {
                cmd.append(String.format(y > 0 ? "(main_h * %f)" : "(main_h - main_h * %f)", ay));
            } else {
                cmd.append(String.format(y >= 0 ? "%f" : "(main_h + %f)", y));
            }
            if (yAlign == Align.CENTER) {
                cmd.append(" - overlay_h / 2");
            } else if (yAlign == Align.END) {
                cmd.append(" - overlay_h");
            }
            cmd.append("): ");
            cmd.append("[out]");
        }
        cmd.append(String.format("\" %s", this.dest.getAbsolutePath()));
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
}
