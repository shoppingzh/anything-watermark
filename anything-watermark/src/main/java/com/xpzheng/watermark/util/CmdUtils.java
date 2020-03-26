package com.xpzheng.watermark.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 * 命令行执行工具集合
 * 
 * @author xpzheng
 *
 */
public class CmdUtils {

    /**
     * 执行某个命令
     * 
     * @param cmd
     * @return
     */
    public static final int execute(String cmd) {
        Process exec = null;
        try {
            exec = Runtime.getRuntime().exec(cmd);
            final Process exec2 = exec;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        print(exec2.getErrorStream(), true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        print(exec2.getInputStream(), false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            return exec.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (exec != null) {
                exec.destroy();
            }
        }

        return -1;
    }

    private static final void print(InputStream in, boolean error) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "gbk"));
        String line = null;
        while ((line = br.readLine()) != null) {
            @SuppressWarnings("resource")
            PrintStream ps = error ? System.err : System.out;
            ps.println(line);
        }
        br.close();
    }

}
