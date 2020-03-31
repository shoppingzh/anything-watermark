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
import com.xpzheng.watermark.components.Align;
import com.xpzheng.watermark.components.Color;
import com.xpzheng.watermark.components.ImageWatermark;
import com.xpzheng.watermark.components.TextWatermark;
import com.xpzheng.watermark.util.CmdUtils;
import com.xpzheng.watermark.util.CommonUtils;

/**
 * 视频水印生成器<br>
 * 
 * @author xpzheng
 *
 */
public class VideoWatermarkMaker extends AbstractWatermarkMaker implements TextGrowable {

    private static Logger LOG = LoggerFactory.getLogger(VideoWatermarkMaker.class);
    private static final String FFMPEG = String.format("%s/ffmpeg.exe ", Config.getInstance().getFfmpegHome());

    /**
     * 是否在加水印同时进行h264转码，默认同时转码
     */
    private boolean encodeH264 = true;

    public VideoWatermarkMaker(File src, File dest) {
        super(src, dest);
    }

    public VideoWatermarkMaker(File src, File dest, boolean encodeH264) {
        this(src, dest);
        this.encodeH264 = encodeH264;
    }

    @Override
    protected void makeForText(TextWatermark watermark) {
        StringBuilder cmd = new StringBuilder(FFMPEG);
        String fontPath = Config.getInstance().getFont();
        cmd.append(String.format("-i %s ", src.getAbsolutePath())); // 输入视频
        if (this.encodeH264) {
            cmd.append("-pix_fmt yuv420p -c:v libx264 -strict -2 "); // h264编码
        }
        cmd.append("-filter_complex \"drawtext="); // 添加过滤器
        cmd.append(String.format("fontfile=%s: ", convertPath(fontPath))); // 字体
        cmd.append(String.format("fontsize=%.1f: ", getBaseFontSize() * (1 + watermark.getSize() * getFontSizeGrowFactor()))); // 字号
        Color textColor = watermark.getTextColor();
        cmd.append(String.format("fontcolor=%s: ",
            CommonUtils.rgba2Hex(textColor.getR(), textColor.getG(), textColor.getB(), (int) (watermark.getOpacity() * 255)))); // 文字颜色
        cmd.append(String.format("text='%s': ", watermark.getContent())); // 文本内容
        // 设置位置
        float x = watermark.getX(), y = watermark.getY();
        float ax = Math.abs(x), ay = Math.abs(y);
        Align xAlign = watermark.getxAlign(), yAlign = watermark.getyAlign();
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
        cmd.append("): \" ");
        cmd.append(String.format("%s ", dest.getAbsolutePath()));
        LOG.debug("执行命令：{}", cmd.toString());
        CmdUtils.execute(cmd.toString());
    }

    @Override
    protected void makeForImage(ImageWatermark watermark) {
        StringBuilder cmd = new StringBuilder(FFMPEG);
        cmd.append(String.format("-i %s ", src.getAbsolutePath())); // 输入视频
        if (this.encodeH264) {
            cmd.append("-pix_fmt yuv420p -c:v libx264 -strict -2 "); // h264编码
        }
        String filePath = watermark.getFile().getAbsolutePath();
        float ratio = 1;
        try {
            // FIXME 这里的代码本不需要，但是ffmpeg计算的图片的宽高比似乎不正确，于是在这里计算一下
            BufferedImage image = ImageIO.read(watermark.getFile());
            ratio = 1f * image.getWidth() / image.getHeight();
            image = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        cmd.append(String
            .format(
                "-i %s -filter_complex \"[1:v][0:v]scale2ref=oh * %f:sqrt(main_w * main_h * %f * %f)[wm][video];[video][wm] overlay=",
                filePath, ratio, watermark.getSize(), ratio)); // 添加过滤器

        // 设置位置
        float x = watermark.getX(), y = watermark.getY();
        float ax = Math.abs(x), ay = Math.abs(y);
        Align xAlign = watermark.getxAlign(), yAlign = watermark.getyAlign();
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
        cmd.append(String.format("%s ", dest.getAbsolutePath()));
        LOG.debug("执行命令：{}", cmd.toString());
        CmdUtils.execute(cmd.toString());
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

    public boolean isEncodeH264() {
        return encodeH264;
    }

    public void setEncodeH264(boolean encodeH264) {
        this.encodeH264 = encodeH264;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.xpzheng.watermark.TextGrowable#getBaseFontSize()
     */
    @Override
    public float getBaseFontSize() {
        return 24f;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.xpzheng.watermark.TextGrowable#getFontSizeGrowFactor()
     */
    @Override
    public float getFontSizeGrowFactor() {
        return 4f;
    }
}
