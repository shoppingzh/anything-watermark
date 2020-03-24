/**
 * 
 */
package com.xpzheng.watermark;

import com.xpzheng.watermark.cmd.CmdUtils;

/**
 * @author xpzheng
 *
 */
public class App {

    public static void main(String[] args) throws Exception {

//        String cmd = String.format(
//            "d:/tools/ffmpeg/x64/ffmpeg.exe -i %s -vf \"movie=%s[watermark];[in][watermark]overlay=%d:%d:%d[out]\" %s",
//            "d:1.mp4", "logo.png", 10, 10, 1, "d:/2.mp4");
        String cmd = String.format("ffmpeg -i %s -vf \"drawtext=fontfile='d\\:\\\\video\\\\msyh.ttf': fontsize=30: text='Hello, world!'\" %s", 
            "d:/video/1.mp4", "d:/video/444.mp4");
//        System.out.println(cmd);
         CmdUtils.execute(cmd);
         
    }

}
