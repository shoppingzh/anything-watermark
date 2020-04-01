/**
 * 
 */
package com.xpzheng.watermark;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.xpzheng.watermark.util.CommonUtils;

/**
 * @author xpzheng
 *
 */
public final class Config {

    private static final String CONFIG_FILE = "skills.properties";
    private static Config config = new Config();

    private String ffmpegHome;
    private String font;

    private Config() {
        loadConfig();
    }

    /**
     * 加载配置
     */
    private void loadConfig() {
        Properties prop = new Properties();
        InputStream in = Config.class.getClassLoader().getResourceAsStream(CONFIG_FILE);
        try {
            prop.load(in);
            this.ffmpegHome = prop.getProperty("skills.ffmpegtoolsdir");
            this.font = CommonUtils.removeExtraSlash(this.ffmpegHome + "/font.ttf");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFfmpegHome() {
        return ffmpegHome;
    }

    public void setFfmpegHome(String ffmpegHome) {
        this.ffmpegHome = ffmpegHome;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    @Override
    public String toString() {
        return "Config [ffmpegHome=" + ffmpegHome + ", font=" + font + "]";
    }

    public static Config getInstance() {
        return config;
    }

}
