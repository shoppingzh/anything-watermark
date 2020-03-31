package com.xpzheng.watermark.maker;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

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
 * 使用ffmpeg进行加水印操作，为了保证ffmpeg进程增加占用过多的资源，会将所有的加水印任务放置在任务队列中，每次保证只有一个ffmpeg进程
 * 
 * @author xpzheng
 *
 */
public class VideoWatermarkMaker extends AbstractWatermarkMaker implements TextGrowable {
    
//    private static Logger LOG = LoggerFactory.getLogger(VideoWatermarkMaker.class);
//    private static final ExecutorService SERVICE = Executors.newSingleThreadExecutor(); // 单线程线程池
    private static final String FFMPEG = String.format("%s/ffmpeg.exe ", Config.getInstance().getFfmpegHome());
    
    public VideoWatermarkMaker(File src, File dest) {
        super(src, dest);
    }
    
    @Override
    protected void makeForText(TextWatermark watermark) {
        StringBuilder cmd = new StringBuilder(FFMPEG);
        String fontPath = Config.getInstance().getFont();
        cmd.append(String.format("-i %s ", src.getAbsolutePath()));
        cmd.append("-filter_complex \"drawtext=");
        cmd.append(String.format("fontfile=%s: ", convertPath(fontPath))); // 字体
        cmd.append(String.format("fontsize=%.1f: ", getBaseFontSize() * (1 + watermark.getSize() * getFontSizeGrowFactor()))); // 字号
        Color textColor = watermark.getTextColor();
        cmd.append(String.format("fontcolor=%s: ", CommonUtils.rgba2Hex(textColor.getR(), textColor.getG(),
            textColor.getB(), (int) (watermark.getOpacity() * 255)))); // 文字颜色
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
        CmdUtils.execute(cmd.toString());
    }

    @Override
    protected void makeForImage(ImageWatermark watermark) {
        StringBuilder cmd = new StringBuilder(FFMPEG);
        cmd.append(String.format("-i %s ", src.getAbsolutePath()));
        String filePath = watermark.getFile().getAbsolutePath();
        float ratio = 1;
        try {
            BufferedImage image = ImageIO.read(watermark.getFile());
            ratio = 1f * image.getWidth() / image.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
        }
        cmd.append(String
            .format(
                "-i %s -filter_complex \"[1:v][0:v]scale2ref=oh * %f:sqrt(main_w * main_h * %f * %f)[wm][video];[video][wm] overlay=",
                filePath, ratio, watermark.getSize(), ratio)); // 水印图片

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
